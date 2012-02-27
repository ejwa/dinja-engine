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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Point3f;

public class BaseNode implements INode {
	protected final Matrix4f modelMatrix = new Matrix4f();
	protected final Matrix4f worldMatrix = new Matrix4f();
	private final String name;
	protected IRootNode root;
	private final List<INode> nodes = new ArrayList<INode>();
	private final Map<String, INode> nodeNames = new HashMap<String, INode>();

	protected BaseNode(String name) {
		modelMatrix.setIdentity();
		this.name = name;
	}

	@Override
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	@Override
	public void propagateModelMatrix() {
		for (INode n : nodes) {
			n.getModelMatrix().mul(getModelMatrix(), n.getModelMatrix());
		}
	}

	private void propagateModelMatrixDeep(INode node) {
		for (INode n : nodes) {
			n.getModelMatrix().mul(node.getModelMatrix(), n.getModelMatrix());
			propagateModelMatrixDeep(n);
		}
	}

	@Override
	public void propagateModelMatrixDeep() {
		propagateModelMatrixDeep(this);
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
	public IRootNode getRoot() {
		return root;
	}

	@Override
	public void setRoot(IRootNode root) {
		this.root = root;
	}

	@Override
	public void addNodes(INode ...nodes) {
		for (INode n : nodes) {
			if (root != null) {
				root.setRootDeep(n);
			}

			if (nodeNames.put(n.getName(), n) != null) {
				throw new NodeAlreadyAddedException(n);
			}

			this.nodes.add(n);
		}
	}

	@Override
	public void removeNodes(INode ...nodes) {
		for (INode n : nodes) {
			if (root != null) {
				root.clearRootDeep(n);
			}

			if (nodeNames.remove(n.getName()) == null) {
				throw new NodeNotFoundException(n);
			}

			this.nodes.remove(n);
		}
	}

	@Override
	public void removeNodes(String ...nodeNames) {
		for (String nodeName : nodeNames) {
			final INode n = this.nodeNames.get(nodeName);

			if (n == null) {
				throw new NodeNotFoundException(nodeName);
			}

			removeNodes(n);
		}
	}

	@Override
	public List<INode> getNodes() {
		return nodes;
	}

	@Override
	public INode getNode(String nodeName) {
		return nodeNames.get(nodeName);
	}

	@Override
	public INode getNodeClosestToPointDeep(Point3f point) {
		if (!getNodes().isEmpty()) {
			return BaseNode.getNodeClosestToPointDeep(point, getNodes());
		}

		return null;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static INode getNodeClosestToPointDeep(Point3f point, List<INode> nodes) {
		if (nodes.isEmpty()) {
			throw new IllegalArgumentException("Nodes array can't be empty.");
		}

		final Point3f p = Point3f.fromPool();
		INode closestNode = nodes.get(0);
		float shortestDistance = Float.MAX_VALUE;

		for (INode n : nodes) {
			n.getWorldMatrix().get(p);
			final float distance = point.distance(p);

			if (distance < shortestDistance) {
				shortestDistance = distance;
				closestNode = n;
			}
		}

		Point3f.toPool(p);

		if (!closestNode.getNodes().isEmpty()) {
			return closestNode.getNodeClosestToPointDeep(point);
		}

		return closestNode;
	}
}
