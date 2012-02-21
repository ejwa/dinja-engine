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
package com.ejwa.dinja.engine.model.node;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;

public class BaseNode implements INode {
	protected final Matrix4f modelMatrix = new Matrix4f();
	protected final Matrix4f worldMatrix = new Matrix4f();
	protected final String name;
	protected final List<INode> nodes = new LinkedList<INode>();

	protected BaseNode(String name) {
		modelMatrix.setIdentity();
		this.name = name;
	}

	@Override
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	@Override
	public Matrix4f getWorldMatrix() {
		return worldMatrix;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public final void addNodes(INode ...nodes) {
		this.nodes.addAll(Arrays.asList(nodes));
	}

	@Override
	public void removeNodes(INode ...nodes) {
		this.nodes.removeAll(Arrays.asList(nodes));
	}

	@Override
	public void removeNodes(String ...nodeNames) {
		for (String nodeName : nodeNames) {
			for (INode n : nodes) {
				if (n.getName().equals(nodeName)) {
					nodes.remove(n);
				}
			}
		}
	}

	@Override
	public List<INode> getNodes() {
		return nodes;
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public INode getNodeClosestToPoint(Point3f point) {
		if (!getNodes().isEmpty()) {
			return BaseNode.getNodeClosestToPoint(point, getNodes());
		}

		return this;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static INode getNodeClosestToPoint(Point3f point, List<INode> nodes) {
		if (nodes.isEmpty()) {
			throw new IllegalArgumentException("Nodes array can't be empty.");
		}

		final Point3f p = Point3f.fromPool();
		INode closestNode = nodes.get(0);
		float shortestDistance = Float.MAX_VALUE;

		for (int i = 0; i < nodes.size(); i++) {
			final INode n = nodes.get(i);
			n.getWorldMatrix().get(p);
			final float distance = point.distance(p);

			if (distance < shortestDistance) {
				shortestDistance = distance;
				closestNode = n;
			}
		}

		Point3f.toPool(p);

		if (!closestNode.getNodes().isEmpty()) {
			return closestNode.getNodeClosestToPoint(point);
		}

		return closestNode;
	}
}
