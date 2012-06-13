/*
 * Copyright © 2011-2012 Ejwa Software. All rights reserved.
 *
 * This file is part of Dinja Engine. Dinja Engine is a OpenGLES2
 * 3D engine with physics support developed for the Android platform.
 *
 * Dinja Engine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Dinja Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Dinja Engine. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ejwa.dinja.demo.activity;

import android.os.Bundle;
import com.ejwa.dinja.demo.model.RubiksCube;
import com.ejwa.dinja.demo.model.RubiksCube.BoxSection;
import com.ejwa.dinja.engine.activity.DinjaActivity;
import com.ejwa.dinja.engine.controller.Controllable;
import com.ejwa.dinja.engine.controller.animator.IAnimatorListener;
import com.ejwa.dinja.engine.controller.animator.RotationAnimator;
import com.ejwa.dinja.engine.controller.input.IFingerDeltaMovementInputListener;
import com.ejwa.dinja.engine.controller.input.IFingerFlingMeshInputListener;
import com.ejwa.dinja.engine.model.Camera;
import com.ejwa.dinja.engine.model.ease.CubicOutEase;
import com.ejwa.dinja.engine.model.material.Texture;
import com.ejwa.dinja.engine.model.node.BaseNode;
import com.ejwa.dinja.engine.model.node.Group;
import com.ejwa.dinja.engine.model.node.INode;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.model.node.Scene;
import com.ejwa.dinja.engine.util.TextureLoader;
import com.ejwa.dinja.engine.view.DebugView;
import com.ejwa.dinja.engine.view.SceneView;
import com.ejwa.dinja.opengles.display.IFrameUpdateListener;
import com.ejwa.dinja.utility.type.Tuple;
import java.util.ArrayList;
import java.util.List;
import org.openmali.vecmath2.Point2f;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;

public class RubiksCubeActivity extends DinjaActivity {
	private boolean isRolling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Texture texture = TextureLoader.load(getAssets(), "rubik_sides.png");
		final RubiksCube rubiksCube = new RubiksCube(texture);
		final Scene scene = new Scene(new Camera(), rubiksCube);

		registerView(new SceneView(scene));
		registerView(new DebugView(1, this, scene));
		registerController(new RubiksCubeController(rubiksCube));
		registerController(new RubiksCubeFlingController(scene, rubiksCube));
	}

	private static class RubiksCubeController implements Controllable, IFingerDeltaMovementInputListener, IFrameUpdateListener {
		private final RubiksCube rubiksCube;
		private float rotationFromX;
		private float rotationFromY;

		public RubiksCubeController(RubiksCube rubiksCube) {
			this.rubiksCube = rubiksCube;
		}

		@Override
		public void onFingerDeltaMovementInput(float x, float y) {
			rotationFromX += x / 6;
			rotationFromY += y / 6;
		}

		@Override public void onFingerDeltaMovementEndInput() { /* Ignore this event */ }

		@Override
		public void onFrameUpdate(long milliSecondsSinceLastFrame) {
			rubiksCube.getRotator().rotateY(rotationFromX * milliSecondsSinceLastFrame);
			rubiksCube.getRotator().rotateX(rotationFromY * milliSecondsSinceLastFrame);
			rotationFromX = 0;
			rotationFromY = 0;
		}
	}

	private class RubiksCubeFlingController implements Controllable, IFingerFlingMeshInputListener {
		private final Scene scene;
		private final RubiksCube rubiksCube;

		public RubiksCubeFlingController(Scene scene, RubiksCube rubiksCube) {
			this.scene = scene;
			this.rubiksCube = rubiksCube;
		}

		@Override
		public void onFingerFlingMeshInput(List<Tuple<Point2f, Mesh>> meshes) {
			if (!isRolling) {
				final BoxSection[] b = Tuple.getBList(meshes).toArray(new BoxSection[meshes.size()]);
				final Point2f middlePoint = meshes.get(meshes.size() / 2).getA();
				final Vector3f v = Vector3f.fromPool();
				scene.getCamera().getPointWorldCoordinates(new Vector2f(middlePoint), v);

				final INode node = BaseNode.getNodeClosestToPointDeep(new Point3f(v), new ArrayList(rubiksCube.getMiddleBoxSections().keySet()));
				final Vector3f selectedSide = rubiksCube.getMiddleBoxSections().get(node);
				final Tuple<Vector3f, List<BoxSection>> sliceToRoll =  rubiksCube.setSliceToRoll(selectedSide, b);

				if (!sliceToRoll.getA().equals(Vector3f.ZERO)) {
					final Quaternion4f rotation = new Quaternion4f();
					rotation.set(sliceToRoll.getA(), 1);
					rotation.mul(0.5f);

					final BoxSection[] sliceBoxes = sliceToRoll.getB().toArray(new BoxSection[sliceToRoll.getB().size()]);
					final Group slice = new Group("slice", sliceBoxes);
					rubiksCube.removeNodes(sliceBoxes);
					rubiksCube.addNodes(slice);

					isRolling = true;
					registerController(new RubiksCubeSliceFlingAnimator(rubiksCube, slice, rotation));
					rubiksCube.roll(sliceToRoll.getA());
				}
			}
		}
	}

	private class RubiksCubeSliceFlingAnimator extends RotationAnimator implements IAnimatorListener {
		private final RubiksCube rubiksCube;

		public RubiksCubeSliceFlingAnimator(RubiksCube rubikscube, Group rubiksCubeSlice, Quaternion4f rotation) {
			super(rubiksCubeSlice, rotation, 0.5f, CubicOutEase.class);
			this.rubiksCube = rubikscube;
		}

		@Override public void onAnimatorCompleted() { /* Ignore this event */ }
		@Override public void onAnimatorStarted() { /* Ignore this event */ }

		@Override
		public void onAnimatorUnregistered() {
			final Group slice = (Group) animatable;
			rubiksCube.removeNodes(slice);
			slice.propagateModelMatrix();

			for (int i = 0; i < slice.getNodes().size(); i++) {
				rubiksCube.addNodes(slice.getNodes().get(i));
			}

			isRolling = false;
		}
	}
}
