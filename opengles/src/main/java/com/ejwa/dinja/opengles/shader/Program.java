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
package com.ejwa.dinja.opengles.shader;

import android.util.Log;
import com.ejwa.dinja.opengles.GLError;
import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.library.OpenGLES2;
import java.util.HashMap;
import java.util.Map;

public class Program {
	private int handle = 0;
	private VertexShader vertexShader;
	private FragmentShader fragmentShader;
	private final Map<String, Integer> vertexAttributeHandles = new HashMap<String, Integer>();

	public Program() {
		/* It's possible to create an empty Program and then call attach(vs, fs); */
	}

	public Program(VertexShader vertexShader, FragmentShader fragmentShader) {
		attach(vertexShader, fragmentShader);
	}

	public final void attach(VertexShader vertexShader, FragmentShader fragmentShader) {
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
	}

	public void registerVertexAttributeHandles(String ...vertexAttributeVariableNames) {
		for (String n : vertexAttributeVariableNames) {
			final int vertexAttributeHandle = OpenGLES2.glGetAttribLocation(handle, n);

			if (vertexAttributeHandle != -1) {
				vertexAttributeHandles.put(n, vertexAttributeHandle);
			}
		}
	}

	public int getVertexAttributeHandle(String vertexAttributeVariableName) {
		final Integer vertexAttributeHandle = vertexAttributeHandles.get(vertexAttributeVariableName);

		if (vertexAttributeHandle == null) {
			return -1;
		}

		return vertexAttributeHandle;
	}

	private void linkProgram() {
		OpenGLES2.glLinkProgram(handle);

		if (!isLinked()) {
			final String infoLog = OpenGLES2.glGetProgramInfoLog(handle);

			delete();
			Log.e(Shader.class.getName(), infoLog);
			throw new GLException("Failed to link program.");
		}
	}

	public void compile() {
		if (vertexShader == null || fragmentShader == null) {
			throw new GLException("A vertex shader and a fragment shader has to be attached before compiling.");
		}

		vertexShader.compile();
		fragmentShader.compile();
		handle = OpenGLES2.glCreateProgram();

		if (handle == 0) {
			throw new GLException("Failed to create program.");
		}

		OpenGLES2.glAttachShader(handle, vertexShader.getHandle());
		GLError.check(Program.class);

		OpenGLES2.glAttachShader(handle, fragmentShader.getHandle());
		GLError.check(Program.class);

		linkProgram();
	}

	public void delete() {
		OpenGLES2.glDeleteProgram(handle);
	}

	public int getHandle() {
		return handle;
	}

	public boolean isLinked() {
		final int GL_LINK_STATUS = 0x8b82;
		return OpenGLES2.glGetProgramiv(handle, GL_LINK_STATUS) != 0;
	}

	public void use() {
		OpenGLES2.glUseProgram(handle);
	}
}
