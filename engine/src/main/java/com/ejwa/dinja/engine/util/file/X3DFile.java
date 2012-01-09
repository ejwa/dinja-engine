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
package com.ejwa.dinja.engine.util.file;

import android.content.res.AssetManager;
import android.util.Log;
import com.ejwa.dinja.engine.model.file.FileFormatException;
import com.ejwa.dinja.engine.model.file.x3d.Appearance;
import com.ejwa.dinja.engine.model.file.x3d.Group;
import com.ejwa.dinja.engine.model.file.x3d.IndexedFaceSet;
import com.ejwa.dinja.engine.model.file.x3d.Shape;
import com.ejwa.dinja.engine.model.file.x3d.Transform;
import com.ejwa.dinja.engine.util.xml.XMLReader;
import com.ejwa.dinja.engine.model.file.x3d.X3D;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import com.ejwa.dinja.engine.model.mesh.Vertex;
import com.ejwa.dinja.engine.util.TextureLoader;
import com.ejwa.dinja.opengles.primitive.PrimitiveType;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

	private void loadMeshTextures(Mesh mesh, Appearance appearance) {
		if (appearance != null && appearance.getImageTexture() != null) {
			final String texture = appearance.getImageTexture().getUrl().split(" \"")[0].replaceAll("\"", "");
			mesh.setTexture(TextureLoader.load(assetManager, texture));
		}
	}

	private void setVertexProperties(Vertex v, int index, List<Vector3f> normals, ListIterator<Vector2f> textureCoordinates) {
		if (normals != null) {
			v.setNormal(normals.get(index));
		}

		if (textureCoordinates != null) {
			final Vector2f uv = textureCoordinates.next();
			uv.y = 1f - uv.y;
			v.setTextureCoordinates(uv);
		}
	}

	@SuppressWarnings({"PMD.ProtectLogD", "PMD.AvoidInstantiatingObjectsInLoops", "PMD.DataflowAnomalyAnalysis"})
	private void addMeshVertices(Mesh mesh, IndexedFaceSet set) {
		final List<Vector3f> coordinates = set.getCoordinate().getPointVectors();
		final List<Vector3f> normals = set.getNormal() == null ? null : set.getNormal().getVectorList();
		final ListIterator<Vector2f> textureCoordinates = set.getTextureCoordinate() == null ? null : set.getTextureCoordinate().getPointList().listIterator();
		final List<Integer> faces = set.getCoordIndexList();

		for (Integer i : faces) {
			if (i != -1) {
				final Vertex v = new Vertex(coordinates.get(i));
				setVertexProperties(v, i, normals, textureCoordinates);

				/*
				 * If an identical vertex exists with identical properties, re-use that vertex in the face instead
				 * of adding the newly created one.
				 */
				if (mesh.getVertices().contains(v)) {
					mesh.addIndices(mesh.getVertices().get(mesh.getVertices().indexOf(v)));
				} else {
					mesh.addVertices(v);
					mesh.addIndices(v);
				}
			}
		}

		Log.d(getClass().getName(), String.format("Number of indices: %s", mesh.getIndices().size()));
		Log.d(getClass().getName(), String.format("Original number of vertices: %s", coordinates.size()));
		Log.d(getClass().getName(), String.format("Number of vertices after duplicating for texture coordinates: %s", mesh.getVertices().size()));
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
					final String meshName = ((Group) t.getTransformable()).getDef();
					final Mesh mesh = new Mesh(meshName, PrimitiveType.GL_TRIANGLES);

					addMeshVertices(mesh, shape.getIndexedFaceSet());
					loadMeshTextures(mesh, shape.getAppearance());
					mesh.updatePrimitiveDataAttributes();
					meshes.add(mesh);
				}
			}
		}

		return meshes;
	}

	@Override
	public Mesh getMesh(String name) {
		final List<Mesh> meshes = getMeshes();

		for (Mesh m : meshes) {
			if (name.equals(m.getName())) {
				return m;
			}
		}

		return null;
	}
}
