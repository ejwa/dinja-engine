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
package com.ejwa.dinja.opengles.library;

import com.ejwa.dinja.opengles.DataType;
import com.ejwa.dinja.opengles.texture.TextureFormat;
import com.ejwa.dinja.opengles.texture.TextureTarget;
import com.ejwa.dinja.opengles.texture.TextureType;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.argument.AbstractSampler;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import com.ejwa.dinja.opengles.shader.argument.AbstractVertexAttributeArray;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Pointer;

public final class OpenGLES2  {
	private OpenGLES2() {
	}

	public static void glDrawElements(Program program, PrimitiveData primitiveData) {
		final Pointer indices =  primitiveData.getIndices();
		final DataType indicesType = primitiveData.getVertices().getData().capacity() / 3 >= 256 ? DataType.GL_UNSIGNED_SHORT : DataType.GL_UNSIGNED_BYTE;
		final int vertexAttributeHandle = program.getVertexAttributeHandle(primitiveData.getVertices().getVariableName());

		if (vertexAttributeHandle != -1) {
			OpenGLES2Native.glVertexAttribPointer(vertexAttributeHandle, 3, DataType.GL_FLOAT.getId() , false, 0, primitiveData.getVertices().getData());
			OpenGLES2Native.glEnableVertexAttribArray(vertexAttributeHandle);
		}

		for (int i = 0; i < primitiveData.getUniforms().size(); i++) {
			final AbstractUniform u = primitiveData.getUniforms().get(i);
			final int uniformHandle = program.getUniformHandle(u.getVariableName());
			u.send(uniformHandle);
		}

		for (int i = 0; i < primitiveData.getVertexAttributeArrays().size(); i++) {
			final AbstractVertexAttributeArray va = primitiveData.getVertexAttributeArrays().get(i);
			final int vAttributeArrayHandle = program.getVertexAttributeHandle(va.getVariableName());
			OpenGLES2Native.glVertexAttribPointer(vAttributeArrayHandle, va.getComponents(), DataType.GL_FLOAT.getId(), false, 0, va.getData());
			OpenGLES2Native.glEnableVertexAttribArray(vAttributeArrayHandle);
		}

		for (int i = 0; i < primitiveData.getSamplers().size(); i++) {
			final AbstractSampler s = primitiveData.getSamplers().get(i);
			final int samplerHandle = program.getUniformHandle(s.getVariableName());
			s.send(samplerHandle);
		}

		primitiveData.getBlending().enable();
		OpenGLES2Native.glDrawElements(primitiveData.getPrimitiveType().getId(), indices.capacity(), indicesType.getId(), indices);
	}

	public static void glTexImage2D(int level, int width, int height, TextureFormat format, TextureType type, Pointer pixels) {
		OpenGLES2Native.glTexImage2D(TextureTarget.GL_TEXTURE_2D.getId(), level, format.getId(), width, height, 0, format.getId(), type.getId(), pixels);
	}

	public static int glGenTexture() {
		final IntPointer handlePtr = new IntPointer(1);
		final int handle;

		OpenGLES2Native.glGenTextures(1, handlePtr);
		handle = handlePtr.get(0);
		handlePtr.deallocate();

		return handle;
	}
}
