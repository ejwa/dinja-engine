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

/**
 * Class that contains some useful methods for searching for subarrays in arrays. The class has methods that work
 * in a similar fashion to {@link String#indexOf(java.lang.String)} but is adapted for generic arrays and includes some
 * additional features, such as support for wrapping and searching of chunks.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public final class ArrayHelper {
	private ArrayHelper() {
		/* No instances of this class allowed. */
	}

	/**
	 * Returns the index, within an array, of the first occurrence of the specified subarray.
	 *
	 * @param array An array to search in.
	 * @param subArray The subarray for which to search.
	 * @return The index within this array of the first occurrence of the specified subarray.
	 */
	public static <T> int indexOfSubArray(T[] array, T[] subArray) {
		return indexOfSubArrayChunk(array, subArray, subArray.length);
	}

	/**
	 * Returns the index, within a wrapped array, of the first occurrence of the specified subarray. This method works in
	 * a similar fashion to @{@link #indexOfSubArray(T[], T[])}, but loops through the array twice when searchnig for the
	 * subarray.
	 *
	 * @param array An array to search in.
	 * @param subArray The subarray for which to search.
	 * @return The index within this array of the first occurrence of the specified subarray.
	 * @see #indexOfSubArray(T[], T[])
	 */
	public static <T> int indexOfSubArrayWrapped(T[] array, T[] subArray) {
		 return indexOfSubArray(ArrayUtils.addAll(array, array), subArray);
	}

	/**
	 * Returns the index, within an array, of the first occurrence of one of the specified subarray chunks. This method
	 * works in a similar fashion to @{@link #indexOfSubArray(T[], T[])}, but searches for chunks of size
	 * <code>subArrayChunkSize</code> from the subarray when searching through the array.
	 *
	 * @param array An array to search in.
	 * @param subArray The subarray for which to search from.
	 * @param subArrayChunkSize Size of the chunks to pick from the subarray when searching through the array.
	 * @return The index within this array of the first occurrence of one of the specified subarray chunks.
	 * @see #indexOfSubArray(T[], T[])
	 */
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

	/**
	 * Returns the index, within a wrapped array, of the first occurrence of one of the specified subarray chunks. This
	 * method works in a similar fashion to @{@link #indexOfSubArrayWrapped(T[], T[])}, but searches for chunks of size
	 * <code>subArrayChunkSize</code> from the subarray when searching through the array.
	 *
	 * @param array An array to search in.
	 * @param subArray The subarray for which to search from.
	 * @param subArrayChunkSize Size of the chunks to pick from the subarray when searching through the array.
	 * @return The index within this array of the first occurrence of one of the specified subarray chunks.
	 * @see #indexOfSubArrayWrapped(T[], T[])
	 */
	public static <T> int indexOfSubArrayChunkWrapped(T[] array, T[] subArray, int subArrayChunkSize) {
		return indexOfSubArrayChunk(ArrayUtils.addAll(array, array), subArray, subArrayChunkSize);
	}
}
