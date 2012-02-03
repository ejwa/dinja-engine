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
package com.ejwa.dinja.opengles.primitive;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class PrimitiveDataArgument<T> extends LinkedList<T> {
	private static final long serialVersionUID = 1L;

	private final Map<String, T> argumentMap = new HashMap<String, T>();

	public void put(String name, T argument) {
		argumentMap.put(name, argument);
		add(argument);
	}

	public T remove(String name) {
		final T argument = argumentMap.remove(name);

		if (argument != null) {
			super.remove(argument);
		}

		return argument;
	}

	public T get(String name) {
		return argumentMap.get(name);
	}

	public Set<String> getNames() {
		return argumentMap.keySet();
	}
}
