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
import org.junit.Assert;
import org.junit.Test;

public class ArrayHelperTest {
	private static final String TEST_STRING = "ArrayHelperTest is testing the ArrayHelper class!";
	private static final Character[] CHARACTERS = toCharArray(TEST_STRING);

	private static Character[] toCharArray(String string) {
		return ArrayUtils.toObject(string.toCharArray());
	}

	@Test(expected = IllegalAccessException.class)
	public void testConstructorPrivacy() throws InstantiationException, IllegalAccessException {
		ArrayHelper.class.newInstance();
	}

	@Test
	public void testIndexOfSubArray() {
		// Should return correct index
		Assert.assertEquals(0, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("Ar")));
		Assert.assertEquals(0, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("ArrayHelper")));
		Assert.assertEquals(5, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("Helper")));
		Assert.assertEquals(48, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("!")));

		// Should not return index
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("testinG")));
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("")));
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("! ")));
	}

	@Test
	public void testIndexOfSubArrayWrapped() {
		// Should return correct index
		Assert.assertEquals(0, ArrayHelper.indexOfSubArrayWrapped(CHARACTERS, toCharArray(TEST_STRING + "A")));
		Assert.assertEquals(5, ArrayHelper.indexOfSubArray(CHARACTERS, toCharArray("Helper")));
		Assert.assertEquals(48, ArrayHelper.indexOfSubArrayWrapped(CHARACTERS, toCharArray("!Ar")));

		// Should not return index
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArrayWrapped(CHARACTERS, toCharArray("!ar")));
	}

	@Test
	public void testIndexOfSubArrayChunk() {
		// Should return correct index
		Assert.assertEquals(0, ArrayHelper.indexOfSubArrayChunk(CHARACTERS, toCharArray("zzzzA"), 1));
		Assert.assertEquals(11, ArrayHelper.indexOfSubArrayChunk(CHARACTERS, toCharArray("TestHelper"), 4));
		Assert.assertEquals(3, ArrayHelper.indexOfSubArrayChunk(CHARACTERS, toCharArray("ayHe"), 3));
		Assert.assertEquals(19, ArrayHelper.indexOfSubArrayChunk(CHARACTERS, toCharArray("some!testing"), 5));

		// Should not return index
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArrayChunk(CHARACTERS, toCharArray("!"), 2));
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArrayChunk(CHARACTERS, toCharArray("Array "), 6));
	}

	@Test
	public void testIndexOfSubArrayChunkWrapped() {
		// Should return correct index
		Assert.assertEquals(48, ArrayHelper.indexOfSubArrayChunkWrapped(CHARACTERS, toCharArray("z!Array"), 3));

		// Should not return index
		Assert.assertEquals(-1, ArrayHelper.indexOfSubArrayChunkWrapped(CHARACTERS, toCharArray("zz!Array"), 7));
	}
}
