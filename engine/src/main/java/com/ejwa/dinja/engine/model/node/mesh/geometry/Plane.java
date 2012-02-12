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
package com.ejwa.dinja.engine.model.node.mesh.geometry;

import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.model.node.mesh.Vertex;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import java.util.LinkedList;
import java.util.List;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;

/**
 * Describes a plane geometry which is a flat rectangle mesh with no depth. If you need depth, there is also a {@link Cube}
 * geometry class. The plane geometry uses triangle strips. For example, a 4-segment plane is constructed in the following
 * manner:
 * <p>
 * <div style="text-align: center;"><img src="doc-files/plane_construction.svg" /></div>
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public class Plane extends Mesh {
	/**
	 * Creates a plane geometry complete with defined properties such as texture coordinates. This constructor defaults
	 * to creating a plane with a single segment.
	 *
	 * @param name The name of the plane (and mesh).
	 * @param width The width of the plane.
	 * @param height The height of the plane.
	 */
	public Plane(String name, float width, float height) {
		this(name, width, height, 1);
	}

	/**
	 * Creates a plane geometry complete with defined properties such as texture coordinates.
	 *
	 * @param name The name of the plane (and mesh).
	 * @param width The width of the plane.
	 * @param height The height of the plane.
	 * @param segments The number of segments on this plane. The plane needs to have at least 1 segment.
	 */
	public Plane(String name, float width, float height, int segments) {
		this(name, width, height, segments, true);
	}

	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	protected Plane(String name, float width, float height, int segments, boolean updatePrimitiveData) {
		super(name, PrimitiveType.GL_TRIANGLE_STRIP);

		if (segments == 0) {
			throw new IllegalArgumentException("The number of segments has to be at least 1.");
		}

		final float xs = width / segments;
		final float ys = height / segments;
		final float ts = 1f / segments;

		for (int y = 0; y < segments; y++) {
			final List<Vertex> indices = new LinkedList<Vertex>();

			for (int x = 0; x <= segments; x++) {
				final float xpCurrent = -width / 2 + x * xs;
				final float ypCurrent = height / 2 - y * ys;
				final float ypNext = height / 2 - (y + 1) * ys;

				final Vertex top = new Vertex(new Vector3f(xpCurrent, ypNext, 0));
				Vertex bottom = new Vertex(new Vector3f(xpCurrent, ypCurrent, 0));

				top.setTextureCoordinates(new Vector2f(x * ts, (y + 1) * ts));
				bottom.setTextureCoordinates(new Vector2f(x * ts, y * ts));

				/*
				 * If there is a match, we scrap the new vertex and re-use the old vertex that should
				 * be there...
				 */
				if (getVertices().indexOf(bottom) == -1) {
					addVertices(bottom, top);
				} else {
					bottom = getVertices().get(getVertices().indexOf(bottom));
					addVertices(top);
				}

				/*
				 * On even rows, indices are added from left to right in [bottom, top] pairs. On odd rows,
				 * indices are added from right to left in [top, bottom] pairs.
				 */
				if (y % 2 == 0) {
					indices.add(bottom);
					indices.add(top);
				} else {
					indices.add(0, bottom);
					indices.add(0, top);
				}
			}

			addIndices(indices.toArray(new Vertex[indices.size()]));
		}

		if (updatePrimitiveData) {
			getMeshPrimitiveData().update();
		}
	}
}
