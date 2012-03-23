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
package com.ejwa.dinja.opengles;

import javax.microedition.khronos.opengles.GL10;

public enum UniformDataType {
	GL_BYTE(GL10.GL_BYTE),
	GL_UNSIGNED_BYTE(GL10.GL_UNSIGNED_BYTE),
	GL_SHORT(GL10.GL_SHORT),
	GL_UNSIGNED_SHORT(GL10.GL_UNSIGNED_SHORT),
	GL_INT(DataType.GL_INT.getId()),
	GL_UNSIGNED_INT(DataType.GL_UNSIGNED_INT.getId()),
	GL_FLOAT(GL10.GL_FLOAT),
	GL_FIXED(GL10.GL_FIXED),
	GL_FLOAT_VEC2(0x8b50),
	GL_FLOAT_VEC3(0x8b51),
	GL_FLOAT_VEC4(0x8b52),
	GL_INT_VEC2(0x8b53),
	GL_INT_VEC3(0x8b54),
	GL_INT_VEC4(0x8b55),
	GL_BOOL(0x8b56),
	GL_BOOL_VEC2(0x8b57),
	GL_BOOL_VEC3(0x8b58),
	GL_BOOL_VEC4(0x8b59),
	GL_FLOAT_MAT2(0x8b50),
	GL_FLOAT_MAT3(0x8b51),
	GL_FLOAT_MAT4(0x8b52),
	GL_SAMPLER_2D(0x8b5e),
	GL_SAMPLER_CUBE(0x8b60);

	private final int id;
	UniformDataType(int id) { this.id = id; }

	public int getId() {
		return id;
	}
}
