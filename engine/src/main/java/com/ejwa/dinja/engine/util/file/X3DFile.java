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
package com.ejwa.dinja.engine.util.file;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ejwa.dinja.engine.model.file.FileFormatException;
import com.ejwa.dinja.engine.model.file.FileResourceException;
import com.ejwa.dinja.engine.model.file.x3d.Group;
import com.ejwa.dinja.engine.model.file.x3d.IndexedFaceSet;
import com.ejwa.dinja.engine.model.file.x3d.Shape;
import com.ejwa.dinja.engine.model.file.x3d.Transform;
import com.ejwa.dinja.engine.util.xml.XMLReader;
import com.ejwa.dinja.engine.model.file.x3d.X3D;
import com.ejwa.dinja.engine.model.mesh.Face;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import com.ejwa.dinja.engine.model.mesh.Vertex;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class X3DFile extends XMLReader<X3D> implements IFile {
	private static final String REQUIRED_FILE_VERSION = "3.0";
	private final AssetManager assetManager;
	private final X3D x3d;

	public X3DFile(AssetManager assetManager, String fileName) {
		super();
		this.assetManager = assetManager;
		x3d = read(X3D.class, assetManager, fileName);

		if (x3d == null) {
			throw new FileFormatException("Invalid or unsupported file format.");
		} else if (!REQUIRED_FILE_VERSION.equals(x3d.getVersion())) {
			throw new FileFormatException(String.format("Invalid file format; only version %s is supported but " +
			                              "the file is of version %s.", REQUIRED_FILE_VERSION, x3d.getVersion()));
		}
	}

	private void loadMeshTextures(Mesh mesh, Shape shape) {
		if (shape.getAppearance() != null && shape.getAppearance().getImageTexture() != null) {
			final String texture = shape.getAppearance().getImageTexture().getUrl();

			try {
				final Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(texture));
				/* TODO: Implement the rest of the texture loading. */
			} catch (IOException ex) {
				throw new FileResourceException(String.format("Failed to load texture '%s' (%s).",
				                                texture, ex.getMessage()), ex);
			}
		}
	}

	@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "PMD.DataflowAnomalyAnalysis"})
	private void addMeshVertices(Mesh mesh, IndexedFaceSet set) {
		final List<Vector3f> coordinates = set.getCoordinate().getPointVectors();
		final List<Vector3f> normals = set.getNormal() == null ? null : set.getNormal().getVectorList();
		final List<Vector2f> textureCoordinates = set.getTextureCoordinate() == null ? null : set.getTextureCoordinate().getPointList();

		for (int i = 0; i < coordinates.size(); i++) {
			final Vertex vertex = new Vertex();

			vertex.setPosition(coordinates.get(i));

			if (normals != null) {
				vertex.setNormal(normals.get(i));
			}

			if (textureCoordinates != null) {
				vertex.setTextureCoordinates(textureCoordinates.get(i));
			}

			mesh.addVertices(vertex);
		}
	}

	@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "PMD.DataflowAnomalyAnalysis"})
	private void addMeshFaces(Mesh mesh, IndexedFaceSet set) {
		final List<Integer> faces = set.getCoordIndexList();
		Face f = new Face();

		for (Integer i : faces) {
			if (i == -1) {
				mesh.addFaces(f);
				f = new Face();
			} else {
				f.addVertices(mesh.getVertices().get(i));
			}
		}
	}

	@Override
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public List<Mesh> getMeshes() {
		final List<Mesh> meshes = new ArrayList<Mesh>();
		final List<Transform> transforms = x3d.getScene().getTransform();

		for (Transform t : transforms) {
			if (t.getTransformable() instanceof Group) {
				final Shape shape = ((Group) t.getTransformable()).getShape();

				if (shape != null) {
					final Mesh mesh = new Mesh(PrimitiveType.GL_TRIANGLE_STRIP);
					addMeshVertices(mesh, shape.getIndexedFaceSet());
					addMeshFaces(mesh, shape.getIndexedFaceSet());
				}
			}
		}

		return meshes;
	}
}
