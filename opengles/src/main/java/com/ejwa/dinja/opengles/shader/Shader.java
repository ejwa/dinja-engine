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

import com.ejwa.dinja.opengles.OpenGLES2;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.PointerPointer;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

class Shader {
	private final int shaderHandle;

	private void attachShaderFile(File shaderFile) throws IOException {
		final String shaderFileString = FileUtils.readFileToString(shaderFile);
		final PointerPointer sourceCode = new PointerPointer(new BytePointer(shaderFileString));

		OpenGLES2.glShaderSource(shaderHandle, 1, sourceCode, null);
	}

	public Shader(int shaderType, File shaderFile) throws IOException {
		shaderHandle = OpenGLES2.glCreateShader(shaderType);
		attachShaderFile(shaderFile);
	}

	public void delete() {
		OpenGLES2.glDeleteShader(shaderHandle);
	}
}
