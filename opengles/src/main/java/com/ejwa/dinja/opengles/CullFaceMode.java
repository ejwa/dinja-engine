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

import com.ejwa.dinja.opengles.library.OpenGLES2;
import javax.microedition.khronos.opengles.GL10;

public enum CullFaceMode {
	GL_FRONT(GL10.GL_FRONT),
	GL_BACK(GL10.GL_BACK),
	GL_FRONT_AND_BACK(GL10.GL_FRONT_AND_BACK);

	private final int id;
	CullFaceMode(int id) { this.id = id; }

	public void set() {
		OpenGLES2.glCullFace(id);
	}
}
