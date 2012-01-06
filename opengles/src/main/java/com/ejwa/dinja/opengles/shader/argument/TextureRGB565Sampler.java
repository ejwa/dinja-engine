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
import com.ejwa.dinja.opengles.PixelStorageMode;
import com.ejwa.dinja.opengles.TextureFormat;
import com.ejwa.dinja.opengles.TextureMagnifyFilter;
import com.ejwa.dinja.opengles.TextureMinifyFilter;
import com.ejwa.dinja.opengles.TextureTarget;
import com.ejwa.dinja.opengles.TextureType;
import com.ejwa.dinja.opengles.library.NativeMemory;
import com.ejwa.dinja.opengles.library.OpenGLES2;
import com.googlecode.javacpp.ShortPointer;
import org.apache.commons.lang.ArrayUtils;

@SuppressWarnings("PMD.AvoidUsingShortType")
public class TextureRGB565Sampler extends AbstractSampler<Short[], ShortPointer> {
	private int textureHandle;

	public TextureRGB565Sampler(String textureName, ActiveTexture activeTexture, int width, int height, short ...pixels) {
		super(textureName, activeTexture, width, height, ArrayUtils.toObject(pixels));
	}

	@Override
	public void bind() {
		textureHandle = OpenGLES2.glGenTexture();
		activeTexture.set();
		OpenGLES2.glBindTexture(TextureTarget.GL_TEXTURE_2D, textureHandle);
		PixelStorageMode.GL_UNPACK_ALIGNMENT.set(1);
		OpenGLES2.glTexImage2D(0, width, height, TextureFormat.GL_RGB, TextureType.GL_UNSIGNED_SHORT_5_6_5, data);

		TextureMagnifyFilter.GL_LINEAR.set(TextureTarget.GL_TEXTURE_2D);
		TextureMinifyFilter.GL_LINEAR.set(TextureTarget.GL_TEXTURE_2D);
	}

	@Override
	public void send(int handle) {
		activeTexture.set();
		OpenGLES2.glBindTexture(TextureTarget.GL_TEXTURE_2D, textureHandle);
		OpenGLES2.glUniform1i(handle, 0);
	}

	@Override
	protected void setData(Short[] values) {
		if (data == null) {
			data = NativeMemory.getShortPointer(data, values.length);
		}

		for (int i = 0; i < values.length; i++) {
			data.put(i, values[i]);
		}
	}
}
