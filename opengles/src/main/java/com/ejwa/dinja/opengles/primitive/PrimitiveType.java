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
package com.ejwa.dinja.opengles.primitive;

import javax.microedition.khronos.opengles.GL10;

public enum PrimitiveType {
	GL_POINTS(GL10.GL_POINTS),
	GL_LINES(GL10.GL_LINES),
	GL_LINE_LOOP(GL10.GL_LINE_LOOP),
	GL_TRIANGLES(GL10.GL_TRIANGLES),
	GL_TRIANGLE_FAN(GL10.GL_TRIANGLE_FAN),
	GL_TRIANGLE_STRIP(GL10.GL_TRIANGLE_STRIP);

	private final int id;
	PrimitiveType(int id) { this.id = id; }

	public int getId() {
		return id;
	}
}
