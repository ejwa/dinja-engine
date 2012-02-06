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
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

@SuppressWarnings("PMD.ShortMethodName")
@Platform(include = "LinearMath/btQuaternion.h", link = "bullet")
@Name("btQuaternion")
public class PhysicsQuaternion extends Pointer implements Poolable<PhysicsQuaternion> {
	static { Loader.load(BulletNative.class); }

	private static final ThreadLocal<Pool<PhysicsQuaternion>> POOL = new ThreadLocal<Pool<PhysicsQuaternion>>() {
		@Override
		protected Pool<PhysicsQuaternion> initialValue() {
			return new Pool(PhysicsQuaternion.class);
		}
	};

	@Allocator private native void allocate(@Const @ByRef float x, @Const @ByRef float y, @Const @ByRef float z, @Const @ByRef float w);
	public native @Const @ByRef float getX();
	public native void setX(float x);
	public native @Const @ByRef float getY();
	public native void setY(float y);
	public native @Const @ByRef float getZ();
	public native void setZ(float z);
	private native @Const @ByRef float w();
	public float getW() { return w(); };
	public native void setW(float z);
	public native float getAngle();
	public native @ByVal PhysicsVector3 getAxis();

	public PhysicsQuaternion() {
		super();
		allocate(0, 0, 0, 0);
	}

	public void set(float x, float y, float z, float w) {
		setX(x); setY(y);
		setZ(z); setW(w);
	}

	public void set(Quaternion4f quaternion) {
		set(quaternion.getA(), quaternion.getB(), quaternion.getC(), quaternion.getD());
	}

	public void getAxis(Vector3f axis) {
		final PhysicsVector3 vector = getAxis();

		axis.setX(vector.getX());
		axis.setY(vector.getY());
		axis.setZ(vector.getZ());
	}

	public static PhysicsQuaternion fromPool() {
		return POOL.get().allocateCleared();
	}

	public static PhysicsQuaternion fromPool(float x, float y, float z, float w) {
		final PhysicsQuaternion quaternion = POOL.get().allocate();
		quaternion.set(x, y, z, w);
		return quaternion;
	}

	public static PhysicsQuaternion fromPool(Quaternion4f quaternion) {
		return fromPool(quaternion.getA(), quaternion.getB(), quaternion.getC(), quaternion.getD());
	}

	public static void toPool(PhysicsQuaternion q) {
		POOL.get().free(q);
	}

	@Override
	public void clear() {
		set(0, 0, 0, 0);
	}
}
