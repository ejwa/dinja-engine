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
import com.ejwa.dinja.opengles.texture.PixelStorageMode;
import com.ejwa.dinja.opengles.texture.TextureFormat;
import com.ejwa.dinja.opengles.texture.TextureMagnifyFilter;
import com.ejwa.dinja.opengles.texture.TextureMinifyFilter;
import com.ejwa.dinja.opengles.texture.TextureTarget;
import com.ejwa.dinja.opengles.texture.TextureType;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Pointer;

public abstract class AbstractSampler<T extends Pointer> extends AbstractUniform<T, T> {
	protected final ActiveTexture activeTexture;
	protected final TextureFormat textureFormat;
	protected final TextureType textureType;
	protected final int width;
	protected final int height;
	protected int textureHandle;

	public AbstractSampler(String textureName, ActiveTexture activeTexture, TextureFormat textureFormat,
	                       TextureType textureType, int width, int height, T pixels) {
		super(textureName, width * height, pixels);
		this.activeTexture = activeTexture;
		this.textureFormat = textureFormat;
		this.textureType = textureType;
		this.width = width;
		this.height = height;
	}

	private static int genTexture() {
		final IntPointer handlePtr = new IntPointer(1);
		final int handle;

		OpenGLES2Native.glGenTextures(1, handlePtr);
		handle = handlePtr.get(0);
		handlePtr.deallocate();

		return handle;
	}

	private void texImage2D(int level) {
		OpenGLES2Native.glTexImage2D(TextureTarget.GL_TEXTURE_2D.getId(), level, textureFormat.getId(), width, height,
		                             0, textureFormat.getId(), textureType.getId(), data);
	}

	public void bind() {
		textureHandle = genTexture();
		activeTexture.set();
		TextureTarget.GL_TEXTURE_2D.bind(textureHandle);
		PixelStorageMode.GL_UNPACK_ALIGNMENT.set(1);
		texImage2D(0);

		TextureMagnifyFilter.GL_LINEAR.set(TextureTarget.GL_TEXTURE_2D);
		TextureMinifyFilter.GL_LINEAR.set(TextureTarget.GL_TEXTURE_2D);
	}

	@Override
	public void send(int handle) {
		activeTexture.set();
		TextureTarget.GL_TEXTURE_2D.bind(textureHandle);
		OpenGLES2Native.glUniform1i(handle, 0);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
