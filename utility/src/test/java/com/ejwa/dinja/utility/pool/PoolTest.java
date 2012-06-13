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
package com.ejwa.dinja.utility.pool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PoolTest {
	private static class BasePoolableClass<T> implements Poolable<T> {
		protected int numClears = 0;

		@Override
		public void clear() {
			numClears++;
		}
	}

	private static class PrivatePoolableClass extends BasePoolableClass<PrivatePoolableClass> {
		/* Just a dummy class implementing the poolable interface */
	}

	public static class PublicPoolableClassNoDC extends BasePoolableClass<PublicPoolableClassNoDC> {
		public PublicPoolableClassNoDC(int numClears) {
			super();
			this.numClears = numClears;
		}
	}

	private Pool<PrivatePoolableClass> firstPool;
	private Pool<PrivatePoolableClass> secondPool;


	@Before
	public void createFreshPools() {
		firstPool = new Pool<PrivatePoolableClass>(PrivatePoolableClass.class);
		secondPool = new Pool<PrivatePoolableClass>(PrivatePoolableClass.class);
	}

	@Test
	public void testAllocate() {
		for (int i = 1; i <= 200; i++) {
			final PrivatePoolableClass pb = firstPool.allocate();
			secondPool.free(pb);
			Assert.assertEquals(i, secondPool.getSize());
		}

		Assert.assertEquals(0, firstPool.getSize());
		final PrivatePoolableClass pb = secondPool.allocate();
		Assert.assertEquals(199, secondPool.getSize());

		firstPool.free(pb);
		Assert.assertEquals(1, firstPool.getSize());
	}

	@Test(expected = IllegalStateException.class)
	public void testAllocatePublicNoDefaultConstuctor() throws IllegalStateException {
		final Pool<PublicPoolableClassNoDC> pool = new Pool<PublicPoolableClassNoDC>(PublicPoolableClassNoDC.class);
		pool.allocate();
		Assert.fail("Allocating a class with no default constructor should throw an exception.");
	}

	@Test
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void testAllocateCleared() {
		PrivatePoolableClass pb = null;

		for (int i = 1; i <= 200; i++) {
			pb = firstPool.allocateCleared();
			Assert.assertEquals(i, pb.numClears);
			firstPool.free(pb);
		}

		secondPool.free(pb);
		Assert.assertEquals(201, secondPool.allocateCleared().numClears);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFreeNull() throws IllegalArgumentException {
		firstPool.free(null);
		Assert.fail("Freeing a null object from a pool should throw an exception.");
	}
}
