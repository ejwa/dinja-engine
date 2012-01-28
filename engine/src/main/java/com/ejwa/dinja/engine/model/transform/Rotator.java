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
package com.ejwa.dinja.engine.model.transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

public class Rotator {
	private final Matrix4f baseMatrix;
	private final Matrix4f rotationMatrix = new Matrix4f();

	public Rotator(Matrix4f baseMatrix) {
		this.baseMatrix = baseMatrix;
	}

	public void rotateX(float angle) {
		rotationMatrix.setZero();
		rotationMatrix.rotX(angle);
		baseMatrix.mul(rotationMatrix, baseMatrix);
	}

	public void rotateY(float angle) {
		rotationMatrix.setZero();
		rotationMatrix.rotY(angle);
		baseMatrix.mul(rotationMatrix, baseMatrix);
	}

	public void rotateZ(float angle) {
		rotationMatrix.setZero();
		rotationMatrix.rotZ(angle);
		baseMatrix.mul(rotationMatrix, baseMatrix);
	}

	public Quat4f get() {
		final Quat4f rotation = new Quat4f();
		baseMatrix.get(rotation);
		return rotation;
	}

	public void set(Quat4f rotation) {
		baseMatrix.set(rotation);
	}
}
