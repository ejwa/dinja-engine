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
package com.ejwa.dinja.opengles.shader.argument;

import com.ejwa.dinja.opengles.library.NativeMemory;
import com.googlecode.javacpp.FloatPointer;
import javax.vecmath.Tuple4f;

public class Tuple4fVertexAttributeArray extends AbstractVertexAttributeArray<Tuple4f, FloatPointer> {
	public Tuple4fVertexAttributeArray(String variableName) {
		super(variableName, 4);
	}

	public Tuple4fVertexAttributeArray(String variableName, Tuple4f ...values) {
		super(variableName, 4, values);
	}

	@Override
	public synchronized void setData(Tuple4f... values) {
		data = NativeMemory.getFloatPointer(data, values.length * components);

		for (int i = 0; i < values.length; i++) {
			data.put((i * components), values[i].x);
			data.put((i * components) + 1, values[i].y);
			data.put((i * components) + 2, values[i].z);
			data.put((i * components) + 3, values[i].w);
		}
	}
}
