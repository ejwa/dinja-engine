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

import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

public class Rotator {
	private final Matrix4f baseMatrix;
	private final Vector3f translation;
	private final Vector3f centerOfRotation = new Vector3f();

	private final Matrix3f rotationMatrix = new Matrix3f();
	private final Matrix4f offsetedRotationMatrix = new Matrix4f();
	private final Matrix3f currentRotation = new Matrix3f();
	private final Quaternion4f calculatedRotation = new Quaternion4f();

	public Rotator(Matrix4f baseMatrix, Vector3f translation) {
		rotationMatrix.setIdentity();
		this.baseMatrix = baseMatrix;
		this.translation = translation;
	}

	public void rotateX(float angle) {
		rotationMatrix.setIdentity();
		rotationMatrix.rotX(angle);
		rotate(rotationMatrix);
	}

	public void rotateY(float angle) {
		rotationMatrix.setIdentity();
		rotationMatrix.rotY(angle);
		rotate(rotationMatrix);
	}

	public void rotateZ(float angle) {
		rotationMatrix.setIdentity();
		rotationMatrix.rotZ(angle);
		rotate(rotationMatrix);
	}

	public void rotate(Matrix3f rotationMatrix) {
		baseMatrix.get(currentRotation);
		currentRotation.mul(rotationMatrix, currentRotation);
		baseMatrix.get(calculatedRotation);
		set(currentRotation);
	}

	public Quaternion4f get() {
		final Quaternion4f rotation = new Quaternion4f();
		baseMatrix.get(rotation);
		return rotation;
	}

	public void set(Quaternion4f rotation) {
		rotationMatrix.set(rotation);
		set(rotationMatrix);
	}

	private void calculateOffsetedRotation(Matrix3f rotation) {
		offsetedRotationMatrix.setIdentity();
		offsetedRotationMatrix.setTranslation(centerOfRotation);
		offsetedRotationMatrix.setRotation(rotation);

		final Matrix4f m = Matrix4f.fromPool();
		m.setIdentity();
		m.setTranslation(-centerOfRotation.getX(), -centerOfRotation.getY(), -centerOfRotation.getZ());
		offsetedRotationMatrix.mul(m);

		Matrix4f.toPool(m);
	}

	public void set(Matrix3f rotation) {
		if (centerOfRotation.getX() == 0 && centerOfRotation.getY() == 0 && centerOfRotation.getZ() == 0) {
			baseMatrix.setRotation(rotation);
		} else {
			calculateOffsetedRotation(rotation);

			final Vector3f rotationOffset = Vector3f.fromPool();
			offsetedRotationMatrix.get(rotationOffset);
			offsetedRotationMatrix.get(calculatedRotation);

			baseMatrix.setTranslation(rotationOffset.getX() + translation.getX(), rotationOffset.getY() + translation.getY(),
			                          rotationOffset.getZ() + translation.getZ());
			baseMatrix.setRotation(calculatedRotation);

			Vector3f.toPool(rotationOffset);
		}
	}

	public void setCenter(Vector3f vector) {
		setCenter(vector.getX(), vector.getY(), vector.getZ());
	}

	public void setCenter(float x, float y, float z) {
		centerOfRotation.set(x, y, z);
	}

	public void setCenter(Translatable translatable) {
		setCenter(translatable.getTranslator().get());
	}
}
