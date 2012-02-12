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
package com.ejwa.dinja.engine.model.node.mesh;

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;

public class Vertex implements Cloneable {
	private Colorf color;
	private Vector3f normal;
	private Vector3f position;
	private Vector2f textureCoordinates;

	public Vertex() {
		/* Currently, no need to do anything here. */
	}

	public Vertex(Vector3f position) {
		this();
		this.position = position;
	}

	public Vertex(Vector3f position, Vector3f normal) {
		this(position);
		this.normal = normal;
	}

	public Vertex(Vector3f position, Vector3f normal, Colorf color) {
		this(position, normal);
		this.color = color;
	}

	public Colorf getColor() {
		return color;
	}

	public void setColor(Colorf color) {
		this.color = color;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getTextureCoordinates() {
		return textureCoordinates;
	}

	public void setTextureCoordinates(Vector2f textureCoordinates) {
		this.textureCoordinates = textureCoordinates;
	}

	@Override
	@SuppressWarnings({"PMD.NPathComplexity", "PMD.DataflowAnomalyAnalysis"})
	public int hashCode() {
		int hash = 7;

		hash *= 67 + (color == null ? 0 : color.hashCode());
		hash *= 67 + (normal == null ? 0 : normal.hashCode());
		hash *= 67 + (position == null ? 0 : position.hashCode());
		hash *= 67 + (textureCoordinates == null ? 0 : textureCoordinates.hashCode());

		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vertex) {
			final Vertex v = (Vertex) o;

			if (color != null && (!color.equals(v.color))) {
				return false;
			}

			if (normal != null && (!normal.equals(v.normal))) {
				return false;
			}

			if (position != null && (!position.equals(v.position))) {
				return false;
			}

			if (textureCoordinates != null && (!textureCoordinates.equals(v.textureCoordinates))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Vertex clone() throws CloneNotSupportedException {
		return (Vertex) super.clone();
	}
}
