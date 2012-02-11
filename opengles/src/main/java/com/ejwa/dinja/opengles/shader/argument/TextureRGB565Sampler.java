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

import com.ejwa.dinja.opengles.ActiveTexture;
import com.ejwa.dinja.opengles.TextureFormat;
import com.ejwa.dinja.opengles.TextureType;
import com.googlecode.javacpp.ShortPointer;

@SuppressWarnings("PMD.AvoidUsingShortType")
public class TextureRGB565Sampler extends AbstractSampler<ShortPointer> {
	public TextureRGB565Sampler(String textureName, ActiveTexture activeTexture, int width, int height, ShortPointer pixels) {
		super(textureName, activeTexture, TextureFormat.GL_RGB, TextureType.GL_UNSIGNED_SHORT_5_6_5, width, height, pixels);
	}

	@Override
	protected void setData(ShortPointer pixels) {
		data = pixels;
	}
}
