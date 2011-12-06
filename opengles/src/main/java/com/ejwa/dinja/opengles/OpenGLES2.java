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
package com.ejwa.dinja.opengles;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Platform;

@SuppressWarnings("PMD.TooManyMethods")
@Platform(cinclude = "GLES2/gl2.h", link = "GLESv2")
public final class OpenGLES2 {
	static { Loader.load(); }

	public static final int GL_ES_VERSION_2_0 = 1;
	public static final int GL_DEPTH_BUFFER_BIT = 0x100;
	public static final int GL_STENCIL_BUFFER_BIT = 0x400;
	public static final int GL_COLOR_BUFFER_BIT =0x4000;

	private OpenGLES2() {
		/* No instances of this class allowed. */
	}

	public static native void glClear(int mask);
	public static native void glClearColor(float red, float green, float blue, float alpha);

	public static native int glGetError();
	public static native @Cast("const uint8_t *") BytePointer glGetString(int nameIdentifier);
	public static native void glViewport (int x, int y, int width, int height);

	public static native int glCreateProgram();
	public static native void glAttachShader(int program, int shader);
	public static native void glDetachShader(int program, int shader);
	public static native int glCreateShader(int shaderType);
	public static native void glDeleteShader(int shader);
	public static native void glShaderSource(int shader, int count, @Cast("const char **") PointerPointer strings, IntPointer length);
	public static native void glGetShaderiv(int shader, int paramName, IntPointer params);
}
