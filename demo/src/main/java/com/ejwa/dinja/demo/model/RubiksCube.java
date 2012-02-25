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
package com.ejwa.dinja.demo.model;

import com.ejwa.dinja.engine.model.material.Texture;
import com.ejwa.dinja.engine.model.node.Group;
import com.ejwa.dinja.engine.model.node.INode;
import com.ejwa.dinja.engine.model.node.mesh.geometry.Box;
import com.ejwa.dinja.utility.ArrayHelper;
import com.ejwa.dinja.utility.type.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.openmali.vecmath2.Vector2f;
import org.openmali.vecmath2.Vector3f;

public class RubiksCube extends Group {
	private final Map<INode, Vector3f> middleBoxSections = new HashMap<INode, Vector3f>();
	private final List<BoxSection> boxSections = new ArrayList<BoxSection>();
	private final Tuple<Vector3f, List<BoxSection>> rotationSlice = new Tuple<Vector3f, List<BoxSection>>();
	private int[] sliceToRoll;

	public final int[][] xIndices = {{9,18,21,24,15,6,3,0}, {10,19,22,25,16,7,4,1}, {11,20,23,26,17,8,5,2}};
	public final int[][] yIndices = {{0,1,2,11,20,19,18,9}, {3,4,5,14,23,22,21,12}, {6,7,8,17,26,25,24,15}};
	public final int[][] zIndices = {{0,3,6,7,8,5,2,1}, {9,12,15,16,17,14,11,10}, {18,21,24,25,26,23,20,19}};

