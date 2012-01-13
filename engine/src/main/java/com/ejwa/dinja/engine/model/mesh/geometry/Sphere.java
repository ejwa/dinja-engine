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

import android.util.FloatMath;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import com.ejwa.dinja.engine.model.mesh.Vertex;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Describes a sphere geometry with a number of rings and segments. The sphere geometry uses triangle strips. The strip is
 * constructed from the bottom up in a similar way that an orange could be peeled. For example, an n-ring, 8 segment sphere is
 * constructed in the following manner:
 * <p>
 * <div style="text-align: center;"><img src="doc-files/sphere_construction.svg" /></div>
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public class Sphere extends Mesh {
	/**
	 * Creates a sphere geometry complete with defined properties such as texture coordinates.
	 *
	 * @param name The name of the sphere (and mesh).
	 * @param radius The radius of the sphere.
	 * @param rings The number of rings in the sphere. The number of rings is the number of horizontal rows used to
	 *              construct the sphere.
	 * @param segments The number of segments in this sphere. The number of segments specifies the number of vertical
	 *                 sections on each row or horizontal ring.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public Sphere(String name, float radius, int rings, int segments) {
		super(name, PrimitiveType.GL_TRIANGLE_STRIP);

		final float deltaRingAngle = (float) Math.PI / rings;
		final float deltaSegmentAngle = (float) (2 * Math.PI / segments);
		final float tx = 1f / segments;
		final float ty = 1f / rings;

		for (int i = 0; i <= rings; i++) {
			final float r = radius * FloatMath.sin(i * deltaRingAngle);
			final float y = radius * FloatMath.cos(i * deltaRingAngle);
			final float rNext = radius * FloatMath.sin((i + 1) * deltaRingAngle);
			final float yNext = radius * FloatMath.cos((i + 1) * deltaRingAngle);

			for (int j = 0; j <= segments; j++) {
				final float x = r * FloatMath.sin(j * deltaSegmentAngle);
				final float z = r * FloatMath.cos(j * deltaSegmentAngle);
				final float xNext = rNext * FloatMath.sin(j * deltaSegmentAngle);
				final float zNext = rNext * FloatMath.cos(j * deltaSegmentAngle);

				final Vertex top = new Vertex(new Vector3f(xNext, yNext, zNext));
				Vertex bottom = new Vertex(new Vector3f(x, y, z));

				top.setTextureCoordinates(new Vector2f(j * tx, (i + 1) * ty));
				bottom.setTextureCoordinates(new Vector2f(j * tx, i * ty));

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

				addIndices(bottom, top);
			}
		}

		updatePrimitiveData();
	}
}
