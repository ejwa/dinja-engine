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
package com.ejwa.dinja.physics.pool;

import java.util.ArrayList;
import java.util.List;

public class Pool<T extends Poolable<T>> {
	private final List<T> objects = new ArrayList<T>();
	private final Class<T> poolable;
	private int n = 0;

	public Pool(Class<T> poolable) {
		this.poolable = poolable;
	}

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
				object = poolable.newInstance();
			} catch (Exception ex) {
				throw new IllegalStateException("Failed to create instance for pool.", ex);
			}
		}

		if (clear) {
			object.clear();
		}

		return object;
	}

	public T allocate() {
		return allocate(false);
	}

	public T allocateCleared() {
		return allocate(true);
	}

	public void free(T object) {
		if (object == null) {
			return;
		}

		objects.add(object);
		n++;
	}
}
