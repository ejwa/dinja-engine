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
package com.ejwa.dinja.opengles.library;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Platform;

/**
 * The main class for all native OpenGL ES 2 bindings. The class is intended to be used by the classes in this layer and should
 * in most cases only be called directly from within this layer. This class is static and can not be instantiated.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.UseSingleton"})
@Platform(cinclude = "GLES2/gl2.h", link = "GLESv2")
public final class OpenGLES2Native {
	static { Loader.load(); }

	private OpenGLES2Native() {
		/* No instances of this class allowed. */
	}

	public static final int GL_ES_VERSION_2_0 = 1;

	public static native void glEnable(int cap);
	public static native void glDisable(int cap);
	public static native void glCullFace(int mode);

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
	public static native void glGetProgramiv(int program, int paramName, IntPointer params);
	public static native void glGetProgramInfoLog(int program, int bufferSize, IntPointer length, @Cast("char *") BytePointer infoLog);

	public static native int glCreateShader(int shaderType);
	public static native void glDeleteShader(int shader);
	public static native void glShaderSource(int shader, int count, @Cast("const char **") PointerPointer strings, IntPointer length);
	public static native void glCompileShader(int shader);
	public static native void glGetShaderiv(int shader, int paramName, IntPointer params);
	public static native void glGetShaderInfoLog(int shader, int bufferSize, IntPointer length, @Cast("char *") BytePointer infoLog);

	public static native void glDrawElements(int mode, int count, int type, Pointer indices);

	public static native void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, Pointer pointer);
	public static native void glEnableVertexAttribArray(int index);
	public static native void glDisableVertexAttribArray(int index);
	public static native int glGetAttribLocation(int program, String name);

	public static native int glGetUniformLocation(int program, String name);
	public static native void glUniform4fv(int location, int count, FloatPointer value);
	public static native void glUniformMatrix4fv(int location, int count, boolean transpose, FloatPointer value);
	public static native void glUniform1i(int location, int value);
	public static native void glUniform3f(int location, float v0, float v1, float v2);

	public static native void glActiveTexture(int texture);
	public static native void glTexParameteri(int target, int paramName, int param);
	public static native void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, Pointer pixels);
	public static native void glBindTexture(int target, int texture);
	public static native void glGenTextures(int n, @Cast("unsigned int *") IntPointer textures);
	public static native void glPixelStorei(int paramName, int param);

	public static native void glBlendFunc(int sourceFactor, int destinationFactor);

	public static native void glBindBuffer(int target, int buffer);
	public static native void glGenBuffers(int n, @Cast("unsigned int *") IntPointer buffers);
}
