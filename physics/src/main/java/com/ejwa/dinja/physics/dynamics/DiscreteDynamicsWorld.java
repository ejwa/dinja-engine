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

import com.ejwa.dinja.physics.collision.CollisionConfiguration;
import com.ejwa.dinja.physics.collision.ICollisionConfiguration;
import com.ejwa.dinja.physics.collision.broadphase.Broadphase;
import com.ejwa.dinja.physics.collision.broadphase.IBroadphase;
import com.ejwa.dinja.physics.collision.dispatch.Dispatcher;
import com.ejwa.dinja.physics.collision.dispatch.IDispatcher;
import com.ejwa.dinja.physics.dynamics.solver.ConstraintSolver;
import com.ejwa.dinja.physics.dynamics.solver.IConstraintSolver;
import com.ejwa.dinja.physics.library.BulletNative;
import com.ejwa.dinja.physics.math.PhysicsVector3;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;
import org.openmali.vecmath2.Vector3f;

@SuppressWarnings("PMD.AvoidUsingShortType")
@Platform(include = "BulletDynamics/Dynamics/btDiscreteDynamicsWorld.h", link = "bullet")
@Name("btDiscreteDynamicsWorld")
public class DiscreteDynamicsWorld extends Pointer implements IDynamicsWorld {
	static { Loader.load(BulletNative.class); }

	@Allocator private native void allocate(Dispatcher dispatcher, Broadphase broadphase, ConstraintSolver constraintSolver,
	                                        CollisionConfiguration collisionConfiguration);

	public DiscreteDynamicsWorld(IDispatcher dispatcher, IBroadphase broadphase, IConstraintSolver solver,
	                             ICollisionConfiguration collisionConfiguration) {
		super();
		allocate((Dispatcher) dispatcher, (Broadphase) broadphase, (ConstraintSolver) solver,
		         (CollisionConfiguration) collisionConfiguration);
	}

	@Override public native void addRigidBody(RigidBody body);
	@Override public native void addRigidBody(RigidBody body, short group, short mask);
	@Override public native void removeRigidBody(RigidBody body);
	public native @ByVal PhysicsVector3 getGravity();
	public native void setGravity(@Const @ByRef PhysicsVector3 gravity);
	@Override public native void synchronizeMotionStates();

	@Override
	public void getGravity(Vector3f gravity) {
		final PhysicsVector3 v = getGravity();
		gravity.set(v.getX(), v.getY(), v.getZ());
	}

	@Override
	public void setGravity(Vector3f gravity) {
		setGravity(gravity.getX(), gravity.getY(), gravity.getZ());
	}

	@Override
	public void setGravity(float x, float y, float z) {
		final PhysicsVector3 gravity = PhysicsVector3.fromPool(x, y, z);
		setGravity(gravity);
		PhysicsVector3.toPool(gravity);
	}

	@Override
	public native void stepSimulation(float timeStep, int maxSubSteps, float fixedTimeStep);

	@Override
	public void stepSimulation(float timeStep) {
		stepSimulation(timeStep, 1, 1/60f);
	}
}
