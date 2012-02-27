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
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Matrix4f;

@Platform(include = "LinearMath/btDefaultMotionState.h", link = "bullet")
@Name("btDefaultMotionState")
public class DefaultMotionState extends MotionState {
	static { Loader.load(BulletNative.class); }

	@Allocator private native void allocate();
	@Allocator private native void allocate(@Const @ByRef PhysicsTransform startTransform);
	@Allocator private native void allocate(@Const @ByRef PhysicsTransform startTransform,
	                                        @Const @ByRef PhysicsTransform centerOfMassOffset);

	public DefaultMotionState() {
		super();
		allocate();
	}

	public DefaultMotionState(PhysicsTransform startTransform) {
		super();
		allocate(startTransform);
	}

	public DefaultMotionState(Matrix4f start) {
		super();
		final PhysicsTransform startTransform = new PhysicsTransform();

		startTransform.setTransformationMatrix(start);
		allocate(startTransform);
	}

	public DefaultMotionState(PhysicsTransform startTransform, PhysicsTransform centerOfMassOffset) {
		super();
		allocate(startTransform, centerOfMassOffset);
	}

	public DefaultMotionState(Matrix4f start, Matrix4f centerOfMassOffset) {
		super();
		final PhysicsTransform startTransform = new PhysicsTransform();
		final PhysicsTransform centerOfMassOffsetTransform = new PhysicsTransform();

		startTransform.setTransformationMatrix(start);
		centerOfMassOffsetTransform.setTransformationMatrix(centerOfMassOffset);
		allocate(startTransform, centerOfMassOffsetTransform);
	}
}
