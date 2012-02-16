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

import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.argument.AbstractSampler;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import java.util.List;
import java.util.Set;

public abstract class AbstractDraw implements IFrameDrawListener {
	protected final Program program;
	private boolean enabled = true;

	public AbstractDraw(Program program) {
		this.program = program;
	}

	@Override
	public Program getProgram() {
		return program;
	}

	@Override
	public synchronized boolean isEnabled() {
		return enabled;
	}

	@Override
	public synchronized void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void onPrepareDraw(List<PrimitiveData> primitiveDataList) {
		program.compile();

		synchronized (program.getGlobalUniforms()) {
			for (AbstractUniform u : program.getGlobalUniforms()) {
				program.registerUniformHandles(u.getVariableName());
			}
		}

		for (PrimitiveData p : primitiveDataList) {
			final Set<String> attributes = p.getVertexAttributeArrays().getNames();
			final Set<String> uniforms = p.getUniforms().getNames();

			program.registerVertexAttributeHandles(p.getVertices().getVariableName());
			program.registerVertexAttributeHandles(attributes.toArray(new String[attributes.size()]));
			program.registerUniformHandles(uniforms.toArray(new String[uniforms.size()]));

			for (AbstractSampler s : p.getSamplers()) {
				program.registerUniformHandles(s.getVariableName());
				s.bind();
			}
		}
	}

	@Override
	public abstract void onDrawFrame(List<PrimitiveData> primitiveData);
}
