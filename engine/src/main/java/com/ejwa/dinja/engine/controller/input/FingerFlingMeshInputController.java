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
package com.ejwa.dinja.engine.controller.input;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.model.node.mesh.MeshPrimitiveData;
import com.ejwa.dinja.opengles.display.draw.ISelectionDrawListener;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.utility.type.Tuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.openmali.vecmath2.Point2f;
import org.openmali.vecmath2.Point2i;
import org.openmali.vecmath2.Vector2f;

public class FingerFlingMeshInputController extends SimpleOnGestureListener implements ISelectionDrawListener {
	private final IFingerFlingMeshInputListener fingerFlingMeshInputListener;
	private final GestureDetector gestureDetector;
	private final GLSurfaceView glSurfaceView;
	private final float minimumFlingVelocity;
	private final AtomicBoolean flinging = new AtomicBoolean(false);
	private final List<Tuple<Point2f, Mesh>> selectedMeshes = Collections.synchronizedList(new ArrayList<Tuple<Point2f, Mesh>>());

	private final Point2i fingerPosition = new Point2i();
	private final Vector2f fingerDeltaMovement = new Vector2f();

	private float distanceMoved;
	private long flingStartTime;

	public FingerFlingMeshInputController(IFingerFlingMeshInputListener fingerFlingMeshInputListener,
	                                      GLSurfaceView glSurfaceView, float minimumFlingVelocity) {
		super();
		gestureDetector = new GestureDetector(this);
		this.fingerFlingMeshInputListener = fingerFlingMeshInputListener;
		this.glSurfaceView = glSurfaceView;
		this.minimumFlingVelocity = minimumFlingVelocity;
	}

	public FingerFlingMeshInputController(IFingerFlingMeshInputListener fingerFlingMeshInputListener, GLSurfaceView glSurfaceView) {
		this(fingerFlingMeshInputListener, glSurfaceView, 0.65f);
	}

	public boolean isFlinging() {
		return flinging.get();
	}

	public void onTouchEvent(MotionEvent motionEvent) {
		gestureDetector.onTouchEvent(motionEvent);

		synchronized (fingerPosition) {
			fingerPosition.set((int) motionEvent.getX(), (int) motionEvent.getY());
		}

		if (distanceMoved < 0) {
			distanceMoved++;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		distanceMoved = -5; // Wait -distanceMoved touch events before executing onScroll().
		flingStartTime = SystemClock.uptimeMillis();
		selectedMeshes.clear();
		flinging.set(true);
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (flinging.get() && !selectedMeshes.isEmpty()) {
			final List<Tuple<Point2f, Mesh>> selectedMeshesCopy;

			synchronized (selectedMeshes) {
				selectedMeshesCopy = new ArrayList<Tuple<Point2f, Mesh>>(selectedMeshes);
			}

			fingerFlingMeshInputListener.onFingerFlingMeshInput(selectedMeshesCopy);
		}

		flinging.set(false);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		flinging.set(false);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (distanceMoved >= 0) {
			fingerDeltaMovement.set(distanceX / glSurfaceView.getWidth(), distanceY / glSurfaceView.getHeight());
			distanceMoved += Math.abs(fingerDeltaMovement.length());

			if (minimumFlingVelocity > distanceMoved / ((SystemClock.uptimeMillis() - flingStartTime) / 1000f)) {
				flinging.set(false);
			}
		}

		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		flinging.set(false);
		return true;
	}

	@Override
	public void onRefreshSelectedPoint(Point2i selectedPoint) {
		synchronized (fingerPosition) {
			selectedPoint.set(fingerPosition);
		}
	}

	@Override
	public void onSelectedPrimitiveData(PrimitiveData primitiveData) {
		if (flinging.get() && primitiveData instanceof MeshPrimitiveData) {
			final Point2f p = new Point2f((float) fingerPosition.getX() / glSurfaceView.getWidth(),
			                              (float) fingerPosition.getY() / glSurfaceView.getHeight());
			selectedMeshes.add(new Tuple<Point2f, Mesh>(p, ((MeshPrimitiveData) primitiveData).getParentMesh()));
		}
	}
}
