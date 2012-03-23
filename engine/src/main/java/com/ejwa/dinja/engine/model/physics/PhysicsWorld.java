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
package com.ejwa.dinja.engine.model.physics;

import com.ejwa.dinja.engine.model.node.IRootNode;
import com.ejwa.dinja.physics.collision.DefaultCollisionConfiguration;
import com.ejwa.dinja.physics.collision.ICollisionConfiguration;
import com.ejwa.dinja.physics.collision.broadphase.DbvtBroadphase;
import com.ejwa.dinja.physics.collision.broadphase.IBroadphase;
import com.ejwa.dinja.physics.collision.dispatch.CollisionDispatcher;
import com.ejwa.dinja.physics.dynamics.DiscreteDynamicsWorld;
import com.ejwa.dinja.physics.dynamics.IDynamicsWorld;
import com.ejwa.dinja.physics.dynamics.solver.IConstraintSolver;
import com.ejwa.dinja.physics.dynamics.solver.SequentialImpulseConstraintSolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmali.vecmath2.Vector3f;

public class PhysicsWorld {
	private static final Vector3f DEFAULT_GRAVITY = new Vector3f(0, -10, 0);
	private static final Map<IRootNode, PhysicsWorld> PHYSICS_WORLDS = new HashMap<IRootNode, PhysicsWorld>();
	private static final List<PhysicsWorld> PHYSICS_WORLDS_LIST = new ArrayList<PhysicsWorld>();

	private final IBroadphase broadphase = new DbvtBroadphase();
	private final ICollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
	private final CollisionDispatcher dispatcher = new CollisionDispatcher(new DefaultCollisionConfiguration());
	private final IConstraintSolver solver = new SequentialImpulseConstraintSolver();
	private final IDynamicsWorld dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);

	private final IRootNode rootNode;
	private final List<PhysicsBody> physicsBodies = new ArrayList<PhysicsBody>();

	public PhysicsWorld(IRootNode rootNode) {
		this.rootNode = rootNode;
	}

	public IRootNode getRootNode() {
		return rootNode;
	}

	public void step(float seconds) {
		dynamicsWorld.stepSimulation(seconds, 4); /* Allow a fps/4 slow down for the simulation */
	}

	public List<PhysicsBody> getPhysicsBodies() {
		return physicsBodies;
	}

	public static List<PhysicsWorld> get() {
		return PHYSICS_WORLDS_LIST;
	}

	public static PhysicsWorld get(IRootNode rootNode) {
		return PHYSICS_WORLDS.get(rootNode);
	}

	/*public static void add(PhysicsBody physicsBody) {
		PhysicsWorld physicsWorld = PHYSICS_WORLDS.get(rootNode);

		if (physicsWorld == null) {
			physicsWorld = new PhysicsWorld(rootNode);
			physicsWorld.dynamicsWorld.setGravity(DEFAULT_GRAVITY);

			PHYSICS_WORLDS.put(rootNode, physicsWorld);
			PHYSICS_WORLDS_LIST.add(physicsWorld);
		}

		physicsWorld.physicsBodies.add(physicsBody);
		physicsWorld.dynamicsWorld.addRigidBody(physicsBody.getRigidBody());
	}

	public static void remove(PhysicsBody physicsBody) {
		final PhysicsWorld physicsWorld = PHYSICS_WORLDS.get(rootNode);

		if (physicsWorld != null) {
			physicsWorld.physicsBodies.remove(physicsBody);
			physicsWorld.dynamicsWorld.removeRigidBody(physicsBody.getRigidBody());

			if (physicsWorld.physicsBodies.isEmpty()) {
				PHYSICS_WORLDS_LIST.remove(PHYSICS_WORLDS.remove(rootNode));
			}
		}
	}*/
}
