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
package com.ejwa.dinja.engine.model.node.mesh;

import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import com.ejwa.dinja.opengles.shader.argument.ColorfVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple2fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple3fVertexAttributeArray;
import java.util.ArrayList;
import java.util.List;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;

public class MeshPrimitiveData extends PrimitiveData {
	public static final String COLOR_ATTRIBUTE_NAME = "aColor";
	public static final String MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME = "uModelViewProjectionMatrix";
	public static final String NORMAL_ATTRIBUTE_NAME = "aNormal";
	public static final String TEXTURE_COORDINATE_ATTRIBUTE_NAME = "aTexCoord";
	public static final String TEXTURE_SAMPLER_NAME = "sTexture";
	public static final String VERTEX_COORDINATE_ATTRIBUTE_NAME = "aPosition";
	private final List<Vertex> vertices;
	private final List<Vertex> indices;

	public MeshPrimitiveData(PrimitiveType primitiveType, String vertexCoordVariableName,
		                 List<Vertex> vertices, List<Vertex> indices) {
		super(primitiveType, vertexCoordVariableName);
		this.vertices = vertices;
		this.indices = indices;
	}

	private void updatePrimitiveDataAttributes(Vector3f positions[], Colorf colors[], Vector3f normals[],
	                                           Vector2f textureCoordinates[]) {
		setVerticesData(positions);
		removeVertexAttributeArray(COLOR_ATTRIBUTE_NAME);
		removeVertexAttributeArray(NORMAL_ATTRIBUTE_NAME);
		removeVertexAttributeArray(TEXTURE_COORDINATE_ATTRIBUTE_NAME);

		if (colors[0] != null) {
			addVertexAttributeArray(new ColorfVertexAttributeArray(COLOR_ATTRIBUTE_NAME, colors));
		}

		if (normals[0] != null) {
			addVertexAttributeArray(new Tuple3fVertexAttributeArray(NORMAL_ATTRIBUTE_NAME, normals));
		}

		if (textureCoordinates[0] != null) {
			addVertexAttributeArray(new Tuple2fVertexAttributeArray(TEXTURE_COORDINATE_ATTRIBUTE_NAME,
			                        textureCoordinates));
		}
	}

	private void updatePrimitiveDataIndices() {
		final List<Integer> glIndices = new ArrayList<Integer>();

		for (Vertex i : indices) {
			glIndices.add(vertices.indexOf(i));
		}

		setIndices(glIndices.toArray(new Integer[glIndices.size()]));
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void update() {
		final Colorf colors[] = new Colorf[vertices.size()];
		final Vector3f normals[] = new Vector3f[vertices.size()];
		final Vector3f positions[] = new Vector3f[vertices.size()];
		final Vector2f textureCoordinates[] = new Vector2f[vertices.size()];

		for (int i = 0; i < vertices.size(); i++) {
			colors[i] = vertices.get(i).getColor();
			normals[i] = vertices.get(i).getNormal();
			positions[i] = vertices.get(i).getPosition();
			textureCoordinates[i] = vertices.get(i).getTextureCoordinates();
		}

		updatePrimitiveDataAttributes(positions, colors, normals, textureCoordinates);
		updatePrimitiveDataIndices();
	}
}
