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
package com.ejwa.dinja.engine.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("PMD.TooManyMethods")
public class HashedArrayList<T> extends ArrayList<T> {
	private static final long serialVersionUID = 1L;
	private final Map<T, Integer> elementsWithIndex = new HashMap<T, Integer>();

	private void addIndexed(T e) {
		if (elementsWithIndex.put(e, size()) != null) {
			throw new IllegalArgumentException("Hash list already contains an indentical element.");
		}
	}

	@Override
	public boolean add(T e) {
		addIndexed(e);
		super.add(e);
		return true;
	}

	@Override
	public void add(int i, T e) {
		if (i > size()) {
			throw new IllegalArgumentException("Index exceeds size of list.");
		}

		addIndexed(e);
		super.add(i, e);

		for (Integer index : elementsWithIndex.values()) {
			if (index >= i) {
				index++;
			}
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		for (T o : collection) {
			add(o);
		}

		return true;
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public boolean remove(Object o) {
		final int removedIndex = elementsWithIndex.remove((T) o);
		super.remove(o);

		for (Integer index : elementsWithIndex.values()) {
			if (index > removedIndex) {
				index--;
			}
		}

		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		for (Object o : collection) {
			remove((T) o);
		}

		return true;
	}

	@Override
	public int indexOf(Object o) {
		final Integer index = elementsWithIndex.get((T) o);
		return index == null ? -1 : index;
	}

	@Override
	public boolean contains(Object o) {
		return elementsWithIndex.containsKey((T) o);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object o : collection) {
			if (!contains((T) o)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void clear() {
		super.clear();
		elementsWithIndex.clear();
	}

	public void update(int i) {
		elementsWithIndex.put(get(i), i);
	}

	public void update() {
		for (int i = 0; i < size(); i++) {
			update(i);
		}
	}

	@Override
	public boolean addAll(int i, Collection<? extends T> collection) {
		throw new UnsupportedOperationException("Insertion of indexed object collection into a hashed list is not supported.");
	}

	@Override
	public T remove(int i) {
		throw new UnsupportedOperationException("Indexed removal in hashed list is not supported.");
	}

	@Override
	protected void removeRange(int i, int i1) {
		throw new UnsupportedOperationException("Indexed removal in hashed list is not supported.");
	}
}
