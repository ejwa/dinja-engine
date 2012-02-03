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

import android.view.MotionEvent;
import com.ejwa.dinja.opengles.display.GLSurface;
import org.openmali.FastMath;
import org.openmali.vecmath2.Point2f;

public class FingerMovementInputController {
	private final IFingerMovementInputListener fingerMovementInputListener;
	private final GLSurface surface;
	private final Point2f startPosition = new Point2f();
	private final Point2f endPosition = new Point2f();
	private float angle;

	public FingerMovementInputController(IFingerMovementInputListener fingerMovementInputListener, GLSurface surface) {
		this.fingerMovementInputListener = fingerMovementInputListener;
		this.surface = surface;
	}

	public void onTouchEvent(MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			final float x = motionEvent.getX() / surface.getWidth();
			final float y = motionEvent.getY() / surface.getHeight();

			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				startPosition.set(x, y);
				endPosition.set(x, y);
				angle = 0;
			} else {
				endPosition.set(x, y);
				angle = FastMath.atan2(endPosition.getY() - startPosition.getY(), endPosition.getX() - startPosition.getX());
			}

			fingerMovementInputListener.onFingerMovementInput(startPosition, endPosition, angle);
		} else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			fingerMovementInputListener.onFingerMovementEndInput();
		}
	}
}
