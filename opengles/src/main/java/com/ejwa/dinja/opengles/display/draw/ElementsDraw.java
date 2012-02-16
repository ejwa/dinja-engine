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
package com.ejwa.dinja.opengles.display.draw;

import com.ejwa.dinja.opengles.Capability;
import com.ejwa.dinja.opengles.DataType;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.argument.AbstractSampler;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import com.ejwa.dinja.opengles.shader.argument.AbstractVertexAttributeArray;
import com.googlecode.javacpp.Pointer;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;

public class ElementsDraw extends AbstractDraw {
	public ElementsDraw(Program program) {
		super(program);
	}

	@Override
	public synchronized void onDrawFrame(List<PrimitiveData> primitiveDataList) {
		Capability.GL_DEPTH_TEST.enable();
		Capability.GL_CULL_FACE.enable();
		Capability.GL_BLEND.enable();

		program.use();
		OpenGLES2Native.glClear(GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < primitiveDataList.size(); i++) {
			final PrimitiveData pd = primitiveDataList.get(i);
			final Pointer indices =  pd.getIndices();
			final DataType indicesType = pd.getVertices().getData().capacity() / 3 >= 256 ? DataType.GL_UNSIGNED_SHORT : DataType.GL_UNSIGNED_BYTE;
			final int vertexAttributeHandle = program.getVertexAttributeHandle(pd.getVertices().getVariableName());

			if (vertexAttributeHandle != -1) {
				OpenGLES2Native.glVertexAttribPointer(vertexAttributeHandle, 3, DataType.GL_FLOAT.getId() , false, 0, pd.getVertices().getData());
				OpenGLES2Native.glEnableVertexAttribArray(vertexAttributeHandle);
			}

			for (int j = 0; j < pd.getUniforms().size(); j++) {
				final AbstractUniform u = pd.getUniforms().get(j);
				final int uniformHandle = program.getUniformHandle(u.getVariableName());
				u.send(uniformHandle);
			}

			for (int j = 0; j < pd.getVertexAttributeArrays().size(); j++) {
				final AbstractVertexAttributeArray va = pd.getVertexAttributeArrays().get(j);
				final int vAttributeArrayHandle = program.getVertexAttributeHandle(va.getVariableName());
				OpenGLES2Native.glVertexAttribPointer(vAttributeArrayHandle, va.getComponents(), DataType.GL_FLOAT.getId(), false, 0, va.getData());
				OpenGLES2Native.glEnableVertexAttribArray(vAttributeArrayHandle);
			}

			for (int j = 0; j < pd.getSamplers().size(); j++) {
				final AbstractSampler s = pd.getSamplers().get(j);
				final int samplerHandle = program.getUniformHandle(s.getVariableName());
				s.send(samplerHandle);
			}

			pd.getBlending().enable();
			OpenGLES2Native.glDrawElements(pd.getPrimitiveType().getId(), indices.capacity(), indicesType.getId(), indices);
		}

		Capability.GL_DEPTH_TEST.disable();
		Capability.GL_CULL_FACE.disable();
		Capability.GL_BLEND.disable();
	}
}
