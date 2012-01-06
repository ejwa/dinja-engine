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
package com.ejwa.dinja.physics.library;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Platform;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.UseSingleton", "PMD.AvoidUsingShortType"})
@Platform(include = {"BulletCollision/BroadphaseCollision/btBroadphaseInterface.h",
                     "BulletCollision/BroadphaseCollision/btDispatcher.h",
                     "BulletCollision/CollisionDispatch/btCollisionDispatcher.h",
                     "BulletCollision/CollisionDispatch/btDefaultCollisionConfiguration.h",
                     "BulletCollision/CollisionShapes/btBoxShape.h",
                     "BulletCollision/CollisionShapes/btConcaveShape.h",
                     "BulletCollision/CollisionShapes/btConvexInternalShape.h",
		     "BulletCollision/CollisionShapes/btConvexShape.h",
                     "BulletCollision/CollisionShapes/btCollisionShape.h",
                     "BulletCollision/CollisionShapes/btPolyhedralConvexShape.h",
                     "BulletCollision/CollisionShapes/btSphereShape.h",
                     "BulletCollision/CollisionShapes/btStaticPlaneShape.h",
                     "BulletDynamics/ConstraintSolver/btConstraintSolver.h",
                     "BulletDynamics/ConstraintSolver/btSequentialImpulseConstraintSolver.h",
                     "BulletDynamics/Dynamics/btDiscreteDynamicsWorld.h",
                     "BulletDynamics/Dynamics/btDynamicsWorld.h",
                     "BulletDynamics/Dynamics/btRigidBody.h",
                     "LinearMath/btDefaultMotionState.h", "LinearMath/btMotionState.h",
                     "LinearMath/btTransform.h", "LinearMath/btQuaternion.h"}, link = "bullet")
class BulletNative {
	static { Loader.load(); }

	protected BulletNative() {
		/* Only inheriting classes can make instances of this class. */
	}

	@Name("btBoxShape")
	public static class BoxShape extends PolyhedralConvexShape {
		static { Loader.load(); }
		@Allocator public native void allocate(@Const @ByRef BulletVector3 boxHalfExtents);
	}

	@Name("btBroadphaseInterface")
	public static class IBroadphase extends Pointer {
		static { Loader.load(); }
	}

	@Name("btCollisionDispatcher")
	public static class CollisionDispatcher extends Dispatcher {
		static { Loader.load(); }
		@Allocator public native void allocate(CollisionConfiguration collisionConfiguration);
	}

	@Name("btCollisionConfiguration")
	public static class CollisionConfiguration extends Pointer {
		static { Loader.load(); }
	}

	@Name("btCollisionShape")
	public static class CollisionShape extends Pointer {
		static { Loader.load(); }
		public native void calculateLocalInertia(float mass, @ByRef BulletVector3 inertia);
	}

	@Name("btConcaveShape")
	public static class ConcaveShape extends CollisionShape {
		static { Loader.load(); }
	}

	@Name("btConvexInternalShape")
	public static class ConvexInternalShape extends ConvexShape {
		static { Loader.load(); }
	}

	@Name("btConvexShape")
	public static class ConvexShape extends CollisionShape {
		static { Loader.load(); }
	}

	@Name("btConstraintSolver")
	public static class ConstraintSolver extends Pointer {
		static { Loader.load(); }
	}

	@Name("btDefaultCollisionConfiguration")
	public static class DefaultCollisionConfiguration extends CollisionConfiguration {
		static { Loader.load(); }
	}

	@Name("btDefaultMotionState")
	public static class DefaultMotionState extends MotionState {
		static { Loader.load(); }
		@Allocator public native void allocate(@Const @ByRef BulletTransform startTrans, @Const @ByRef BulletTransform centerOfMassOffset);
	}

	@Name("btDbvtBroadphase")
	public static class DbvtBroadphase extends IBroadphase {
		static { Loader.load(); }
	}

