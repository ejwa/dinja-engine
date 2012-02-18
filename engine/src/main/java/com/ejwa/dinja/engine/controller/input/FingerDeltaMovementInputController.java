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
import android.view.MotionEvent;
import org.openmali.vecmath2.Point2f;

public class FingerDeltaMovementInputController {
	private final IFingerDeltaMovementInputListener fingerDeltaMovementInputListener;
	private final GLSurfaceView glSurfaceView;
	private final Point2f previousPosition = new Point2f();

	public FingerDeltaMovementInputController(IFingerDeltaMovementInputListener fingerDeltaMovementInputListener, GLSurfaceView glSurfaceView) {
		this.fingerDeltaMovementInputListener = fingerDeltaMovementInputListener;
		this.glSurfaceView = glSurfaceView;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void onTouchEvent(MotionEvent motionEvent) {
		final float x = motionEvent.getX() / glSurfaceView.getWidth();
		final float y = motionEvent.getY() / glSurfaceView.getHeight();

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				previousPosition.set(x, y);
				break;
			case MotionEvent.ACTION_UP:
				fingerDeltaMovementInputListener.onFingerDeltaMovementEndInput();
				break;
			default: /* MotionEvent.ACTION_MOVE */
				fingerDeltaMovementInputListener.onFingerDeltaMovementInput(x - previousPosition.getX(),
				                                                            y - previousPosition.getY());
				previousPosition.set(x, y);
		}
	}
}
