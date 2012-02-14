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
package com.ejwa.dinja.opengles.texture;

import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import javax.microedition.khronos.opengles.GL10;

public enum TextureMagnifyFilter {
	GL_NEAREST(GL10.GL_NEAREST),
	GL_LINEAR(GL10.GL_LINEAR);

	private final int id;
	TextureMagnifyFilter(int id) { this.id = id; }

	public void set(TextureTarget textureTarget) {
		OpenGLES2Native.glTexParameteri(textureTarget.getId(), GL10.GL_TEXTURE_MAG_FILTER, id);
	}
}
