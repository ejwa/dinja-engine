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

import com.ejwa.dinja.engine.model.material.Material;
import com.ejwa.dinja.engine.model.node.BaseNode;
import com.ejwa.dinja.engine.model.properties.Rotatable;
import com.ejwa.dinja.engine.model.properties.Scalable;
import com.ejwa.dinja.engine.model.properties.Translatable;
import com.ejwa.dinja.engine.model.transform.Rotator;
import com.ejwa.dinja.engine.model.transform.Scaler;
import com.ejwa.dinja.engine.model.transform.Translator;
import com.ejwa.dinja.opengles.error.GLException;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import com.ejwa.dinja.opengles.shader.argument.UniformMatrix4f;
import com.ejwa.dinja.utility.type.HashedArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openmali.vecmath2.Matrix4f;

public class Mesh extends BaseNode implements Rotatable, Scalable, Translatable {
	private final List<Vertex> indices = new ArrayList<Vertex>();
	private final MeshPrimitiveData meshPrimitiveData;
	private final Material material;
	private final List<Vertex> vertices = new HashedArrayList<Vertex>();

	private final Translator translator = new Translator(modelMatrix);
	private final Rotator rotator = new Rotator(modelMatrix, translator.get());
	private final Scaler scaler = new Scaler(modelMatrix);

	public Mesh(String name, PrimitiveType primitiveType) {
		super(name);

		//TODO: Remove as many deps on MeshPrimitiveData as possible here...
		meshPrimitiveData = new MeshPrimitiveData(this, primitiveType, MeshPrimitiveData.VERTEX_COORDINATE_ATTRIBUTE_NAME,
		                                          vertices, indices);
		material = new Material(meshPrimitiveData);
		final Matrix4f modelViewProjectionMatrix = new Matrix4f();
		modelViewProjectionMatrix.setIdentity();
		meshPrimitiveData.addUniform(new UniformMatrix4f(MeshPrimitiveData.MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME,
		                             modelViewProjectionMatrix));
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

	public void setModelViewProjectionMatrix(Matrix4f modelViewProjectionMatrix) {
		meshPrimitiveData.getUniforms().get(MeshPrimitiveData.MODEL_VIEW_PROJECTION_MATRIX_UNIFORM_NAME).set(modelViewProjectionMatrix);
	}

	public Material getMaterial() {
		return material;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public final void addVertices(Vertex ...vertices) {
		this.vertices.addAll(Arrays.asList(vertices));
	}

	public MeshPrimitiveData getMeshPrimitiveData() {
		return meshPrimitiveData;
	}

	@Override
	public Translator getTranslator() {
		return translator;
	}

	@Override
	public Rotator getRotator() {
		return rotator;
	}

	@Override
	public Scaler getScaler() {
		return scaler;
	}
}