	public class BoxSection extends Box {
		public BoxSection(String name, float x, float y, float z, float size) {
			super(name, size, size, size);
			getTranslator().set(new Vector3f(x - 0.5f, y - 0.5f, z - 0.5f));
		}
	}

	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public RubiksCube(Texture texture) {
		super("Rubiks Cube");

		final Vector2f[] uvs = new Vector2f[]{
			new Vector2f(0, 0.5f), new Vector2f(0.33f, 0.5f), new Vector2f(0, 1), new Vector2f(0.33f, 1), // front
			new Vector2f(0.67f, 0.5f), new Vector2f(1, 0.5f), new Vector2f(0.67f, 1), new Vector2f(1, 1), // top
			new Vector2f(0.67f, 0), new Vector2f(1, 0), new Vector2f(0.67f, 0.5f), new Vector2f(1, 0.5f), // left
			new Vector2f(0, 0), new Vector2f(0.33f, 0), new Vector2f(0, 0.5f), new Vector2f(0.33f, 0.5f), // right
			new Vector2f(0.34f, 0.5f), new Vector2f(0.66f, 0.5f), new Vector2f(0.34f, 1), new Vector2f(0.66f, 1), // bottom
			new Vector2f(0.34f, 0), new Vector2f(0.66f, 0), new Vector2f(0.34f, 0.5f), new Vector2f(0.66f, 0.5f) // back
		};

		for (int z = 0; z < 3; z++) {
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					final BoxSection box = new BoxSection(String.format("box_%d.%d.%d", x, y, z),
					                                      x * 0.5f, y * 0.5f, z * 0.5f, 0.24f);
					box.setFrontFaceTextureCoordinates(uvs[0], uvs[1], uvs[2], uvs[3]); // green
					box.setTopFaceTextureCoordinates(uvs[4], uvs[5], uvs[6], uvs[7]); // white
					box.setLeftFaceTextureCoordinates(uvs[8], uvs[9], uvs[10], uvs[11]); // organge
					box.setRightFaceTextureCoordinates(uvs[12], uvs[13], uvs[14], uvs[15]); // red
					box.setBottomFaceTextureCoordinates(uvs[16], uvs[17], uvs[18], uvs[19]); // blue
					box.setBackFaceTextureCoordinates(uvs[20], uvs[21], uvs[22], uvs[23]); // yellow

					box.getMaterial().setTexture(texture);
					boxSections.add(box);
				}
			}
		}

		addNodes(boxSections.toArray(new BoxSection[boxSections.size()]));
	}

	public Map<INode, Vector3f> getMiddleBoxSections() {
		middleBoxSections.clear();
		middleBoxSections.put(boxSections.get((2 * 3 * 3) + (1 * 3) + 1), Vector3f.POSITIVE_Z_AXIS);
		middleBoxSections.put(boxSections.get((1 * 3 * 3) + (2 * 3) + 1), Vector3f.POSITIVE_Y_AXIS);
		middleBoxSections.put(boxSections.get((1 * 3 * 3) + (1 * 3) + 0), Vector3f.NEGATIVE_X_AXIS);
		middleBoxSections.put(boxSections.get((1 * 3 * 3) + (1 * 3) + 2), Vector3f.POSITIVE_X_AXIS);
		middleBoxSections.put(boxSections.get((1 * 3 * 3) + (0 * 3) + 1), Vector3f.NEGATIVE_Y_AXIS);
		middleBoxSections.put(boxSections.get((0 * 3 * 3) + (1 * 3) + 1), Vector3f.NEGATIVE_Z_AXIS);

		return middleBoxSections;
	}

	/*
	 * Rotates a slice of the cube around a step using 6 swap operations in the following manner
	 * (showing a rotation in the negative direction):
	 *
	 * 234    034    014    410    450    456    456
	 * 1 5 -> 1 5 -> 3 5 -> 3 5 -> 3 1 -> 3 1 -> 3 7
	 * 076    276    276    276    276    270    210
	 */
	public void roll(Vector3f axis) {
		if (axis.equals(Vector3f.POSITIVE_X_AXIS) || axis.equals(Vector3f.POSITIVE_Y_AXIS) ||
		    axis.equals(Vector3f.POSITIVE_Z_AXIS)) {
			for (int j = 0; j < sliceToRoll.length - 2; j++) {
				Collections.swap(boxSections, sliceToRoll[j], sliceToRoll[j + 2]);
			}
		} else if (axis.equals(Vector3f.NEGATIVE_X_AXIS) || axis.equals(Vector3f.NEGATIVE_Y_AXIS) ||
		           axis.equals(Vector3f.NEGATIVE_Z_AXIS)) {
			for (int j = sliceToRoll.length - 1; j >= 2; j--) {
				Collections.swap(boxSections, sliceToRoll[j], sliceToRoll[j - 2]);
			}
		}
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private boolean setSlice(int[][] indices, Vector3f pRotation, Vector3f nRotation, BoxSection ...boxSections) {
		final BoxSection[] b = new BoxSection[indices[0].length];

		for (int i = 0; i < indices.length; i++) {
			sliceToRoll = indices[i];

			for (int j = 0; j < indices[i].length; j++) {
				b[j] = this.boxSections.get(indices[i][j]);
			}

			if (ArrayHelper.indexOfSubArrayChunkWrapped(b, boxSections, 2) == -1) {
				ArrayUtils.reverse(b);

				if (ArrayHelper.indexOfSubArrayChunkWrapped(b, boxSections, 2) != -1) {
					rotationSlice.set(pRotation, Arrays.asList(b));
					return true;
				}
			} else {
				rotationSlice.set(nRotation, Arrays.asList(b));
				return true;
			}
		}

		return false;
	}

	private boolean handleXSides(Vector3f side, BoxSection ...boxSections) {
		if (side.equals(Vector3f.POSITIVE_X_AXIS) || side.equals(Vector3f.NEGATIVE_X_AXIS)) {
			return setSlice(yIndices, Vector3f.POSITIVE_Y_AXIS, Vector3f.NEGATIVE_Y_AXIS, boxSections) ||
			       setSlice(zIndices, Vector3f.POSITIVE_Z_AXIS, Vector3f.NEGATIVE_Z_AXIS, boxSections);
		}

		return false;
	}

	private boolean handleYSides(Vector3f side, BoxSection ...boxSections) {
		if (side.equals(Vector3f.POSITIVE_Y_AXIS) || side.equals(Vector3f.NEGATIVE_Y_AXIS)) {
			return setSlice(xIndices, Vector3f.POSITIVE_X_AXIS, Vector3f.NEGATIVE_X_AXIS, boxSections) ||
			       setSlice(zIndices, Vector3f.POSITIVE_Z_AXIS, Vector3f.NEGATIVE_Z_AXIS, boxSections);
		}

		return false;
	}

	private boolean handleZSides(Vector3f side, BoxSection ...boxSections) {
		if (side.equals(Vector3f.POSITIVE_Z_AXIS) || side.equals(Vector3f.NEGATIVE_Z_AXIS)) {
			return setSlice(xIndices, Vector3f.POSITIVE_X_AXIS, Vector3f.NEGATIVE_X_AXIS, boxSections) ||
			       setSlice(yIndices, Vector3f.POSITIVE_Y_AXIS, Vector3f.NEGATIVE_Y_AXIS, boxSections);
		}

		return false;
	}

	public Tuple<Vector3f, List<BoxSection>> setSliceToRoll(Vector3f side, BoxSection ...boxSections) {
		if (handleXSides(side, boxSections) || handleYSides(side, boxSections) || handleZSides(side, boxSections)) {
			return rotationSlice;
		}

		rotationSlice.set(Vector3f.ZERO, null);
		return rotationSlice;
	}
}
