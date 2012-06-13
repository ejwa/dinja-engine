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
package com.ejwa.dinja.engine.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.ejwa.dinja.engine.model.file.FileResourceException;
import com.ejwa.dinja.engine.model.material.Texture;
import java.io.IOException;

/**
 * Provides the ability to load textures.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public final class TextureLoader {
	private TextureLoader() {
		/* No instances of this class allowed. */
	}

	/**
	 * Creates a {@link Texture} from a given asset.
	 *
	 * @param assetManager An android AssetManager supplied from an activity or similar.
	 * @param fileName Name of the texture file (asset) to load.
	 * @return The created texture.
	 */
	@SuppressWarnings("PMD.ProtectLogD")
	@edu.umd.cs.findbugs.annotations.SuppressWarnings({"EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS", "REC_CATCH_EXCEPTION"})
	public static Texture load(AssetManager assetManager, String fileName) {
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(fileName));

			if (bitmap == null) {
				throw new IOException("Couldn't interpret file.");
			}

			final int pixels[] = new int[bitmap.getWidth() * bitmap.getHeight()];
			final boolean hasAlpha = bitmap.getConfig() == Bitmap.Config.ARGB_8888 ? true : false;

			bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

			Log.d(TextureLoader.class.getName(), String.format("Loaded %dx%d bitmap \"%s\" in format %s.",
			      bitmap.getWidth(), bitmap.getHeight(), fileName, bitmap.getConfig()));
			bitmap.recycle();

			return new Texture(bitmap.getWidth(), bitmap.getHeight(), hasAlpha, pixels);
		} catch (IOException ex) {
			throw new FileResourceException(String.format("Failed to load texture '%s' (%s).",
			                                fileName, ex.getMessage()), ex);
		}
	}
}
