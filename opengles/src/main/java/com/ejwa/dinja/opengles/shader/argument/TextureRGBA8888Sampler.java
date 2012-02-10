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
import com.ejwa.dinja.opengles.library.NativeMemory;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Pointer;
import org.apache.commons.lang.ArrayUtils;

public class TextureRGBA8888Sampler extends AbstractSampler<Integer[], Pointer> {
	public TextureRGBA8888Sampler(String textureName, ActiveTexture activeTexture, int width, int height, int ...pixels) {
		super(textureName, activeTexture, TextureFormat.GL_RGBA, TextureType.GL_UNSIGNED_BYTE,
		      width, height, ArrayUtils.toObject(pixels));
	}

	@Override
	protected void setData(Integer[] values) {
		if (data == null) {
			data = NativeMemory.getIntPointer(data, values.length);
		}

		for (int i = 0; i < values.length; i++) {
			((IntPointer) data).put(i, values[i]);
		}
	}
}
