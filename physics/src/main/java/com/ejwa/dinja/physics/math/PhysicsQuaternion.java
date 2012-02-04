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
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

public class PhysicsQuaternion implements Poolable<PhysicsQuaternion> {
	private final BulletNative.Quaternion quaternion = new BulletNative.Quaternion();

	private static final ThreadLocal<Pool<PhysicsQuaternion>> POOL = new ThreadLocal<Pool<PhysicsQuaternion>>() {
		@Override
		protected Pool<PhysicsQuaternion> initialValue() {
			return new Pool(PhysicsQuaternion.class);
		}
	};

	public PhysicsQuaternion(float x, float y, float z, float w) {
		quaternion.allocate(x, y, z, w);
	}

	public PhysicsQuaternion(Quaternion4f q) {
		this(q.getA(), q.getB(), q.getC(), q.getD());
	}

	public float getAngle() {
		return quaternion.getAngle();
	}

	public Vector3f getAxis() {
		final Vector3f result = new Vector3f();
		getAxis(result);

		return result;
	}

	public void getAxis(Vector3f axis) {
		final BulletNative.Vector3 vector = quaternion.getAxis();

		axis.setX(vector.getX());
		axis.setY(vector.getY());
		axis.setZ(vector.getZ());
		vector.deallocate(); /* Passed by value */
	}

	public float getX() {
		return quaternion.getX();
	}

	public float getY() {
		return quaternion.getY();
	}

	public float getZ() {
		return quaternion.getZ();
	}

	public float getW() {
		return quaternion.w();
	}

	public void setX(float x) {
		quaternion.setX(x);
	}

	public void setY(float y) {
		quaternion.setY(y);
	}

	public void setZ(float z) {
		quaternion.setZ(z);
	}

	public void setW(float w) {
		quaternion.setW(w);
	}

	public void set(float x, float y, float z, float w) {
		setX(x); setY(y);
		setZ(z); setW(w);
	}

	public static PhysicsQuaternion fromPool() {
		return POOL.get().allocate();
	}

	public static void toPool(PhysicsQuaternion q) {
		POOL.get().free(q);
	}

	@Override
	public void initialize() {
		set(0, 0, 0, 0);
	}

	@Override
	protected void finalize() throws Throwable {
		quaternion.deallocate();
		super.finalize();
	}
}
