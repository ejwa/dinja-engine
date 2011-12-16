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
package com.ejwa.dinja.opengles.library;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Platform;
import javax.microedition.khronos.opengles.GL10;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.UseSingleton"})
@Platform(cinclude = "GLES2/gl2.h", link = "GLESv2")
class OpenGLES2Native {
	static { Loader.load(); }

	protected OpenGLES2Native() {
		/* Only inheriting classes can make instances of this class. */
	}

	public static final int GL_ES_VERSION_2_0 = 1;
	public static final int GL_DEPTH_BUFFER_BIT = GL10.GL_DEPTH_BUFFER_BIT;
	public static final int GL_STENCIL_BUFFER_BIT = GL10.GL_STENCIL_BUFFER_BIT;
	public static final int GL_COLOR_BUFFER_BIT = GL10.GL_COLOR_BUFFER_BIT;

	public static native void glClear(int mask);
	public static native void glClearColor(float red, float green, float blue, float alpha);

	public static native int glGetError();
	public static native @Cast("const uint8_t *") BytePointer glGetString(int nameIdentifier);
	public static native void glViewport(int x, int y, int width, int height);

	public static native int glCreateProgram();
	public static native void glDeleteProgram(int program);
	public static native void glLinkProgram(int program);
	public static native void glUseProgram(int program);
	public static native void glAttachShader(int program, int shader);
	public static native void glDetachShader(int program, int shader);
	protected static native void glGetProgramiv(int program, int paramName, IntPointer params);
	protected static native void glGetProgramInfoLog(int program, int bufferSize, IntPointer length, @Cast("char *") BytePointer infoLog);

	public static native int glCreateShader(int shaderType);
	public static native void glDeleteShader(int shader);
	protected static native void glShaderSource(int shader, int count, @Cast("const char **") PointerPointer strings, IntPointer length);
	public static native void glCompileShader(int shader);
	protected static native void glGetShaderiv(int shader, int paramName, IntPointer params);
	protected static native void glGetShaderInfoLog(int shader, int bufferSize, IntPointer length, @Cast("char *") BytePointer infoLog);

	protected static native void glDrawElements(int mode, int count, int type, Pointer indices);

	protected static native void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, Pointer pointer);
	public static native void glEnableVertexAttribArray(int index);
	public static native void glDisableVertexAttribArray(int index);
	public static native int glGetAttribLocation(int program, String name);

	public static native int glGetUniformLocation(int program, String name);
}
