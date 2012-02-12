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

import com.ejwa.dinja.engine.model.INode;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import com.ejwa.dinja.engine.view.SceneView;
import com.ejwa.dinja.opengles.display.IFrameUpdateListener;
import org.openmali.vecmath2.Matrix4f;

public class SceneController implements Controllable, IFrameUpdateListener {
	private final SceneView sceneView;

	public SceneController(SceneView sceneView) {
		this.sceneView = sceneView;
	}

	private void handleMesh(Mesh mesh, Matrix4f propagatedModelMatrix) {
		final Matrix4f modelViewProjectionMatrix = Matrix4f.fromPool();
		final Matrix4f projectionMatrix = sceneView.getScene().getCamera().getProjectionMatrix();
		final Matrix4f viewMatrix = sceneView.getScene().getCamera().getViewMatrix();

		modelViewProjectionMatrix.mul(projectionMatrix, viewMatrix);
		modelViewProjectionMatrix.mul(propagatedModelMatrix);

		mesh.setModelViewProjectionMatrix(modelViewProjectionMatrix);
		Matrix4f.toPool(modelViewProjectionMatrix);
	}

	private void handleChildren(INode node, Matrix4f propagatedModelMatrix) {
		final Matrix4f m = Matrix4f.fromPool();

		for (int i = 0; i < node.getNodes().size(); i++) {
			final INode n = node.getNodes().get(i);
			m.mul(propagatedModelMatrix, n.getModelMatrix());

			if (!n.getNodes().isEmpty()) {
				handleChildren(n, m);
			}

			if (n instanceof Mesh) {
				handleMesh((Mesh) n, m);
			}
		}

		Matrix4f.toPool(m);
	}

	@Override
	public void onFrameUpdate(long milliSecondsSinceLastFrame) {
		handleChildren(sceneView.getScene(), sceneView.getScene().getModelMatrix());
	}
}
