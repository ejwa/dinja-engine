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
package com.ejwa.dinja.utility;

import org.apache.commons.lang.ArrayUtils;

public final class ArrayHelper {
	private ArrayHelper() {
		/* No instances of this class allowed. */
	}

	public static <T> int indexOfSubArray(T[] array, T[] subArray) {
		return indexOfSubArrayChunk(array, subArray, subArray.length);
	}

	public static <T> int indexOfSubArrayWrapped(T[] array, T[] subArray) {
		 return indexOfSubArray(ArrayUtils.addAll(array, array), subArray);
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static <T> int indexOfSubArrayChunk(T[] array, T[] subArray, int subArrayChunkSize) {
		if (subArray.length < subArrayChunkSize) {
			return -1;
		}

		int matches = 0;
		int matchingIndex = -1;

		for (int i = 0; i < subArray.length; i++) {
			for (int j = 0; j < array.length && i + matches < subArray.length; j++) {
				if (array[j].equals(subArray[i + matches])) {
					if (matches++ == 0) {
						matchingIndex = j;
					}

					if (matches == subArrayChunkSize) {
						return matchingIndex;
					}
				} else {
					matches = 0;
				}
			}
		}

		return -1;
	}

	public static <T> int indexOfSubArrayChunkWrapped(T[] array, T[] subArray, int subArrayChunkSize) {
		return indexOfSubArrayChunk(ArrayUtils.addAll(array, array), subArray, subArrayChunkSize);
	}
}
