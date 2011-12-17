/*
 * Copyright © 2011 Ejwa Software. All rights reserved.
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

import com.ejwa.dinja.engine.model.Scene;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import com.ejwa.dinja.opengles.display.IFrameUpdateListener;
import javax.vecmath.Matrix4f;

public class SceneController implements Controllable, IFrameUpdateListener {
	private final Scene scene;

	public SceneController(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void onFrameUpdate(long milliSecondsSinceLastFrame) {
		final Matrix4f modelViewProjectionMatrix = new Matrix4f();

		for (Mesh m : scene.getMeshes()) {
			final Matrix4f projectionMatrix = scene.getCamera().getProjectionMatrix();
			final Matrix4f viewMatrix = scene.getCamera().getViewMatrix();

			modelViewProjectionMatrix.mul(projectionMatrix, viewMatrix);
			modelViewProjectionMatrix.mul(m.getModelMatrix());
			m.setModelViewProjectionMatrix(modelViewProjectionMatrix);
		}
	}
}
