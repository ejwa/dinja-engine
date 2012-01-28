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
import javax.vecmath.Vector3f;

public class Translator {
	private final Matrix4f baseMatrix;
	private final Matrix4f translationMatrix = new Matrix4f();

	public Translator(Matrix4f baseMatrix) {
		this.baseMatrix = baseMatrix;
	}

	public void moveX(float distance) {
		move(distance, 0, 0);
	}

	public void moveY(float distance) {
		move(0, distance, 0);
	}

	public void moveZ(float distance) {
		move(0, 0, distance);
	}

	public void move(float x, float y, float z) {
		move(new Vector3f(x, y, z));
	}

	public void move(Vector3f distance) {
		translationMatrix.setZero();
		translationMatrix.setTranslation(distance);
		baseMatrix.mul(translationMatrix, baseMatrix);
	}

	public Vector3f get() {
		final Vector3f rotation = new Vector3f();
		baseMatrix.get(rotation);
		return rotation;
	}

	public void set(Vector3f position) {
		baseMatrix.set(position);
	}
}
