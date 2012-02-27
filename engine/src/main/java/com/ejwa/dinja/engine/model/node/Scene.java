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

import com.ejwa.dinja.engine.model.Camera;
import com.ejwa.dinja.engine.model.node.INode;
import com.ejwa.dinja.engine.model.node.BaseRootNode;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;

public class Scene extends BaseRootNode {
	protected final Camera camera;

	public Scene(Camera camera) {
		super("Scene");
		this.camera = camera;
	}

	public Scene(Camera camera, INode ...nodes) {
		this(camera);
		addNodes(nodes);
	}

	public Camera getCamera() {
		return camera;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private int countMeshes(INode node) {
		int count = 0;

		for (int i = 0; i < node.getNodes().size(); i++) {
			final INode n = node.getNodes().get(i);

			if (!n.getNodes().isEmpty()) {
				count += countMeshes(n);
			}

			if (n instanceof Mesh) {
				count++;
			}
		}

		return count;
	}

	public int countMeshes() {
		return countMeshes(this);
	}
}
