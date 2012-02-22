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
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Quaternion4f;
import org.openmali.vecmath2.Vector3f;

@Platform(include = "LinearMath/btMatrix3x3.h", link = "bullet")
@Name("btMatrix3x3")
public class PhysicsMatrix3 extends Pointer implements Poolable<PhysicsMatrix3> {
	static { Loader.load(BulletNative.class); }

	private static final ThreadLocal<Pool<PhysicsMatrix3>> POOL = new ThreadLocal<Pool<PhysicsMatrix3>>() {
		@Override
		protected Pool<PhysicsMatrix3> initialValue() {
			return new Pool(PhysicsMatrix3.class);
		}
	};

	@Allocator private native void allocate(@Const @ByRef float xx, @Const @ByRef float xy, @Const @ByRef float xz,
		                                @Const @ByRef float yx, @Const @ByRef float yy, @Const @ByRef float yz,
		                                @Const @ByRef float zx, @Const @ByRef float zy, @Const @ByRef float zz);
	public native @ByVal PhysicsVector3 getColumn(int i);
	public native @Const @ByRef PhysicsVector3 getRow(int i);
	public native void getRotation(@ByRef PhysicsQuaternion rotation);
	public native void setRotation(@Const @ByRef PhysicsQuaternion rotation);
	public native void setValue(@Const @ByRef float xx, @Const @ByRef float xy, @Const @ByRef float xz,
	                            @Const @ByRef float yx, @Const @ByRef float yy, @Const @ByRef float yz,
	                            @Const @ByRef float zx, @Const @ByRef float zy, @Const @ByRef float zz);

	public PhysicsMatrix3() {
		super();
		allocate(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public void getColumn(Vector3f column, int i) {
		final PhysicsVector3 pv = getColumn(i);
		column.set(pv.getX(), pv.getY(), pv.getZ());
	}

	public void getRow(Vector3f row, int i) {
		final PhysicsVector3 pv = getRow(i);
		row.set(pv.getX(), pv.getY(), pv.getZ());
	}

	public void getRotation(@ByRef Quaternion4f rotation) {
		final PhysicsQuaternion q = PhysicsQuaternion.fromPool();
		getRotation(q);
		rotation.set(q.getX(), q.getY(), q.getZ(), q.getW());
		PhysicsQuaternion.toPool(q);
	}

	public void setRotation(@ByRef Quaternion4f rotation) {
		final PhysicsQuaternion q = PhysicsQuaternion.fromPool();
		q.set(rotation);
		PhysicsQuaternion.toPool(q);
	}

	public Matrix3f getValue() {
		final Matrix3f m = new Matrix3f();
		getValue(m);
		return m;
	}

	public void getValue(Matrix3f matrix) {
		final PhysicsVector3 r0 = getRow(0);
		final PhysicsVector3 r1 = getRow(1);
		final PhysicsVector3 r2 = getRow(2);

		matrix.set(r0.getX(), r0.getY(), r0.getZ(),
		           r1.getX(), r1.getY(), r1.getZ(),
		           r2.getX(), r2.getY(), r2.getZ());
	}

	public void setValue(Matrix3f matrix) {
		setValue(matrix.get(0, 0), matrix.get(0, 1), matrix.get(0, 2),
		         matrix.get(1, 0), matrix.get(1, 1), matrix.get(1, 2),
		         matrix.get(2, 0), matrix.get(2, 1), matrix.get(2, 2));
	}

	public static PhysicsMatrix3 fromPool() {
		return POOL.get().allocateCleared();
	}

	public static PhysicsMatrix3 fromPool(Quaternion4f rotation) {
		final PhysicsMatrix3 matrix = POOL.get().allocateCleared();
		matrix.setRotation(rotation);
		return matrix;
	}

	public static PhysicsMatrix3 fromPool(Matrix3f matrix) {
		final PhysicsMatrix3 m = POOL.get().allocate();
		m.setValue(matrix);
		return m;
	}

	public static void toPool(PhysicsMatrix3 m) {
		POOL.get().free(m);
	}

	@Override
	public void clear() {
		setValue(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
}
