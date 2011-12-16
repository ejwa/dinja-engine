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
import javax.vecmath.Matrix4f;

public class UniformMatrix4f extends AbstractUniform<Matrix4f, FloatPointer> {
	public UniformMatrix4f(String variableName) {
		super(variableName, 16);
	}

	public UniformMatrix4f(String variableName, Matrix4f value) {
		super(variableName, 16, value);
	}

	@Override
	protected synchronized void setData(Matrix4f value) {
		if (data == null) {
			data = NativeMemory.getFloatPointer(data, components);
		}

		for (int i = 0; i < components / 4; i++) {
			for (int j = 0; j < components / 4; j++) {
				data.put(i, value.getElement(i, j));
			}
		}
	}
}
