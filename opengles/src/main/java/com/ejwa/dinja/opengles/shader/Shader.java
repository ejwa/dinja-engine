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
import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.OpenGLES2;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.PointerPointer;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

class Shader {
	private final int shaderHandle;

	private void attachShaderSource(String shaderSource) {
		final BytePointer sourceCodePtr = new BytePointer(shaderSource);
		final PointerPointer sourceCodePtrPtr =  new PointerPointer(new BytePointer[] { sourceCodePtr });

		OpenGLES2.glShaderSource(shaderHandle, 1, sourceCodePtrPtr, null);
		OpenGLES2.glCompileShader(shaderHandle);

		sourceCodePtrPtr.deallocate();
		sourceCodePtr.deallocate();
	}

	public Shader(int shaderType, File shaderSource) throws IOException {
		this(shaderType, FileUtils.readFileToString(shaderSource));
	}

	public Shader(int shaderType, String shaderSource) {
		shaderHandle = OpenGLES2.glCreateShader(shaderType);
		attachShaderSource(shaderSource);

		if (!isCompiled()) {
			final String infoLog = getInfoLog();

			delete();
			Log.e(Shader.class.getName(), infoLog);
			throw new GLException("Failed to compile shader.");
		}
	}

	public final void delete() {
		OpenGLES2.glDeleteShader(shaderHandle);
	}

	private int getParameter(int code) {
		final IntPointer parameterPtr = new IntPointer(1);
		OpenGLES2.glGetShaderiv(shaderHandle, code, parameterPtr);

		final int parameter = parameterPtr.get();
		parameterPtr.deallocate();
		return parameter;
	}

	public boolean isFlaggedForDeletion() {
		final int GL_DELETE_STATUS = 0x8b80;
		return getParameter(GL_DELETE_STATUS) != 0;
	}

	public final boolean isCompiled() {
		final int GL_COMPILE_STATUS = 0x8b81;
		return getParameter(GL_COMPILE_STATUS) != 0;
	}

	public final String getInfoLog() {
		final BytePointer infoLogPtr = new BytePointer(256);
		final IntPointer length = new IntPointer(1);

		OpenGLES2.glGetShaderInfoLog(shaderHandle, infoLogPtr.capacity(), length, infoLogPtr);

		final String infoLog = infoLogPtr.getString();
		infoLogPtr.deallocate();
		length.deallocate();

		return infoLog;
	}
}
