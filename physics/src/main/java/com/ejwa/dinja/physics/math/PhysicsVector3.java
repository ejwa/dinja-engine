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
import com.ejwa.dinja.utility.pool.Pool;
import com.ejwa.dinja.utility.pool.Poolable;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Vector3f;

@SuppressWarnings("PMD.TooManyMethods")
@Platform(include = "LinearMath/btVector3.h", link = "bullet")
@Name("btVector3")
public class PhysicsVector3 extends Pointer implements Poolable<PhysicsVector3> {
	static { Loader.load(BulletNative.class); }

	private static final ThreadLocal<Pool<PhysicsVector3>> POOL = new ThreadLocal<Pool<PhysicsVector3>>() {
		@Override
		protected Pool<PhysicsVector3> initialValue() {
			return new Pool(PhysicsVector3.class);
		}
	};

	@Allocator private native void allocate(@Const @ByRef float x, @Const @ByRef float y, @Const @ByRef float z);
	public native @Const @ByRef float getX();
	public native void setX(float x);
	public native @Const @ByRef float getY();
	public native void setY(float y);
	public native @Const @ByRef float getZ();
	public native void setZ(float z);
	@Name("operator+=") public native void add(@Const @ByRef PhysicsVector3 v);
	@Name("operator-=") public native void sub(@Const @ByRef PhysicsVector3 v);
	@Name("operator*=") public native void mul(@Const @ByRef float s);
	@Name("operator/=") public native void div(@Const @ByRef float s);
	public native float dot(@Const @ByRef PhysicsVector3 v);

	public PhysicsVector3() {
		super();
		allocate(0, 0, 0);
	}

	public Vector3f get() {
		final Vector3f v = new Vector3f();
		get(v);
		return v;
	}

	public void get(Vector3f vector) {
		vector.set(getX(), getY(), getZ());
	}

	public void set(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public void set(Vector3f v) {
		set(v.getX(), v.getY(), v.getZ());
	}

	public void add(Vector3f v) {
		final PhysicsVector3 pv = fromPool();
		pv.set(v);
		add(pv);
		toPool(pv);
	}

	public void sub(Vector3f v) {
		final PhysicsVector3 pv = fromPool();
		pv.set(v);
		sub(pv);
		toPool(pv);
	}

	public float dot(Vector3f v) {
		final PhysicsVector3 pv = fromPool();
		pv.set(v);
		final float result = dot(pv);

		toPool(pv);
		return result;
	}

	public static PhysicsVector3 fromPool() {
		return POOL.get().allocateCleared();
	}

	public static PhysicsVector3 fromPool(float x, float y, float z) {
		final PhysicsVector3 v = POOL.get().allocate();
		v.set(x, y, z);
		return v;
	}

	public static PhysicsVector3 fromPool(Vector3f v) {
		final PhysicsVector3 pv = POOL.get().allocate();
		pv.set(v);
		return pv;
	}

	public static void toPool(PhysicsVector3 v) {
		POOL.get().free(v);
	}

	@Override
	public void clear() {
		set(0, 0, 0);
	}
}
