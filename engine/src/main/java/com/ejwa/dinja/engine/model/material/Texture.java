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
package com.ejwa.dinja.engine.model.material;

import android.graphics.Color;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.ShortPointer;

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
	private ShortPointer pixelsRGB565;
	private IntPointer pixelsRGBA8888;

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
	 * the image. In effect, if set to false, Dinja Engine will fetch color values using {@link #getPixelsRGB565()}.
	 *
	 * @param hasAlpha true if alpha values should be fetched from the texture, otherwise false.
	 */
	public void setHasAlpha(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	/**
	 * Converts the pixel data and returns it in the form RGB565.
	 *
	 * @return A pointer to the pixel data for this texture in the form RGB565.
	 */
	@SuppressWarnings("PMD.AvoidUsingShortType")
	public ShortPointer getPixelsRGB565() {
		if (pixelsRGB565 == null) {
			pixelsRGB565 = new ShortPointer(pixels.length);

			for (int i = 0; i < pixels.length; i++) {
				short pixel = 0;

				pixel |= Color.red(pixels[i]) << 8 & 0xf800;
				pixel |= Color.green(pixels[i]) << 3 & 0x7e0;
				pixel |= Color.blue(pixels[i]) >>> 3 & 0x1f;
				pixelsRGB565.put(i, pixel);
			}
		}

		return pixelsRGB565;
	}

	/**
	 * Converts the pixel data and returns it in the form RGB565.
	 *
	 * @return An array with the pixel data for this texture in the form RGB565.
	 */
	@SuppressWarnings("PMD.AvoidUsingShortType")
	public short[] getPixelsRGB565Array() {
		final ShortPointer pointer = getPixelsRGB565();
		return pointer.asBuffer().array();
	}

	/**
	 * Converts the pixel data and returns it in the form RGBA8888.
	 *
	 * @return A pointer to the pixel data for this texture in the form RGBA8888.
	 */
	public IntPointer getPixelsRGBA8888() {
		if (pixelsRGBA8888 == null) {
			pixelsRGBA8888 = new IntPointer(pixels.length);

			for (int i = 0; i < pixels.length; i++) {
				int pixel = 0;

				pixel |= Color.red(pixels[i]) & 0xff;
				pixel |= Color.green(pixels[i]) << 8 & 0xff00;
				pixel |= Color.blue(pixels[i]) << 16 & 0xff0000;
				pixel |= Color.alpha(pixels[i]) << 24 & 0xff000000;
				pixelsRGBA8888.put(i, pixel);
			}
		}

		return pixelsRGBA8888;
	}

	/**
	 * Converts the pixel data and returns it in the form RGBA8888.
	 *
	 * @return An array with the pixel data for this texture in the form RGBA8888.
	 */
	public int[] getPixelsRGBA8888Array() {
		final IntPointer pointer = getPixelsRGBA8888();
		return pointer.asBuffer().array();
	}
}
