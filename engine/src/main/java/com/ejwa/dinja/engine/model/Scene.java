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

import com.ejwa.dinja.engine.model.mesh.Mesh;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Scene {
	private final Camera camera;
	private final List<Mesh> meshes = new LinkedList<Mesh>();

	public Scene(Camera camera) {
		this.camera = camera;
	}

	public Scene(Camera camera, Mesh ...meshes) {
		this(camera);
		addMeshes(meshes);
	}

	public Camera getCamera() {
		return camera;
	}

	public final void addMeshes(Mesh ...meshes) {
		this.meshes.addAll(Arrays.asList(meshes));
	}

	public void removeMeshes(Mesh ...meshes) {
		this.meshes.removeAll(Arrays.asList(meshes));
	}

	public void removeMeshes(String ...meshNames) {
		for (String n : meshNames) {
			for (Mesh m : meshes) {
				if (m.getName().equals(n)) {
					meshes.remove(m);
				}
			}
		}
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}
}
