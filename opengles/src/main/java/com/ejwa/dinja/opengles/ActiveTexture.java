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

import com.ejwa.dinja.opengles.library.OpenGLES2;
import javax.microedition.khronos.opengles.GL10;

public enum ActiveTexture {
	GL_TEXTURE0(GL10.GL_TEXTURE0),
	GL_TEXTURE1(GL10.GL_TEXTURE1),
	GL_TEXTURE2(GL10.GL_TEXTURE2),
	GL_TEXTURE3(GL10.GL_TEXTURE3),
	GL_TEXTURE4(GL10.GL_TEXTURE4),
	GL_TEXTURE5(GL10.GL_TEXTURE5),
	GL_TEXTURE6(GL10.GL_TEXTURE6),
	GL_TEXTURE7(GL10.GL_TEXTURE7),
	GL_TEXTURE8(GL10.GL_TEXTURE8),
	GL_TEXTURE9(GL10.GL_TEXTURE9);

	private final int id;
	ActiveTexture(int id) { this.id = id; }

	public int getId() {
		return id;
	}

	public void set() {
		OpenGLES2.glActiveTexture(id);
	}
}
