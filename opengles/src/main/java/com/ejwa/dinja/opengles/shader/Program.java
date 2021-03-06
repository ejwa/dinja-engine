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
package com.ejwa.dinja.opengles.shader;

import android.util.Log;
import com.ejwa.dinja.opengles.error.GLError;
import com.ejwa.dinja.opengles.error.GLException;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("PMD.ProtectLogD")
public class Program {
	private int handle = 0;
	private VertexShader vertexShader;
	private FragmentShader fragmentShader;
	private final Map<String, Integer> vertexAttributeHandles = new HashMap<String, Integer>();
	private final Map<String, Integer> uniformHandles = new HashMap<String, Integer>();
	private final List<AbstractUniform> globalUniforms = Collections.synchronizedList(new ArrayList<AbstractUniform>());

	public Program() {
		/* It's possible to create an empty Program and then call attach(vs, fs); */
	}

	public Program(VertexShader vertexShader, FragmentShader fragmentShader) {
		attach(vertexShader, fragmentShader);
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings({"EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS", "REC_CATCH_EXCEPTION"})
	public Program(String vertexShader, String fragmentShader) {
		final InputStream vs = getClass().getResourceAsStream(vertexShader);
		final InputStream fs = getClass().getResourceAsStream(fragmentShader);

		try {
			attach(new VertexShader(vs), new FragmentShader(fs));
			vs.close();
			fs.close();
		} catch (Exception ex) {
			throw new GLException("Failed to open shader source files.", ex);
		}
	}

	public final void attach(VertexShader vertexShader, FragmentShader fragmentShader) {
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
	}

	public void registerVertexAttributeHandles(String ...vertexAttributeVariableNames) {
		for (String n : vertexAttributeVariableNames) {
			final int vertexAttributeHandle = OpenGLES2Native.glGetAttribLocation(handle, n);

			if (vertexAttributeHandle != -1) {
				Log.d(Program.class.getName(), String.format("Registered vertex attribute '%s' at position %d.",
				                                             n, vertexAttributeHandle));
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

	public void registerUniformHandles(String ...uniformVariableNames) {
		for (String n : uniformVariableNames) {
			final int uniformHandle = OpenGLES2Native.glGetUniformLocation(handle, n);

			if (uniformHandle != -1) {
				Log.d(Program.class.getName(), String.format("Registered uniform '%s' at position %d.", n, uniformHandle));
				uniformHandles.put(n, uniformHandle);
			}
		}
	}

	public int getUniformHandle(String uniformVariableName) {
		final Integer uniformHandle = uniformHandles.get(uniformVariableName);

		if (uniformHandle == null) {
			return -1;
		}

		return uniformHandle;
	}

	public void registerGlobalUniform(AbstractUniform uniform) {
		globalUniforms.add(uniform);
	}

	public void unregisterGlobalUniform(AbstractUniform uniform) {
		globalUniforms.remove(uniform);
	}

	public List<AbstractUniform> getGlobalUniforms() {
		return globalUniforms;
	}

	private String getProgramInfoLog() {
		final BytePointer infoLogPtr = new BytePointer(256);
		final IntPointer length = new IntPointer(1);

		OpenGLES2Native.glGetProgramInfoLog(handle, infoLogPtr.capacity(), length, infoLogPtr);

		final String infoLog = infoLogPtr.getString();
		infoLogPtr.deallocate();
		length.deallocate();

		return infoLog;
	}

	private void linkProgram() {
		OpenGLES2Native.glLinkProgram(handle);

		if (!isLinked()) {
			final String infoLog = getProgramInfoLog();

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
		handle = OpenGLES2Native.glCreateProgram();

		if (handle == 0) {
			throw new GLException("Failed to create program.");
		}

		OpenGLES2Native.glAttachShader(handle, vertexShader.getHandle());
		GLError.check(Program.class);

		OpenGLES2Native.glAttachShader(handle, fragmentShader.getHandle());
		GLError.check(Program.class);

		linkProgram();
	}

	public void delete() {
		OpenGLES2Native.glDeleteProgram(handle);
	}

	public int getHandle() {
		return handle;
	}

	private int getProgramiv(int paramName) {
		final IntPointer parameterPtr = new IntPointer(1);
		OpenGLES2Native.glGetProgramiv(handle, paramName, parameterPtr);

		final int parameter = parameterPtr.get();
		parameterPtr.deallocate();
		return parameter;
	}

	public boolean isLinked() {
		final int GL_LINK_STATUS = 0x8b82;
		return getProgramiv(GL_LINK_STATUS) != 0;
	}

	public void use() {
		OpenGLES2Native.glUseProgram(handle);
	}
}
