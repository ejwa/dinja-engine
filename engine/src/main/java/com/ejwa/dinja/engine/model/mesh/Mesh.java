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

import com.ejwa.dinja.engine.model.properties.Rotatable;
import com.ejwa.dinja.engine.model.properties.Scalable;
import com.ejwa.dinja.engine.model.properties.Translatable;
import com.ejwa.dinja.engine.model.transform.Rotator;
import com.ejwa.dinja.engine.model.transform.Scaler;
import com.ejwa.dinja.engine.model.transform.Translator;
import com.ejwa.dinja.engine.util.HashedArrayList;
import com.ejwa.dinja.opengles.ActiveTexture;
import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import com.ejwa.dinja.opengles.shader.argument.TextureRGB565Sampler;
import com.ejwa.dinja.opengles.shader.argument.Tuple2fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple3fVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.ColorfVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.UniformMatrix4f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Matrix4f;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;

public class Mesh implements Rotatable, Scalable, Translatable {
	private static final String COLOR_ATTRIBUTE_NAME = "aColor";
	private static final String MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME = "uModelViewProjectionMatrix";
	private static final String NORMAL_ATTRIBUTE_NAME = "aNormal";
	private static final String TEXTURE_COORDINATE_ATTRIBUTE_NAME = "aTexCoord";
	private static final String TEXTURE_SAMPLER_NAME = "sTexture";
	private static final String VERTEX_COORDINATE_ATTRIBUTE_NAME = "aPosition";

	private final List<Vertex> indices = new ArrayList<Vertex>();
	private final Matrix4f modelMatrix = new Matrix4f();
	private final String name;
	private final PrimitiveData primitiveData;
	private final PrimitiveType primitiveType;
	private Texture texture;
	private final List<Vertex> vertices = new HashedArrayList<Vertex>();

	private final Rotator rotator = new Rotator(modelMatrix);
	private final Scaler scaler = new Scaler(modelMatrix);
	private final Translator translator = new Translator(modelMatrix);

	public Mesh(String name, PrimitiveType primitiveType) {
		this.name = name;
		this.primitiveType = primitiveType;
		primitiveData = new PrimitiveData(primitiveType, VERTEX_COORDINATE_ATTRIBUTE_NAME);
		modelMatrix.setIdentity();

		final Matrix4f modelViewProjectionMatrix = new Matrix4f();
		modelViewProjectionMatrix.setIdentity();
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

	public List<Vertex> getIndices() {
		return indices;
	}

	public final void addIndices(Vertex ...indices) {
		if (!vertices.containsAll(Arrays.asList(indices))) {
			throw new GLException("Invalid faces list passed to mesh.");
		}

		this.indices.addAll(Arrays.asList(indices));
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public void setModelViewProjectionMatrix(Matrix4f modelViewProjectionMatrix) {
		primitiveData.getUniforms().get(MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME).set(modelViewProjectionMatrix);
	}

	public String getName() {
		return name;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;

		primitiveData.addSampler(new TextureRGB565Sampler(TEXTURE_SAMPLER_NAME, ActiveTexture.GL_TEXTURE0,
		                         texture.getWidth(), texture.getHeight(), texture.getPixelsRGB565()));
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public final void addVertices(Vertex ...vertices) {
		this.vertices.addAll(Arrays.asList(vertices));
	}

	private void updatePrimitiveDataAttributes(Vector3f positions[], Colorf colors[], Vector3f normals[], Vector2f textureCoordinates[]) {
		primitiveData.setVerticesData(positions);
		primitiveData.removeVertexAttributeArray(COLOR_ATTRIBUTE_NAME);
		primitiveData.removeVertexAttributeArray(NORMAL_ATTRIBUTE_NAME);
		primitiveData.removeVertexAttributeArray(TEXTURE_COORDINATE_ATTRIBUTE_NAME);

		if (colors[0] != null) {
			primitiveData.addVertexAttributeArray(new ColorfVertexAttributeArray(COLOR_ATTRIBUTE_NAME, colors));
		}

		if (normals[0] != null) {
			primitiveData.addVertexAttributeArray(new Tuple3fVertexAttributeArray(NORMAL_ATTRIBUTE_NAME, normals));
		}

		if (textureCoordinates[0] != null) {
			primitiveData.addVertexAttributeArray(new Tuple2fVertexAttributeArray(TEXTURE_COORDINATE_ATTRIBUTE_NAME, textureCoordinates));
		}
	}

	private void updatePrimitiveDataIndices() {
		final List<Integer> glIndices = new ArrayList<Integer>();

		for (Vertex i : indices) {
			glIndices.add(vertices.indexOf(i));
		}

		primitiveData.setIndices(glIndices.toArray(new Integer[glIndices.size()]));
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void updatePrimitiveData() {
		final Colorf colors[] = new Colorf[this.vertices.size()];
		final Vector3f normals[] = new Vector3f[this.vertices.size()];
		final Vector3f positions[] = new Vector3f[this.vertices.size()];
		final Vector2f textureCoordinates[] = new Vector2f[this.vertices.size()];

		for (int i = 0; i < this.vertices.size(); i++) {
			colors[i] = this.vertices.get(i).getColor();
			normals[i] = this.vertices.get(i).getNormal();
			positions[i] = this.vertices.get(i).getPosition();
			textureCoordinates[i] = this.vertices.get(i).getTextureCoordinates();
		}

		updatePrimitiveDataAttributes(positions, colors, normals, textureCoordinates);
		updatePrimitiveDataIndices();
	}

	public PrimitiveData getPrimitiveData() {
		return primitiveData;
	}

	@Override
	public Rotator getRotator() {
		return rotator;
	}

	@Override
	public Scaler getScaler() {
		return scaler;
	}

	@Override
	public Translator getTranslator() {
		return translator;
	}
}
