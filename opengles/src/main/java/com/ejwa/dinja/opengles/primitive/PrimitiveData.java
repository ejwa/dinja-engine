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
package com.ejwa.dinja.opengles.primitive;

import com.ejwa.dinja.opengles.blend.BlendDestinationFactor;
import com.ejwa.dinja.opengles.blend.BlendSourceFactor;
import com.ejwa.dinja.opengles.blend.Blending;
import com.ejwa.dinja.opengles.error.GLException;
import com.ejwa.dinja.opengles.library.NativeMemory;
import com.ejwa.dinja.opengles.shader.argument.AbstractSampler;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import com.ejwa.dinja.opengles.shader.argument.AbstractVertexAttributeArray;
import com.ejwa.dinja.opengles.shader.argument.Tuple3fVertexAttributeArray;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.ShortPointer;
import org.openmali.vecmath2.Vector3f;

/* TODO: Things like uniform.getData().deallocate() should happen in the uniform finalize method, and not here... */
public class PrimitiveData {
	private Blending blending = new Blending(BlendSourceFactor.GL_ONE, BlendDestinationFactor.GL_ZERO);
	private final PrimitiveType primitiveType;
	private Pointer indices;
	private final Tuple3fVertexAttributeArray vertices;
	private final PrimitiveDataArgument<AbstractSampler> samplers = new PrimitiveDataArgument<AbstractSampler>();
	private final PrimitiveDataArgument<AbstractUniform> uniforms = new PrimitiveDataArgument<AbstractUniform>();
	private final PrimitiveDataArgument<AbstractVertexAttributeArray> vertexAttributeArrays = new PrimitiveDataArgument<AbstractVertexAttributeArray>();

	public PrimitiveData(PrimitiveType primitiveType, String vertexCoordVariableName) {
		this.primitiveType = primitiveType;
		vertices = new Tuple3fVertexAttributeArray(vertexCoordVariableName);
	}

	public PrimitiveData(PrimitiveType primitiveType, String vertexCoordVariableName, Vector3f[] vertices, Integer ...indices) {
		this(primitiveType, vertexCoordVariableName);
		setVerticesData(vertices);
		setIndices(indices);
	}

	public Blending getBlending() {
		return blending;
	}

	public void setBlending(Blending blending) {
		this.blending = blending;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	public Pointer getIndices() {
		return indices;
	}

	public final void setIndices(Integer ...indices) {
		if (vertices.getData().capacity() / 3 >= 256) {
			this.indices = NativeMemory.getShortPointer(this.indices, indices.length);
		} else {
			this.indices = NativeMemory.getBytePointer(this.indices, indices.length);
		}

		for (int i = 0; i < indices.length; i++) {
			if (indices[i] >= vertices.getData().capacity() / 3) {
				throw new GLException(String.format("Indices must point within bounds of vertices (got %d, but " +
				                                     "maximum is %d)", indices[i], this.vertices.getData().capacity() / 3));
			}

			if (this.indices instanceof BytePointer) {
				((BytePointer) this.indices).put(i, indices[i].byteValue());
			} else {
				((ShortPointer) this.indices).put(i, indices[i].shortValue());
			}
		}
	}

	public Tuple3fVertexAttributeArray getVertices() {
		return vertices;
	}

	public final void setVerticesData(Vector3f  ...vertices) {
		this.vertices.setData(vertices);
	}

	public void addSampler(AbstractSampler sampler) {
		samplers.put(sampler.getVariableName(), sampler);
	}

	public void removeSampler(String variableName) {
		final AbstractSampler sampler = samplers.remove(variableName);

		if (sampler != null) {
			sampler.getData().deallocate();
		}
	}

	public PrimitiveDataArgument<AbstractSampler> getSamplers() {
		return samplers;
	}

	public void addUniform(AbstractUniform uniform) {
		uniforms.put(uniform.getVariableName(), uniform);
	}

	public void removeUniform(String variableName) {
		final AbstractUniform uniform = uniforms.remove(variableName);

		if (uniform != null) {
			uniform.getData().deallocate();
		}
	}

	public PrimitiveDataArgument<AbstractUniform> getUniforms() {
		return uniforms;
	}

	public void addVertexAttributeArray(AbstractVertexAttributeArray vertexAttributeArray) {
		vertexAttributeArrays.put(vertexAttributeArray.getVariableName(), vertexAttributeArray);
	}

	public void removeVertexAttributeArray(String variableName) {
		final AbstractVertexAttributeArray vertexAttributeArray = vertexAttributeArrays.remove(variableName);

		if (vertexAttributeArray != null) {
			vertexAttributeArray.getData().deallocate();
		}
	}

	public PrimitiveDataArgument<AbstractVertexAttributeArray> getVertexAttributeArrays() {
		return vertexAttributeArrays;
	}

	@Override
	protected void finalize() throws Throwable {
		if (indices != null) { indices.deallocate(); }
		if (vertices != null) { vertices.getData().deallocate(); }

		for (AbstractVertexAttributeArray a : vertexAttributeArrays) {
			a.getData().deallocate();
		}

		super.finalize();
	}
}
