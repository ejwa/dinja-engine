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
package com.ejwa.dinja.engine.model.mesh;

import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Mesh {
	private final String name;
	private final PrimitiveType primitiveType;
	private final List<Face> faces = new LinkedList<Face>();
	private final List<Vertex> vertices = new LinkedList<Vertex>();

	public Mesh(String name, PrimitiveType primitiveType) {
		this.name = name;
		this.primitiveType = primitiveType;
	}

	public Mesh(String name, PrimitiveType type, Vertex ...vertices) {
		this(name, type);
		addVertices(vertices);
	}

	public Mesh(String name, PrimitiveType primitiveType, Vertex vertices[], Face ...faces) {
		this(name, primitiveType, vertices);
		addFaces(faces);

	}

	public String getName() {
		return name;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	public final void addVertices(Vertex ...vertices) {
		this.vertices.addAll(Arrays.asList(vertices));
	}

	public final void addFaces(Face ...faces) {
		for (Face f : faces) {
			if (!vertices.containsAll(f.getVertices())) {
				throw new GLException("Invalid faces list passed to mesh.");
			}

			this.faces.add(f);
		}
	}

	public PrimitiveType getType() {
		return primitiveType;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Face> getFaces() {
		return faces;
	}
}