	@Name("btDispatcher")
	public static class Dispatcher extends Pointer {
		static { Loader.load(); }
	}

	@Name("btDynamicsWorld")
	public static class DynamicsWorld extends Pointer {
		static { Loader.load(); }
		public native void stepSimulation(float timeStep, int maxSubSteps, float fixedTimeStep);
		public native void setGravity(@Const @ByRef BulletVector3 gravity);
                public native @ByVal BulletVector3 getGravity();
		public native void synchronizeMotionStates();
		public native void addRigidBody(RigidBody body);
		public native void addRigidBody(RigidBody body, short group, short mask);
		public native void removeRigidBody(RigidBody body);
	}

	@Name("btDiscreteDynamicsWorld")
	public static class DiscreteDynamicsWorld extends DynamicsWorld {
		static { Loader.load(); }
		@Allocator public native void allocate(Dispatcher dispatcher, IBroadphase pairCache, ConstraintSolver constraintSolver, CollisionConfiguration collisionConfiguration);
	}

	@Name("btMotionState")
	public static class MotionState extends Pointer {
		static { Loader.load(); }
                public native void getWorldTransform(@ByRef BulletTransform worldTransform);
                public native void setWorldTransform(@ByRef @Const BulletTransform worldTransform);
	}

	@Name("btPolyhedralConvexShape")
	public static class PolyhedralConvexShape extends ConvexInternalShape {
		static { Loader.load(); }
	}

	@Name("btRigidBody")
	public static class RigidBody extends Pointer {
		static { Loader.load(); }
		@Allocator public native void allocate(@ByRef RigidBodyConstructionInfo constructionInfo);
		public native MotionState getMotionState();
		public native void setMotionState(MotionState motionState);
	}

	@Name("btRigidBody::btRigidBodyConstructionInfo")
	public static class RigidBodyConstructionInfo extends Pointer {
		static { Loader.load(); }
		@Allocator public native void allocate(float mass, MotionState motionState, CollisionShape collisionShape, @Const @ByRef BulletVector3 localInertia);
	}

	@Name("btSequentialImpulseConstraintSolver")
	public static class SequentialImpulseConstraintSolver extends ConstraintSolver {
		static { Loader.load(); }
	}

	@Name("btSphereShape")
	public static class SphereShape extends ConvexInternalShape {
		static { Loader.load(); }
		@Allocator public native void allocate(float radius);
	}

	@Name("btStaticPlaneShape")
	public static class StaticPlaneShape extends ConcaveShape {
		static { Loader.load(); }
		@Allocator public native void allocate(@Const @ByRef BulletVector3 planeNormal, float planeConstant);
	}

	@Name("btTransform")
	public static class BulletTransform extends Pointer {
		static { Loader.load(); }
		@Allocator public native void allocate(@Const @ByRef BulletQuaternion q, @Const @ByRef BulletVector3 v);
	}

	@Name("btVector3")
	public static class BulletVector3 extends Pointer {
		static { Loader.load(); }
		@Allocator public native void allocate(@Const @ByRef float x, @Const @ByRef float y, @Const @ByRef float z);
		@Allocator public native void allocate(@Const @ByRef BulletVector3 v);
		@Name("operator+=") public native @ByRef BulletVector3 add(@Const @ByRef BulletVector3 v);
		@Name("operator-=") public native @ByRef BulletVector3 sub(@Const @ByRef BulletVector3 v);
		@Name("operator*=") public native @ByRef BulletVector3 mul(@Const @ByRef float s);
		@Name("operator/=") public native @ByRef BulletVector3 div(@Const @ByRef float s);
		public native float dot(@Const @ByRef BulletVector3 v);
	}

	@Name("btQuaternion")
	public static class BulletQuaternion extends Pointer {
		static { Loader.load(); }
		@Allocator public native void allocate(@Const @ByRef float x, @Const @ByRef float y, @Const @ByRef float z, @Const @ByRef float w);
	}
}
