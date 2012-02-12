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
package com.ejwa.dinja.engine.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.openmali.vecmath2.Matrix4f;

public class BaseNode implements INode {
	protected final Matrix4f modelMatrix = new Matrix4f();
	protected final String name;
	protected final List<INode> nodes = new LinkedList<INode>();

	public BaseNode(String name) {
		modelMatrix.setIdentity();
		this.name = name;
	}

	@Override
	public Matrix4f getModelMatrix() {
		return modelMatrix;
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
}
