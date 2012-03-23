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

import com.ejwa.dinja.engine.model.node.INode;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.model.node.mesh.geometry.Box;
import com.ejwa.dinja.engine.model.node.mesh.geometry.HeightMap;
import com.ejwa.dinja.engine.model.node.mesh.geometry.Plane;
import com.ejwa.dinja.engine.model.node.mesh.geometry.Sphere;
import com.ejwa.dinja.physics.collision.shape.BoxShape;
import com.ejwa.dinja.physics.collision.shape.CollisionShape;
import com.ejwa.dinja.physics.collision.shape.SphereShape;
import com.ejwa.dinja.physics.dynamics.RigidBody;
import com.ejwa.dinja.physics.dynamics.RigidBodyConstructionInfo;
import com.ejwa.dinja.physics.math.DefaultMotionState;
import com.ejwa.dinja.physics.math.MotionState;
import org.openmali.vecmath2.Vector3f;

public class PhysicsBody {
	private static final float DEFAULT_MASS = 1.0f;

	private final INode node;
	private float mass;
	private CollisionShape collisionShape;
	private final MotionState motionState = new DefaultMotionState();
	private final RigidBody rigidBody;

	public PhysicsBody(INode node) {
		this(node, DEFAULT_MASS);
	}

	public PhysicsBody(INode node, float mass) {
		this.node = node;
		this.mass = mass;

		if (node instanceof Box || node instanceof Plane) {
			collisionShape = new BoxShape(((Box) node).getDimensions());
		} else if (node instanceof HeightMap) {
			throw new UnsupportedOperationException("Physics heightmap shapes not yet supported.");
		} else if (node instanceof Sphere) {
			collisionShape = new SphereShape(((Sphere) node).getRadius());
		} else if (node instanceof Mesh) {
			throw new UnsupportedOperationException("Physics triangle mesh shapes not yet supported.");
		} else {
			throw new UnsupportedOperationException("Unsupported physics node type.");
		}

		rigidBody = new RigidBody(new RigidBodyConstructionInfo(mass, motionState, collisionShape, new Vector3f(0, 0, 0)));

		/*if (node.getRoot() != null) {
			PhysicsWorld.get(node.getRoot()).add(this);
		}*/
	}

	public INode getNode() {
		return node;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public MotionState getMotionState() {
		return motionState;
	}

	public RigidBody getRigidBody() {
		return rigidBody;
	}
}
