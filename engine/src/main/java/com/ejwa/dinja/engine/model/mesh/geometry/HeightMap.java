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
package com.ejwa.dinja.engine.model.mesh.geometry;

import com.ejwa.dinja.engine.model.mesh.Texture;
import com.ejwa.dinja.engine.model.mesh.Vertex;
import com.ejwa.dinja.engine.util.HashedArrayList;

/**
 * Describes a heightmap geometry which actually is a {@link Plane} with vertices translated in the Z axis according to a
 * heightmap image.
 * <p>
 * The number of segments in a heightmap depends on the resolution of the heightmap image. For example, if a 64 * 64
 * heightmap image is used, the resulting <code>HeightMap</code> geometry will have 63 segments (64 * 64 = 4096) vertices.
 * <p>
 * Ideally, The image used as a heightmap should be a greyscale PNG image. A color image can also be used, but please note that
 * heightmap values will then only be fetched from the red channel of the image.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public class HeightMap extends Plane {
	/**
	 * Creates a heightmap geometry complete with defined properties such as texture coordinates.
	 *
	 * @param name The name of the heightmap (and mesh).
	 * @param heightMapTexture The heightmap texture to be used for the plane displacement.
	 * @param width The width of the heightmap.
	 * @param height The height of the heightmap.
	 * @param depth The height of the heightmap.
	 */
	public HeightMap(String name, Texture heightMapTexture, float width, float height, float depth) {
		super(name, width, height, heightMapTexture.getHeight() - 1, false);

		final int pixels[] = heightMapTexture.getPixelsRGBA8888();
		int i = 0;

		for (Vertex v : getVertices()) {
			int pixel;

			/*
			 * The first two rows require special treatment because they are stored as pairs in the begining of
			 * the vertex array.
			 */
			if (i < heightMapTexture.getWidth() * 2) {
				pixel = pixels[i % 2 == 0 ? i / 2 : heightMapTexture.getWidth() + i / 2];
			} else {
				pixel = pixels[i];
			}

			v.getPosition().z = depth * (pixel & 0xff) / 255f;
			i++;
		}

		((HashedArrayList) getVertices()).update();
		updatePrimitiveData();
	}
}
