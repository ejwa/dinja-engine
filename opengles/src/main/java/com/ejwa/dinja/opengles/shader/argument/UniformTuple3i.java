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
package com.ejwa.dinja.opengles.shader.argument;

import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import org.openmali.vecmath2.Tuple3i;

public class UniformTuple3i extends AbstractUniform<Tuple3i, Tuple3i> {
	public UniformTuple3i(String variableName) {
		super(variableName, 3);
	}

	public UniformTuple3i(String variableName, Tuple3i value) {
		super(variableName, 3, value);
	}

	@Override
	protected synchronized void setData(Tuple3i value) {
		if (data == null) {
			data = new Tuple3i();
		}

		data.set(value.getX(), value.getY(), value.getZ());
	}

	@Override
	public void send(int handle) {
		OpenGLES2Native.glUniform3i(handle, data.getX(), data.getY(), data.getZ());
	}
}
