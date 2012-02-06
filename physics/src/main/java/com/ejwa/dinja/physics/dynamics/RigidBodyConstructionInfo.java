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
package com.ejwa.dinja.physics.dynamics;

import com.ejwa.dinja.physics.collision.shape.CollisionShape;
import com.ejwa.dinja.physics.library.BulletNative;
import com.ejwa.dinja.physics.math.MotionState;
import com.ejwa.dinja.physics.math.PhysicsVector3;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Vector3f;

@Platform(include = "BulletDynamics/Dynamics/btRigidBody.h", link = "bullet")
@Name("btRigidBody::btRigidBodyConstructionInfo")
public class RigidBodyConstructionInfo extends Pointer {
	static { Loader.load(BulletNative.class); }

	@Allocator private native void allocate(float mass, MotionState motionState, CollisionShape collisionShape,
	                                        @Const @ByRef PhysicsVector3 localInertia);

	public RigidBodyConstructionInfo(float mass, MotionState motionState, CollisionShape collisionShape, PhysicsVector3 localInertia) {
		super();
		allocate(mass, motionState, collisionShape, localInertia);
	}

	public RigidBodyConstructionInfo(float mass, MotionState motionState, CollisionShape collisionShape, Vector3f localInertia) {
		super();
		final PhysicsVector3 v = new PhysicsVector3();

		v.set(localInertia);
		allocate(mass, motionState, collisionShape, v);
	}
}
