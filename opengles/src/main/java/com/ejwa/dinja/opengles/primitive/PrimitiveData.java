/*
 * Copyright © 2011 Ejwa Software. All rights reserved.
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
package com.ejwa.dinja.opengles.primitive;

import com.ejwa.dinja.opengles.GLException;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.ShortPointer;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class PrimitiveData {
	private final PrimitiveType primitiveType;
	private FloatPointer colors;
	private final Pointer indices;
	private FloatPointer normals;
	private FloatPointer textureCoords;
	private final FloatPointer vertices;

	public PrimitiveData(PrimitiveType primitiveType, Vector3f[] vertices, Integer ...indices) {
		this.primitiveType = primitiveType;
		this.vertices = new FloatPointer(vertices.length * 3);

		for (int i = 0; i < vertices.length * 3; i += 3) {
			this.vertices.put(i, vertices[i].x);
			this.vertices.put(i, vertices[i].y);
			this.vertices.put(i, vertices[i].z);
		}

		if (this.vertices.capacity() < 256) {
			this.indices = new BytePointer(indices.length);
		} else {
			this.indices = new ShortPointer(indices.length);
		}

		for (int i = 0; i < indices.length; i++) {
			if (indices[i] >= this.vertices.capacity()) {
				throw new GLException(String.format("Indices must point within bounds of vertices (got %d, " +
				                                     "but maximum is %d)", indices[i], this.vertices.capacity()));
			}
		}
	}

	private void validate(Pointer pointer, int length) {
		if (pointer != null) {
			pointer.deallocate();
		}

		if (length != vertices.capacity()) {
			throw new GLException(String.format("Attributes such as normals, colors and texture coordinates must " +
			                                    "match with the number of vertices (got %d, but expected %d).",
			                                    length, vertices.capacity()));
		}
	}

	public FloatPointer getColors() {
		return colors;
	}

	public void setColors(Color4f ...colors) {
		validate(this.colors, colors.length);
		this.colors = new FloatPointer(colors.length);

		for (int i = 0; i < colors.length * 2; i += 4) {
			this.colors.put(i, colors[i].x);
			this.colors.put(i, colors[i].y);
			this.colors.put(i, colors[i].z);
			this.colors.put(i, colors[i].w);
		}
	}

	public Pointer getIndices() {
		return indices;
	}

	public FloatPointer getNormals() {
		return normals;
	}

	public void setNormals(Vector3f ...normals) {
		validate(this.colors, normals.length);
		this.colors = new FloatPointer(normals.length);

		for (int i = 0; i < normals.length * 2; i += 3) {
			this.normals.put(i, normals[i].x);
			this.normals.put(i, normals[i].y);
			this.normals.put(i, normals[i].z);
		}
	}

	public FloatPointer getTextureCoords() {
		return textureCoords;
	}

	public void setTextureCoords(Vector2f ...textureCoords) {
		validate(this.textureCoords, textureCoords.length);
		this.colors = new FloatPointer(textureCoords.length);

		for (int i = 0; i < textureCoords.length * 2; i += 2) {
			this.textureCoords.put(i, textureCoords[i].x);
			this.textureCoords.put(i, textureCoords[i].y);
		}
	}

	public FloatPointer getVertices() {
		return vertices;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	@Override
	protected void finalize() throws Throwable {
		if (colors != null) { colors.deallocate(); }
		if (indices != null) { indices.deallocate(); }
		if (textureCoords != null) { textureCoords.deallocate(); }
		if (vertices != null) { vertices.deallocate(); }

		super.finalize();
	}
}
