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

public class PhysicsMatrix3 implements Poolable<PhysicsMatrix3> {
	private final BulletNative.Matrix3 matrix = new BulletNative.Matrix3();

	private static final ThreadLocal<Pool<PhysicsMatrix3>> POOL = new ThreadLocal<Pool<PhysicsMatrix3>>() {
		@Override
		protected Pool<PhysicsMatrix3> initialValue() {
			return new Pool(PhysicsMatrix3.class);
		}
	};

	public PhysicsMatrix3(Quaternion4f rotation) {
		final BulletNative.Quaternion q = new BulletNative.Quaternion();

		q.allocate(rotation.getA(), rotation.getB(), rotation.getC(), rotation.getD());
		matrix.allocate(q);
	}

	public PhysicsMatrix3(Matrix3f matrix) {
		this.matrix.allocate(matrix.get(0, 0), matrix.get(0, 1), matrix.get(0, 2),
		                     matrix.get(1, 0), matrix.get(1, 1), matrix.get(1, 2),
		                     matrix.get(2, 0), matrix.get(2, 1), matrix.get(2, 2));
	}

	public void setValue(Matrix3f matrix) {
		this.matrix.setValue(matrix.get(0, 0), matrix.get(0, 1), matrix.get(0, 2),
		                     matrix.get(1, 0), matrix.get(1, 1), matrix.get(1, 2),
		                     matrix.get(2, 0), matrix.get(2, 1), matrix.get(2, 2));
	}

	public void setValue(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
		this.matrix.setValue(xx, xy, xz, yx, yy, yz, zx, zy, zz);
	}

	public static PhysicsMatrix3 fromPool() {
		return POOL.get().allocate();
	}

	public static void toPool(PhysicsMatrix3 m) {
		POOL.get().free(m);
	}

	@Override
	public void initialize() {
		setValue(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	@Override
	protected void finalize() throws Throwable {
		matrix.deallocate();
		super.finalize();
	}
}
