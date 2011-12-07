package com.ejwa.dinja.opengles;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.PointerPointer;

public final class OpenGLES2 extends OpenGLES2Native {
	private OpenGLES2() {
		super();
		/* No instances of this class allowed. */
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
}
