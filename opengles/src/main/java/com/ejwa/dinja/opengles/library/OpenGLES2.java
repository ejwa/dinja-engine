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

import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import javax.microedition.khronos.opengles.GL10;

public final class OpenGLES2 extends OpenGLES2Native {
	private OpenGLES2() {
		super(); /* No instances of this class allowed. */
	}

	public static int glGetProgramiv(int program, int paramName) {
		final IntPointer parameterPtr = new IntPointer(1);
		OpenGLES2Native.glGetProgramiv(program, paramName, parameterPtr);

		final int parameter = parameterPtr.get();
		parameterPtr.deallocate();
		return parameter;
	}

	public static String glGetProgramInfoLog(int program) {
		final BytePointer infoLogPtr = new BytePointer(256);
		final IntPointer length = new IntPointer(1);

		OpenGLES2Native.glGetProgramInfoLog(program, infoLogPtr.capacity(), length, infoLogPtr);

		final String infoLog = infoLogPtr.getString();
		infoLogPtr.deallocate();
		length.deallocate();

		return infoLog;
	}

	public static void glShaderSource(int shader, String source) {
		final BytePointer sourcePtr = new BytePointer(source);
		final PointerPointer sourcePtrPtr =  new PointerPointer(new BytePointer[] { sourcePtr });

		OpenGLES2Native.glShaderSource(shader, 1, sourcePtrPtr, null);
		sourcePtrPtr.deallocate();
		sourcePtr.deallocate();
	}

	public static int glGetShaderiv(int shader, int paramName) {
		final IntPointer parameterPtr = new IntPointer(1);
		OpenGLES2Native.glGetShaderiv(shader, paramName, parameterPtr);

		final int parameter = parameterPtr.get();
		parameterPtr.deallocate();
		return parameter;
	}

	public static String glGetShaderInfoLog(int shader) {
		final BytePointer infoLogPtr = new BytePointer(256);
		final IntPointer length = new IntPointer(1);

		OpenGLES2Native.glGetShaderInfoLog(shader, infoLogPtr.capacity(), length, infoLogPtr);

		final String infoLog = infoLogPtr.getString();
		infoLogPtr.deallocate();
		length.deallocate();

		return infoLog;
	}

	public static void glDrawElements(PrimitiveData primitiveData) {
		final Pointer indices =  primitiveData.getIndices();
		final int type = primitiveData.getVertices().capacity() <= 256 ? GL10.GL_UNSIGNED_BYTE : GL10.GL_UNSIGNED_SHORT;

		OpenGLES2Native.glDrawElements(primitiveData.getPrimitiveType().getId(), indices.capacity(), type, indices);
	}
}
