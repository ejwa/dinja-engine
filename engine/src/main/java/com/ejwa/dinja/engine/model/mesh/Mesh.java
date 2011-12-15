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
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import com.ejwa.dinja.opengles.shader.argument.Tuple2fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple3fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple4fVertexAttributeArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Mesh {
	private static final String VERTEX_COORDINATE_ATTRIBUTE_NAME = "vPosition";
	private static final String COLOR_ATTRIBUTE_NAME = "vColor";
	private static final String NORMAL_ATTRIBUTE_NAME = "vNormal";
	private static final String TEXTURE_COORDINATE_ATTRIBUTE_NAME = "vTextCoord";

	private final String name;
	private final PrimitiveType primitiveType;
	private final List<Face> faces = new LinkedList<Face>();
	private final List<Vertex> vertices = new LinkedList<Vertex>();
	private final PrimitiveData primitiveData;

	public Mesh(String name, PrimitiveType primitiveType) {
		this.name = name;
		this.primitiveType = primitiveType;
		primitiveData = new PrimitiveData(primitiveType, VERTEX_COORDINATE_ATTRIBUTE_NAME);
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

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void updatePrimitiveData() {
		final Color4f colors[] = new Color4f[this.vertices.size()];
		final Vector3f normals[] = new Vector3f[this.vertices.size()];
		final Vector3f positions[] = new Vector3f[this.vertices.size()];
		final Vector2f textureCoords[] = new Vector2f[this.vertices.size()];

		for (int i = 0; i < this.vertices.size(); i++) {
			colors[i] = this.vertices.get(i).getColor();
			normals[i] = this.vertices.get(i).getNormal();
			positions[i] = this.vertices.get(i).getPosition();
			textureCoords[i] = this.vertices.get(i).getTextureCoordinates();
		}

		primitiveData.setVerticesData(positions);
		primitiveData.removeVertexAttributeArray(COLOR_ATTRIBUTE_NAME);
		primitiveData.removeVertexAttributeArray(NORMAL_ATTRIBUTE_NAME);
		primitiveData.removeVertexAttributeArray(TEXTURE_COORDINATE_ATTRIBUTE_NAME);

		if (colors[0] != null) {
			primitiveData.addVertexAttributeArray(new Tuple4fVertexAttributeArray(COLOR_ATTRIBUTE_NAME, colors));
		}

		if (normals[0] != null) {
			primitiveData.addVertexAttributeArray(new Tuple3fVertexAttributeArray(NORMAL_ATTRIBUTE_NAME, normals));
		}

		if (textureCoords[0] != null) {
			primitiveData.addVertexAttributeArray(new Tuple2fVertexAttributeArray(TEXTURE_COORDINATE_ATTRIBUTE_NAME, textureCoords));
		}

		final List<Integer> indices = new ArrayList<Integer>();

		for (Face f : this.faces) {
			for (Vertex v : f.getVertices()) {
				indices.add(vertices.indexOf(v));
			}
		}

		primitiveData.setIndices(indices.toArray(new Integer[indices.size()]));
	}

	public PrimitiveData getPrimitiveData() {
		return primitiveData;
	}
}
