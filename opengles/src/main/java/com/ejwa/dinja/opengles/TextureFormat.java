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
package com.ejwa.dinja.opengles;

import javax.microedition.khronos.opengles.GL10;

public enum TextureFormat {
	GL_ALPHA(GL10.GL_ALPHA),
	GL_RGB(GL10.GL_RGB),
	GL_RGBA(GL10.GL_RGBA),
	GL_LUMINANCE(GL10.GL_LUMINANCE),
	GL_LUMINANCE_ALPHA(GL10.GL_LUMINANCE_ALPHA);

	private final int id;
	TextureFormat(int id) { this.id = id; }

	public int getId() {
		return id;
	}
}
