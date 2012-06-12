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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Class that can be used to give an arbitrary object pooling capability. Whenever objects are created and garbage collected
 * very quickly (such as in a tight loop); pooling should be used. When objects are pooled, they are instead put in a list and
 * recycled whenever something needs a new instance of a particular class or object; this avoids the garbage collector from being
 * overloaded. The pooling class is used internally in Dinja Engine and can be used by projects that use the engine
 * whenever appropriate. Classes that should be poolable need to extend the {@link Poolable} interface and provide a proper
 * <code>clear()</code> method, which is called whenever an object is recycled with a {@link #allocateCleared()} call.</p>
 *
 * <p>It's very simple to create a thread-unique pool of objects of type <code>PooledType</code>. Making this kind of pool also
 * avoids having to deal with concurrency and synchronization between threads whenever multiple threads use objects from
 * the pool:</p>
 *
 * <blockquote><pre>
 * private static final ThreadLocal<Pool<PooledType>> POOL = new ThreadLocal<Pool<PooledType>>() {
 *	protected Pool<PooledType> initialValue() {
 *		return new Pool(PooledType.class);
 *	}
 * };</pre></blockquote>
 *
 * <p>It's then possible to call <code>POOL.get().{@link #allocate() allocate()}</code> and
 * <code>POOL.get().{@link #free(com.ejwa.dinja.utility.pool.Poolable) free()}</code> to fetch and store objects from and to the
 * pool.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 * @see Poolable
 */
public class Pool<T extends Poolable<T>> {
	private final List<T> objects = new ArrayList<T>();
	private final Class<T> poolable;
	private int n = 0;

	/**
	 * Constructs a new, empty pool for objects of type <code>T</code>.
	 *
	 * @param poolable The type of object this pool is to hold instances of.
	 */
	public Pool(Class<T> poolable) {
		this.poolable = poolable;
	}

	/**
	 * Returns the current size of this pool. This is essentialy the number of objects currently stored in this pool which
	 * are ready to be fetched and recycled.
	 *
	 * @return The current size of this pool.
	 */
	public int getSize() {
		return n;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private T allocate(boolean clear) {
		T object;

		if (n > 0) {
			object = objects.remove(--n);
		} else {
			try {
				final Constructor<?> constructor = poolable.getDeclaredConstructor();

				if (constructor.isAccessible()) {
					object = (T) constructor.newInstance();
				} else {
					constructor.setAccessible(true);
					object = (T) constructor.newInstance();
					constructor.setAccessible(false);
				}
			} catch (Exception ex) {
				throw new IllegalStateException("Failed to create instance for pool.", ex);
			}
		}

		if (clear) {
			object.clear();
		}

		return object;
	}

	/**
	 * Returns a newly allocated or recycled object from this pool. If no stored objects currently exist in this pool,
	 * return a newly allocated object by calling it's default constructor.
	 *
	 * @return An object of type <code>T</code> from this pool.
	 */
	public T allocate() {
		return allocate(false);
	}


	/**
	 * Clears and returns a newly allocated or recycled object from this pool. This method works in a similar fashion to
	 * {@link #allocate()}, but also calls the objects clear() method before returning it.
	 *
	 * @return A cleared object of type <code>T</code> from this pool.
	 */
	public T allocateCleared() {
		return allocate(true);
	}

	/**
	 * Free an object and and store it in this pool. This operation also increments the size of the pool by one.
	 * The object can later be recycled by calling {@link #allocate()} or {@link #allocateCleared()} on this pool.
	 *
	 * @param object The object to free and store in this pool.
	 */
	public void free(T object) {
		if (object == null) {
			throw new IllegalArgumentException("Can't store null objects in a pool.");
		}

		objects.add(object);
		n++;
	}
}
