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
import com.ejwa.dinja.opengles.OpenGLES2;

public class Program {
	private final int handle;

	private int createProgram() {
		final int h = OpenGLES2.glCreateProgram();

		if (h == 0) {
			throw new GLException("Failed to create program.");
		}

		return h;
	}

	public Program() {
		handle = createProgram();
	}

	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public Program(Shader ...shaders) {
		this();
		attach(shaders);
	}

	public final void attach(Shader ...shaders) {
		for (Shader s : shaders) {
			OpenGLES2.glAttachShader(handle, s.getHandle());
			GLError.check(Program.class);
		}

		OpenGLES2.glLinkProgram(handle);

		if (!isLinked()) {
			final String infoLog = OpenGLES2.glGetProgramInfoLog(handle);
	
			delete();
			Log.e(Shader.class.getName(), infoLog);
			throw new GLException("Failed to link program.");
		}
	}

	public final void delete() {
		OpenGLES2.glDeleteProgram(handle);
	}

	public int getHandle() {
		return handle;
	}

	public boolean isLinked() {
		final int GL_LINK_STATUS = 0x8b82;
		return OpenGLES2.glGetProgramiv(handle, GL_LINK_STATUS) != 0;
	}
}
