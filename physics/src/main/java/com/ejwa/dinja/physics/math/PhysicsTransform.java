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
package com.ejwa.dinja.physics.math;

import com.ejwa.dinja.physics.library.BulletNative;
import com.ejwa.dinja.physics.pool.Pool;
import com.ejwa.dinja.physics.pool.Poolable;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

@Platform(include = "LinearMath/btTransform.h", link = "bullet")
@Name("btTransform")
public class PhysicsTransform extends Pointer implements Poolable<PhysicsTransform> {
	static { Loader.load(BulletNative.class); }

	private final PhysicsMatrix3 basis = new PhysicsMatrix3();
	private final PhysicsVector3 origin = new PhysicsVector3();

	private static final ThreadLocal<Pool<PhysicsTransform>> POOL = new ThreadLocal<Pool<PhysicsTransform>>() {
		@Override
		protected Pool<PhysicsTransform> initialValue() {
			return new Pool(PhysicsTransform.class);
		}
	};

	@Allocator private native void allocate(@Const @ByRef PhysicsMatrix3 basis, @Const @ByRef PhysicsVector3 origin);
	public native @Const @ByRef PhysicsMatrix3 getBasis();
	public native void setBasis(@Const @ByRef PhysicsMatrix3 basis);
	public native @Const @ByRef PhysicsVector3 getOrigin();
	public native void setOrigin(@Const @ByRef PhysicsVector3 origin);
	public native @ByVal PhysicsQuaternion getRotation();
	public native void setRotation(@Const @ByRef PhysicsQuaternion rotation);

	public PhysicsTransform() {
		super();
		allocate(basis, origin);
	}

	public void getBasis(Matrix3f basis) {
		this.basis.getValue(basis);
	}

	public void setBasis(Matrix3f basis) {
		this.basis.setValue(basis);
		setBasis(this.basis);
	}

	public void getOrigin(Vector3f origin) {
		final PhysicsVector3 v = getOrigin();
		origin.set(v.getX(), v.getY(), v.getZ());
	}

	public void setOrigin(Vector3f origin) {
		this.origin.set(origin);
		setOrigin(this.origin);
	}

	public void getRotation(Quaternion4f rotation) {
		final PhysicsQuaternion q = getRotation();
		rotation.set(q.getX(), q.getY(), q.getZ(), q.getW());
	}

	public void setRotation(Quaternion4f rotation) {
		final PhysicsQuaternion q = getRotation();
		q.set(rotation);
		setRotation(q);
	}

	public Matrix4f getTransformationMatrix() {
		final Matrix4f transformationMatrix = new Matrix4f();
		getTransformationMatrix(transformationMatrix);
		return transformationMatrix;
	}

	public void getTransformationMatrix(Matrix4f transformationMatrix) {
		final Matrix3f m = Matrix3f.fromPool();
		final Vector3f v = Vector3f.fromPool();

		basis.getValue(m);
		origin.get(v);
		transformationMatrix.set(m);
		transformationMatrix.setTranslation(v);

		Matrix3f.toPool(m);
		Vector3f.toPool(v);
	}

	public void setTransformationMatrix(Matrix4f transformationMatrix) {
		final Matrix3f m = Matrix3f.fromPool();
		final Vector3f v = Vector3f.fromPool();

		transformationMatrix.get(m, v);
		basis.setValue(m);
		origin.set(v);
		setBasis(basis);
		setOrigin(origin);

		Matrix3f.toPool(m);
		Vector3f.toPool(v);
	}

	public static PhysicsTransform fromPool() {
		return POOL.get().allocateCleared();
	}

	public static PhysicsTransform fromPool(Matrix4f transformationMatrix) {
		final PhysicsTransform transform = POOL.get().allocate();
		transform.setTransformationMatrix(transformationMatrix);
		return transform;
	}

	public static PhysicsTransform fromPool(Quaternion4f basis, Vector3f origin) {
		final PhysicsTransform transform = POOL.get().allocate();

		transform.setRotation(basis);
		transform.setOrigin(origin);
		return transform;
	}

	public static PhysicsTransform fromPool(Matrix3f basis, Vector3f origin) {
		final PhysicsTransform transform = POOL.get().allocate();

		transform.setBasis(basis);
		transform.setOrigin(origin);
		return transform;
	}

	public static void toPool(PhysicsTransform t) {
		POOL.get().free(t);
	}

	@Override
	public void clear() {
		basis.clear();
		origin.clear();
		setBasis(basis);
		setOrigin(origin);
	}
}
