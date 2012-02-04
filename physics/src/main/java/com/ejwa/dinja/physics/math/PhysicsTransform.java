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
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

public class PhysicsTransform implements Poolable<PhysicsTransform> {
	private final BulletNative.Transform transform = new BulletNative.Transform();

	private static final ThreadLocal<Pool<PhysicsTransform>> POOL = new ThreadLocal<Pool<PhysicsTransform>>() {
		@Override
		protected Pool<PhysicsTransform> initialValue() {
			return new Pool(PhysicsTransform.class);
		}
	};

	public PhysicsTransform(Quaternion4f basis, Vector3f origin) {
		final BulletNative.Quaternion q = new BulletNative.Quaternion();
		final BulletNative.Vector3 v = new BulletNative.Vector3();

		q.allocate(basis.getA(), basis.getB(), basis.getC(), basis.getD());
		v.allocate(origin.getX(), origin.getY(), origin.getZ());
		transform.allocate(q, v);
	}

	public PhysicsTransform(Matrix3f basis, Vector3f origin) {
		final BulletNative.Matrix3 m = new BulletNative.Matrix3();
		final BulletNative.Vector3 v = new BulletNative.Vector3();

		m.allocate(basis.get(0, 0), basis.get(0, 1), basis.get(0, 2),
		           basis.get(1, 0), basis.get(1, 1), basis.get(1, 2),
		           basis.get(2, 0), basis.get(2, 1), basis.get(2, 2));
		v.allocate(origin.getX(), origin.getY(), origin.getZ());
		transform.allocate(m, v);
	}

	public Matrix3f getBasis() {
		final Matrix3f result = new Matrix3f();
		getBasis(result);

		return result;
	}

	public void getBasis(Matrix3f basis) {
		final BulletNative.Matrix3 matrix = transform.getBasis();
		final BulletNative.Vector3 r0 = matrix.getRow(0);
		final BulletNative.Vector3 r1 = matrix.getRow(1);
		final BulletNative.Vector3 r2 = matrix.getRow(2);

		basis.set(r0.getX(), r0.getY(), r0.getZ(),
		          r1.getX(), r1.getY(), r1.getZ(),
		          r2.getX(), r2.getY(), r2.getZ());

	}

	public void setBasis(Matrix3f basis) {
		final BulletNative.Matrix3 matrix = transform.getBasis();

		matrix.setValue(basis.get(0, 0), basis.get(0, 1), basis.get(0, 2),
		                basis.get(1, 0), basis.get(1, 1), basis.get(1, 2),
		                basis.get(2, 0), basis.get(2, 1), basis.get(2, 2));
		transform.setBasis(matrix);
	}

	public Vector3f getOrigin() {
		final Vector3f result = new Vector3f();
		getOrigin(result);

		return result;
	}

	public void setOrigin(Vector3f origin) {
		final BulletNative.Vector3 vector = transform.getOrigin();

		vector.setX(origin.getX());
		vector.setY(origin.getY());
		vector.setZ(origin.getZ());
		transform.setOrigin(vector);
	}

	public void getOrigin(Vector3f origin) {
		final BulletNative.Vector3 vector = transform.getOrigin();
		origin.set(vector.getX(), vector.getY(), vector.getZ());
	}


	public Quaternion4f getRotation() {
		final Quaternion4f result = new Quaternion4f();
		getRotation(result);

		return result;
	}

	public void getRotation(Quaternion4f rotation) {
		final BulletNative.Quaternion quaternion = transform.getRotation();

		rotation.set(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.w());
		quaternion.deallocate(); /* Passed by value */
	}


	public static PhysicsTransform fromPool() {
		return POOL.get().allocate();
	}

	public static void toPool(PhysicsTransform t) {
		POOL.get().free(t);
	}

	@Override
	public void initialize() {
		final Matrix3f basis = Matrix3f.fromPool();
		final Vector3f origin = Vector3f.fromPool();

		setBasis(basis);
		setOrigin(origin);
	}

	@Override
	protected void finalize() throws Throwable {
		transform.deallocate();
		super.finalize();
	}
}
