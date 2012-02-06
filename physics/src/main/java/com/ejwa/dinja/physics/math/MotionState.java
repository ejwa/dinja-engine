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
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Matrix4f;

@Platform(include = "LinearMath/btMotionState.h", link = "bullet")
@Name("btMotionState")
public class MotionState extends Pointer {
	static { Loader.load(BulletNative.class); }

	public native void getWorldTransform(@ByRef PhysicsTransform worldTransform);
	public native void setWorldTransform(@ByRef @Const PhysicsTransform worldTransform);

	public Matrix4f getWorldMatrix() {
		final PhysicsTransform worldTransform = PhysicsTransform.fromPool();
		return worldTransform.getTransformationMatrix();
	}

	public void setWorldMatrix(Matrix4f worldMatrix) {
		final PhysicsTransform worldTransform = PhysicsTransform.fromPool();
		worldTransform.setTransformationMatrix(worldMatrix);
		setWorldTransform(worldTransform);
	}
}
