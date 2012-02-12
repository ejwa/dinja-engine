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
package com.ejwa.dinja.engine.model;

import android.opengl.Matrix;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Vector3f;

public class Camera {
	private static final float DEFAULT_ASPECT_RATIO = 16 / 9.6f;
	private static final float DEFAULT_FIELD_OF_VIEW = 45;
	private static final float DEFAULT_FAR_PLANE = 10;
	private static final float DEFAULT_NEAR_PLANE = 0.001f;
	private static final Vector3f DEFAULT_LOOK_DIRECTION = new Vector3f(0, 0, -1);
	private static final Vector3f DEFAULT_LOCATION = new Vector3f(0, 0, 5);
	private static final Vector3f DEFAULT_UP_DIRECTION = new Vector3f(0, 1, 0);

	private final Matrix4f projectionMatrix = new Matrix4f();
	private boolean recalculateProjectionMatrix = true;
	private final Matrix4f viewMatrix = new Matrix4f();
	private boolean recalculateViewMatrix = true;

	private float aspectRatio = DEFAULT_ASPECT_RATIO;
	private float fieldOfView = DEFAULT_FIELD_OF_VIEW;
	private float farPlane = DEFAULT_FAR_PLANE;
	private float nearPlane = DEFAULT_NEAR_PLANE;

	private Vector3f lookDirection = DEFAULT_LOOK_DIRECTION;
	private Vector3f location = DEFAULT_LOCATION;
	private Vector3f upDirection = DEFAULT_UP_DIRECTION;

	public Matrix4f getProjectionMatrix() {
		if (recalculateProjectionMatrix) {
			final float size = nearPlane * (float) Math.tan(Math.toRadians(fieldOfView) / 2.0);
			final float pv[] = new float[16];

			synchronized (this) {
				Matrix.frustumM(pv, 0, -size, size, -size / aspectRatio, size / aspectRatio, nearPlane, farPlane);
			}

			projectionMatrix.set(pv[0], pv[1], pv[2], pv[3], pv[4], pv[5], pv[6], pv[7], pv[8], pv[9],
			                     pv[10], pv[11], pv[12], pv[13], pv[14], pv[15]);
			projectionMatrix.transpose();
			recalculateProjectionMatrix = false;
		}

		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		if (recalculateViewMatrix) {
			final Vector3f f = Vector3f.fromPool();
			final Vector3f s = Vector3f.fromPool();
			final Vector3f u = Vector3f.fromPool();
			final Vector3f translation = Vector3f.fromPool();

			/* Calculate f */
			f.sub(lookDirection, location);
			f.normalize();

			/* Calculate s */
			s.cross(f, upDirection);
			s.normalize();

			/* Calculate u */
			u.cross(s, f);

			viewMatrix.setRow(0, s.getX(), s.getY(), s.getZ(), 0);
			viewMatrix.setRow(1, u.getX(), u.getY(), u.getZ(), 0);
			viewMatrix.setRow(2, -f.getX(), -f.getY(), -f.getZ(), 0);
			viewMatrix.setRow(3, 0, 0, 0, 1);
			translation.set(-location.getX(), -location.getY(), -location.getZ());
			viewMatrix.setTranslation(translation);
			recalculateViewMatrix = false;

			Vector3f.toPool(f);
			Vector3f.toPool(s);
			Vector3f.toPool(u);
			Vector3f.toPool(translation);
		}

		return viewMatrix;
	}

	public void setAspectRatio(float aspectRatio) {
		synchronized (this) {
			this.aspectRatio = aspectRatio;
		}

		recalculateProjectionMatrix = true;
	}

	public synchronized float getAspectRatio() {
		return aspectRatio;
	}

	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
		recalculateProjectionMatrix = true;
	}

	public float getFieldOfView() {
		return fieldOfView;
	}

	public float getFarPlane() {
		return farPlane;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		recalculateProjectionMatrix = true;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		recalculateProjectionMatrix = true;
	}

	public Vector3f getLookDirection() {
		return lookDirection;
	}

	public void setLookDirection(Vector3f lookDirection) {
		this.lookDirection = lookDirection;
		recalculateViewMatrix = true;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
		recalculateViewMatrix = true;
	}

	public Vector3f getUpDirection() {
		return upDirection;
	}

	public void setUpDirection(Vector3f upDirection) {
		this.upDirection = upDirection;
		recalculateViewMatrix = true;
	}
}
