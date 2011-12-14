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
import com.ejwa.dinja.opengles.library.NativeMemory;
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
	private Pointer indices;
	private FloatPointer normals;
	private FloatPointer textureCoords;
	private FloatPointer vertices;

	public PrimitiveData(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
	}

	public PrimitiveData(PrimitiveType primitiveType, Vector3f[] vertices, Integer ...indices) {
		this.primitiveType = primitiveType;
		setVertices(vertices);
		setIndices(indices);
	}

	private void validate(int length) {
		if (vertices == null) {
			throw new GLException("Vertices must be set before setting normals, colors or texture coordinates.");
		} else if (length != vertices.capacity()) {
			throw new GLException(String.format("Attributes such as normals, colors and texture coordinates must " +
			                                    "match with the number of vertices (got %d, but expected %d).",
			                                    length, vertices.capacity()));
		}
	}

	public FloatPointer getColors() {
		return colors;
	}

	public void setColors(Color4f ...colors) {
		validate(colors.length * 3);
		this.colors = NativeMemory.getFloatPointer(this.colors, colors.length * 4);

		for (int i = 0; i < colors.length; i++) {
			this.colors.put((i * 4), colors[i].x);
			this.colors.put((i * 4) + 1, colors[i].y);
			this.colors.put((i * 4) + 2, colors[i].z);
			this.colors.put((i * 4) + 3, colors[i].w);
		}
	}

	public Pointer getIndices() {
		return indices;
	}

	public final void setIndices(Integer ...indices) {
		if (indices.length <= 256) {
			this.indices = NativeMemory.getBytePointer(this.indices, indices.length);
		} else {
			this.indices = NativeMemory.getShortPointer(this.indices, indices.length);
		}

		for (int i = 0; i < indices.length; i++) {
			if (indices[i] >= this.vertices.capacity() / 3) {
				throw new GLException(String.format("Indices must point within bounds of vertices (got %d, " +
				                                     "but maximum is %d)", indices[i], this.vertices.capacity() / 3));
			}

			if (this.indices instanceof BytePointer) {
				((BytePointer) this.indices).put(i, indices[i].byteValue());
			} else {
				((ShortPointer) this.indices).put(i, indices[i].shortValue());
			}
		}
	}

	public FloatPointer getNormals() {
		return normals;
	}

	public void setNormals(Vector3f ...normals) {
		validate(normals.length * 3);
		this.normals = NativeMemory.getFloatPointer(this.normals, normals.length * 3);

		for (int i = 0; i < normals.length; i++) {
			this.normals.put((i * 3), normals[i].x);
			this.normals.put((i * 3) + 1, normals[i].y);
			this.normals.put((i * 3) + 2, normals[i].z);
		}
	}

	public FloatPointer getTextureCoords() {
		return textureCoords;
	}

	public void setTextureCoords(Vector2f ...textureCoords) {
		validate(textureCoords.length * 3);
		this.textureCoords = NativeMemory.getFloatPointer(this.textureCoords, textureCoords.length * 2);

		for (int i = 0; i < textureCoords.length; i++) {
			this.textureCoords.put((i * 2), textureCoords[i].x);
			this.textureCoords.put((i * 2) + 1, textureCoords[i].y);
		}
	}

	public FloatPointer getVertices() {
		return vertices;
	}

	public final void setVertices(Vector3f  ...vertices) {
		this.vertices = NativeMemory.getFloatPointer(this.vertices, vertices.length * 3);

		for (int i = 0; i < vertices.length; i++) {
			this.vertices.put((i * 3), vertices[i].x);
			this.vertices.put((i * 3) + 1, vertices[i].y);
			this.vertices.put((i * 3) + 2, vertices[i].z);
		}
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
