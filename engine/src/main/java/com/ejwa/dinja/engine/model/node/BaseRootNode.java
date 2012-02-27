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

import java.util.HashMap;
import java.util.Map;

public class BaseRootNode extends BaseNode implements IRootNode {
	private final Map<String, INode> allNodes = new HashMap<String, INode>();

	protected BaseRootNode(String name) {
		super(name);
		root = this;
	}

	@Override
	public INode getNodeDeep(String nodeName) {
		return allNodes.get(nodeName);
	}

	@Override
	public void setRootDeep(INode node) {
		node.setRoot(this);

		if (allNodes.put(node.getName(), node) != null) {
			throw new NodeAlreadyAddedException(node, this);
		}

		for (INode n : node.getNodes()) {
			setRootDeep(n);
		}
	}

	@Override
	public void clearRootDeep(INode node) {
		node.setRoot(null);

		if (allNodes.remove(node.getName()) == null) {
			throw new NodeNotFoundException(node, this);
		}

		for (INode n : node.getNodes()) {
			clearRootDeep(n);
		}
	}
}
