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
package com.ejwa.dinja.engine.util;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public final class StringConverter {
	private StringConverter() {
		/* No instances of this class allowed. */
	}

	public static Vector2f getVector2FromString(String source, String separator) {
		return getVector2ListFromString(source, separator).get(0);
	}

	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public static List<Vector2f> getVector2ListFromString(String source, String separator) {
		final List<Float> values = getFloatListFromString(source, separator);
		final List<Vector2f> vectors = new ArrayList<Vector2f>();

		for (int i = 0; i < values.size(); i += 2) {
			vectors.add(new Vector2f(values.get(i), values.get(i + 1)));
		}

		return vectors;
	}

	public static Vector3f getVector3FromString(String source, String separator) {
		return getVector3ListFromString(source, separator).get(0);
	}

	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public static List<Vector3f> getVector3ListFromString(String source, String separator) {
		final List<Float> values = getFloatListFromString(source, separator);
		final List<Vector3f> vectors = new ArrayList<Vector3f>();

		for (int i = 0; i < values.size(); i += 3) {
			vectors.add(new Vector3f(values.get(i), values.get(i + 1), values.get(i + 2)));
		}

		return vectors;
	}

	public static Vector4f getVector4FromString(String source, String separator) {
		return getVector4ListFromString(source, separator).get(0);
	}

	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public static List<Vector4f> getVector4ListFromString(String source, String separator) {
		final List<Float> values = getFloatListFromString(source, separator);
		final List<Vector4f> vectors = new ArrayList<Vector4f>();

		for (int i = 0; i < values.size(); i += 4) {
			vectors.add(new Vector4f(values.get(i), values.get(i + 1), values.get(i + 2), values.get(i + 3)));
		}

		return vectors;
	}

	public static List<Float> getFloatListFromString(String source, String separator) {
		final List<Float> values = new ArrayList<Float>();

		for (String s : source.split(" ")) {
			values.add(Float.valueOf(s));
		}

		return values;
	}

	public static List<Integer> getIntegerListFromString(String source, String separator) {
		final List<Integer> values = new ArrayList<Integer>();

		for (String s : source.split(" ")) {
			values.add(Integer.valueOf(s));
		}

		return values;
	}
}
