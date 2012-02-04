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
import org.openmali.vecmath2.Vector3f;

public class PhysicsVector3 implements Poolable<PhysicsVector3> {
	private final BulletNative.Vector3 vector = new BulletNative.Vector3();

	private static final ThreadLocal<Pool<PhysicsVector3>> POOL = new ThreadLocal<Pool<PhysicsVector3>>() {
		@Override
		protected Pool<PhysicsVector3> initialValue() {
			return new Pool(PhysicsVector3.class);
		}
	};

	public PhysicsVector3(float x, float y, float z) {
		vector.allocate(x, y, z);
	}

	public PhysicsVector3(Vector3f v) {
		this(v.getX(), v.getY(), v.getZ());
	}

	public float getX() {
		return vector.getX();
	}

	public float getY() {
		return vector.getY();
	}

	public float getZ() {
		return vector.getZ();
	}

	public void setX(float x) {
		vector.setX(x);
	}

	public void setY(float y) {
		vector.setY(y);
	}

	public void setZ(float z) {
		vector.setZ(z);
	}

	public void set(float x, float y, float z) {
		vector.setX(x);
		vector.setY(y);
		vector.setZ(z);
	}

	public void add(Vector3f v) {
		final PhysicsVector3 pv = fromPool();
		vector.add(pv.vector);
		toPool(pv);
	}

	public void sub(Vector3f v) {
		final PhysicsVector3 pv = fromPool();
		vector.sub(pv.vector);
		toPool(pv);
	}

	public void mul(float s) {
		vector.mul(s);
	}

	public void div(float s) {
		vector.div(s);
	}

	public float dot(Vector3f v) {
		final PhysicsVector3 pv = fromPool();
		final float result = vector.dot(pv.vector);

		toPool(pv);
		return result;
	}

	public static PhysicsVector3 fromPool() {
		return POOL.get().allocate();
	}

	public static void toPool(PhysicsVector3 v) {
		POOL.get().free(v);
	}

	@Override
	public void initialize() {
		set(0, 0, 0);
	}

	@Override
	protected void finalize() throws Throwable {
		vector.deallocate();
		super.finalize();
	}
}
