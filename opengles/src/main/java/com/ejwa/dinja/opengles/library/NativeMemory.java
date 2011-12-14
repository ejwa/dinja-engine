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
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.ShortPointer;

public final class NativeMemory extends OpenGLES2Native {
	private NativeMemory() {
		super(); /* No instances of this class allowed. */
	}

	private static void checkPointer(Pointer pointer) {
		if (pointer != null) {
			pointer.deallocate();
		}
	}

	public static BytePointer getBytePointer(Pointer pointer, int size) {
		checkPointer(pointer);
		return new BytePointer(size);
	}

	public static FloatPointer getFloatPointer(Pointer pointer, int size) {
		checkPointer(pointer);
		return new FloatPointer(size);
	}

	public static ShortPointer getShortPointer(Pointer pointer, int size) {
		checkPointer(pointer);
		return new ShortPointer(size);
	}
}
