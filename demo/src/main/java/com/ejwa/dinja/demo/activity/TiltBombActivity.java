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
import com.ejwa.dinja.engine.activity.DinjaActivity;
import com.ejwa.dinja.engine.controller.Controllable;
import com.ejwa.dinja.engine.controller.input.ITiltForceInputListener;
import com.ejwa.dinja.engine.model.Camera;
import com.ejwa.dinja.engine.model.node.scene.Scene;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.file.X3DFile;
import com.ejwa.dinja.engine.view.SceneView;
import com.ejwa.dinja.opengles.display.IFrameUpdateListener;

public class TiltBombActivity extends DinjaActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final X3DFile x3dFile = new X3DFile(getAssets(), "dinja_bomb.x3d");
		final Mesh m = x3dFile.getMesh("group_ME_Sphere");

		registerView(new SceneView(new Scene(new Camera(), m)));
		registerController(new BombMeshController(m));
	}

	private class BombMeshController implements Controllable, ITiltForceInputListener, IFrameUpdateListener {
		private final Mesh mesh;
		private float xRotation;
		private float yRotation;

		public BombMeshController(Mesh mesh) {
			this.mesh = mesh;
		}

		@Override
		public void onTiltForceInput(float xForce, float yForce, float zForce) {
			xRotation = xForce / 2000;
			yRotation = yForce / 2000;
		}

		@Override
		public void onFrameUpdate(long milliSecondsSinceLastFrame) {
			mesh.getRotator().rotateX(xRotation * milliSecondsSinceLastFrame);
			mesh.getRotator().rotateY(yRotation * milliSecondsSinceLastFrame);
		}
	}
}
