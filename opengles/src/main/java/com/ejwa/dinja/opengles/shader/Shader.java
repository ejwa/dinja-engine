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
import com.ejwa.dinja.opengles.error.GLException;
import com.ejwa.dinja.opengles.library.OpenGLES2;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Shader {
	private int handle;
	private final String shaderSource;
	private final int shaderType;

	private int createShader(int shaderType) {
		final int h = OpenGLES2Native.glCreateShader(shaderType);

		if (h == 0) {
			throw new GLException("Failed to create shader.");
		}

		return h;
	}

	public Shader(int shaderType, File shaderSource) throws IOException {
		this(shaderType, FileUtils.readFileToString(shaderSource));
	}

	public Shader(int shaderType, InputStream shaderSource) throws IOException {
		this(shaderType, IOUtils.toString(shaderSource));
	}

	public Shader(int shaderType, String shaderSource) {
		this.shaderType = shaderType;
		this.shaderSource = shaderSource;
	}

	public void compile() {
		handle = createShader(shaderType);
		OpenGLES2.glShaderSource(handle, shaderSource);
		OpenGLES2Native.glCompileShader(handle);

		if (!isCompiled()) {
			final String infoLog = OpenGLES2.glGetShaderInfoLog(handle);

			delete();
			Log.e(Shader.class.getName(), infoLog);
			throw new GLException("Failed to compile shader.");
		}
	}

	public final void delete() {
		OpenGLES2Native.glDeleteShader(handle);
	}

	public int getHandle() {
		return handle;
	}

	public boolean isFlaggedForDeletion() {
		final int GL_DELETE_STATUS = 0x8b80;
		return OpenGLES2.glGetShaderiv(handle, GL_DELETE_STATUS) != 0;
	}

	public final boolean isCompiled() {
		final int GL_COMPILE_STATUS = 0x8b81;
		return OpenGLES2.glGetShaderiv(handle, GL_COMPILE_STATUS) != 0;
	}
}
