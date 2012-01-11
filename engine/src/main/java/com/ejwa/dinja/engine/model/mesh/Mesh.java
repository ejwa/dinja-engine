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
package com.ejwa.dinja.engine.model.mesh;

import com.ejwa.dinja.engine.util.HashedArrayList;
import com.ejwa.dinja.opengles.ActiveTexture;
import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import com.ejwa.dinja.opengles.shader.argument.TextureRGB565Sampler;
import com.ejwa.dinja.opengles.shader.argument.Tuple2fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple3fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple4fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.UniformMatrix4f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Mesh {
	private static final String MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME = "uModelViewProjectionMatrix";
	private static final String VERTEX_COORDINATE_ATTRIBUTE_NAME = "aPosition";
	private static final String COLOR_ATTRIBUTE_NAME = "aColor";
	private static final String NORMAL_ATTRIBUTE_NAME = "aNormal";
	private static final String TEXTURE_COORDINATE_ATTRIBUTE_NAME = "aTexCoord";
	private static final String TEXTURE_SAMPLER_NAME = "sTexture";

	private final Matrix4f translationMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();
	private Matrix4f modelViewProjectionMatrix = modelMatrix;
	private final String name;
	private final PrimitiveType primitiveType;
	private final List<Vertex> indices = new ArrayList<Vertex>();
	private Texture texture;
	private final List<Vertex> vertices = new HashedArrayList<Vertex>();
	private final PrimitiveData primitiveData;

	public Mesh(String name, PrimitiveType primitiveType) {
		this.name = name;
		this.primitiveType = primitiveType;
		modelMatrix.setIdentity();
		primitiveData = new PrimitiveData(primitiveType, VERTEX_COORDINATE_ATTRIBUTE_NAME);
		primitiveData.addUniform(new UniformMatrix4f(MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME, modelViewProjectionMatrix));
	}

	public Mesh(String name, PrimitiveType type, Vertex ...vertices) {
		this(name, type);
		addVertices(vertices);
	}

	public Mesh(String name, PrimitiveType primitiveType, Vertex vertices[], Vertex ...indices) {
		this(name, primitiveType, vertices);
		addIndices(indices);

	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public void rotateX(float angle) {
		translationMatrix.setZero();
		translationMatrix.rotX(angle);
		modelMatrix.mul(translationMatrix);
	}

	public void rotateY(float angle) {
		translationMatrix.setZero();
		translationMatrix.rotY(angle);
		modelMatrix.mul(translationMatrix);
	}

	public void rotateZ(float angle) {
		translationMatrix.setZero();
		translationMatrix.rotZ(angle);
		modelMatrix.mul(translationMatrix);
	}

	public Matrix4f getModelViewProjectionMatrix() {
		return modelViewProjectionMatrix;
	}

	public void setModelViewProjectionMatrix(Matrix4f modelViewProjectionMatrix) {
		this.modelViewProjectionMatrix = modelViewProjectionMatrix;
		primitiveData.getUniforms().get(MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME).set(modelViewProjectionMatrix);
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

	public final void addIndices(Vertex ...indices) {
		if (!vertices.containsAll(Arrays.asList(indices))) {
			throw new GLException("Invalid faces list passed to mesh.");
		}

		this.indices.addAll(Arrays.asList(indices));
	}

	public PrimitiveType getType() {
		return primitiveType;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Vertex> getIndices() {
		return indices;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;

		primitiveData.addSampler(new TextureRGB565Sampler(TEXTURE_SAMPLER_NAME, ActiveTexture.GL_TEXTURE0,
		                         texture.getWidth(), texture.getHeight(), texture.getPixelsRGB565()));
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void updatePrimitiveDataAttributes() {
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

		final List<Integer> glIndices = new ArrayList<Integer>();

		for (Vertex i : indices) {
			glIndices.add(vertices.indexOf(i));
		}

		primitiveData.setIndices(glIndices.toArray(new Integer[glIndices.size()]));
	}

	public PrimitiveData getPrimitiveData() {
		return primitiveData;
	}
}
