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

public enum Capability {
	GL_TEXTURE_2D(GL10.GL_TEXTURE_2D),
	GL_CULL_FACE(GL10.GL_CULL_FACE),
	GL_BLEND(GL10.GL_BLEND),
	GL_DITHER(GL10.GL_DITHER),
	GL_STENCIL_TEST(GL10.GL_STENCIL_TEST),
	GL_DEPTH_TEST(GL10.GL_DEPTH_TEST),
	GL_SCISSOR_TEST(GL10.GL_SCISSOR_TEST),
	GL_POLYGON_OFFSET_FILL(GL10.GL_POLYGON_OFFSET_FILL),
	GL_SAMPLE_ALPHA_TO_COVERAGE(GL10.GL_SAMPLE_ALPHA_TO_COVERAGE),
	GL_SAMPLE_COVERAGE(GL10.GL_SAMPLE_COVERAGE);

	private final int id;
	Capability(int id) { this.id = id; }

	public int getId() {
		return id;
	}

	public void enable() {
		OpenGLES2.glEnable(id);
	}

	public void disable() {
		OpenGLES2.glDisable(id);
	}
}
