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
package com.ejwa.dinja.engine.controller;

import com.ejwa.dinja.engine.model.node.INode;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.view.SceneView;
import com.ejwa.dinja.opengles.display.IFrameUpdateListener;
import org.openmali.vecmath2.Matrix4f;

public class SceneController implements Controllable, IFrameUpdateListener {
	private final SceneView sceneView;
	private final Matrix4f modelViewProjectionMatrix = new Matrix4f();

	public SceneController(SceneView sceneView) {
		this.sceneView = sceneView;
	}

	private void handleMesh(Mesh mesh) {
		modelViewProjectionMatrix.mul(sceneView.getScene().getCamera().getViewProjectionMatrix(), mesh.getWorldMatrix());
		mesh.setModelViewProjectionMatrix(modelViewProjectionMatrix);
	}

	private void handleChildren(INode node, Matrix4f propagatedModelMatrix) {
		for (int i = 0; i < node.getNodes().size(); i++) {
			final INode n = node.getNodes().get(i);
			n.getWorldMatrix().mul(propagatedModelMatrix, n.getModelMatrix());

			if (!n.getNodes().isEmpty()) {
				handleChildren(n, n.getWorldMatrix());
			}

			if (n instanceof Mesh) {
				handleMesh((Mesh) n);
			}
		}
	}

	@Override
	public void onFrameUpdate(long milliSecondsSinceLastFrame) {
		final Matrix4f modelMatrix = sceneView.getScene().getModelMatrix();

		sceneView.getScene().acquire();
		handleChildren(sceneView.getScene(), modelMatrix);
		sceneView.getScene().release();

		sceneView.getScene().getWorldMatrix().set(modelMatrix);
	}
}
