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
package com.ejwa.dinja.opengles.shader.argument;

import com.googlecode.javacpp.Pointer;

public abstract class AbstractUniform<T, P extends Pointer> {
	protected final int components;
	protected P data;
	private T value;
	private final String variableName;

	public AbstractUniform(String variableName, int components) {
		this.variableName = variableName;
		this.components = components;
	}

	public AbstractUniform(String variableName, int components, T value) {
		this(variableName, components);
		set(value);
	}

	public int getComponents() {
		return components;
	}

	public T get() {
		return value;
	}

	public final void set(T value) {
		this.value = value;
		setData(value);
	}

	public synchronized P getData() {
		return data;
	}

	public abstract void send(int handle);
	protected abstract void setData(T value);

	public String getVariableName() {
		return variableName;
	}

	@Override
	protected void finalize() throws Throwable {
		if (data != null) { data.deallocate(); }
		super.finalize();
	}
}
