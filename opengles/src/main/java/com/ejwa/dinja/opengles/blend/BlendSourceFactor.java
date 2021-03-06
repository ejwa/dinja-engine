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
package com.ejwa.dinja.opengles.blend;

import javax.microedition.khronos.opengles.GL10;

public enum BlendSourceFactor {
	GL_ZERO(GL10.GL_ZERO),
	GL_ONE(GL10.GL_ONE),
	GL_DST_COLOR(GL10.GL_DST_COLOR),
	GL_ONE_MINUS_DST_COLOR(GL10.GL_ONE_MINUS_DST_COLOR),
	GL_SRC_ALPHA(GL10.GL_SRC_ALPHA),
	GL_ONE_MINUS_SRC_ALPHA(GL10.GL_ONE_MINUS_SRC_ALPHA),
	GL_DST_ALPHA(GL10.GL_DST_ALPHA),
	GL_ONE_MINUS_DST_ALPHA(GL10.GL_ONE_MINUS_DST_ALPHA),
	GL_SRC_ALPHA_SATURATE(GL10.GL_SRC_ALPHA_SATURATE);

	private final int id;
	BlendSourceFactor(int id) { this.id = id; }

	public int getId() {
		return id;
	}
}
