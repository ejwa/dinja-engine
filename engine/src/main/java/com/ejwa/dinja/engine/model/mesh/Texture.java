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
package com.ejwa.dinja.engine.model.mesh;

import android.graphics.Color;

/**
 * Describes texture data to be used together with a mesh.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public class Texture {
	private final int width;
	private final int height;
	private boolean hasAlpha;
	private final int pixels[];

	/**
	 * Creates a texture with the given width, height and pixel data.
	 *
	 * @param width The width of the texture. Has to be a multiple of 2.
	 * @param height The height of the texture. Has to be a multiple of 2.
	 * @param hasAlpha Specify true if the pixel-data has usable alpha values. Otherwise, specify false.
	 * @param pixels An array of pixels, Each <code>int</code> in the array describes pixel data in the form ARGB, with eight
	 *               bits for each color component.
	 */
	public Texture(int width, int height, boolean hasAlpha, int ...pixels) {
		this.width = width;
		this.height = height;

		if (width % 2 != 0 || height % 2 != 0) {
			throw new IllegalArgumentException("Texture dimensions are not a multiple of 2.");
		}

		this.hasAlpha = hasAlpha;
		this.pixels = pixels;
	}

	/**
	 * Returns the current width of this texture.
	 *
	 * @return The width of this texture.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the current height of this texture.
	 *
	 * @return The height of this texture.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns if the current texture has valid alpha values.
	 *
	 * @return true if the texture has valid alpha values.
	 */
	public boolean isHasAlpha() {
		return hasAlpha;
	}

	/**
	 * Sets if the current texture has valid alpha values. This value is used by Dinja Engine to check if the current
	 * texture has valid alpha values or not. Set it to false on a image with alpha values to ignore the alpha channel of
	 * the image. In effect, if set to false, Dinja Engine will fetch color values using {@link #getPixelsRGB565}.
	 *
	 * @param hasAlpha true if alpha values should be fetched from the texture, otherwise false.
	 */
	public void setHasAlpha(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	/**
	 * Converts the pixel data and returns it in the form RGB565, suitable for use together with {@link TextureFormat#GL_RGB}
	 * and {@link TextureType#GL_UNSIGNED_SHORT_5_6_5}.
	 *
	 * @return Pixel data for this texture in the form RGB565.
	 * @see TextureFormat#GL_RGB
	 * @see TextureType#GL_UNSIGNED_SHORT_5_6_5
	 */
	@SuppressWarnings("PMD.AvoidUsingShortType")
	public short[] getPixelsRGB565() {
		final short[] convertedPixels = new short[pixels.length];

		for (int i = 0; i < pixels.length; i++) {
			convertedPixels[i] |= Color.red(pixels[i]) << 8 & 0xf800;
			convertedPixels[i] |= Color.green(pixels[i]) << 3 & 0x7e0;
			convertedPixels[i] |= Color.blue(pixels[i]) >>> 3 & 0x1f;
		}

		return convertedPixels;
	}

	/**
	 * Converts the pixel data and returns it in the form RGBA8888, suitable for use together with {@link TextureFormat#GL_RGBA}
	 * and {@link TextureType#GL_UNSIGNED_BYTE}.
	 *
	 * @return Pixel data for this texture in the form RGBA8888.
	 * @see TextureFormat#GL_RGBA
	 * @see TextureType#GL_UNSIGNED_BYTE
	 */
	public int[] getPixelsRGBA8888() {
		final int[] convertedPixels = new int[pixels.length];

		for (int i = 0; i < pixels.length; i++) {
			convertedPixels[i] |= Color.red(pixels[i]) & 0xff;
			convertedPixels[i] |= Color.green(pixels[i]) << 8 & 0xff00;
			convertedPixels[i] |= Color.blue(pixels[i]) << 16 & 0xff0000;
			convertedPixels[i] |= Color.alpha(pixels[i]) << 24 & 0xff000000;
		}

		return convertedPixels;
	}
}
