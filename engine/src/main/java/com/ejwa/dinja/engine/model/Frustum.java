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
import org.openmali.FastMath;
import org.openmali.vecmath2.Matrix4f;

public class Frustum {
	private static final float DEFAULT_ASPECT_RATIO = 16 / 9.6f;
	private static final float DEFAULT_FIELD_OF_VIEW = 45;
	private static final float DEFAULT_FAR_PLANE = 10;
	private static final float DEFAULT_NEAR_PLANE = 0.001f;

	private float aspectRatio = DEFAULT_ASPECT_RATIO;
	private float fieldOfView = DEFAULT_FIELD_OF_VIEW;
	private float farPlane = DEFAULT_FAR_PLANE;
	private float nearPlane = DEFAULT_NEAR_PLANE;

	private final Matrix4f projectionMatrix = new Matrix4f();
	private boolean recalculateProjectionMatrix = true;

	public Frustum() {
		/* Doing nothing here currently. */
	}

	public Frustum(float aspectRatio, float fieldOfview, float farPlane, float nearPlane) {
		this.aspectRatio = aspectRatio;
		this.fieldOfView = fieldOfview;
		this.farPlane = farPlane;
		this.nearPlane = nearPlane;
	}

	public Matrix4f getProjectionMatrix() {
		if (recalculateProjectionMatrix) {
			final float size = nearPlane * FastMath.tan(FastMath.toRad(fieldOfView) / 2.0f);
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

	public boolean isRecalculateProjectionMatrix() {
		return recalculateProjectionMatrix;
	}

	public synchronized float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		synchronized (this) {
			this.aspectRatio = aspectRatio;
		}

		recalculateProjectionMatrix = true;
	}

	public float getFieldOfView() {
		return fieldOfView;
	}

	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
		recalculateProjectionMatrix = true;
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
}
