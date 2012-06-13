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
package com.ejwa.dinja.utility.type;

import java.util.ArrayList;
import java.util.List;

public class Tuple<A, B> {
	private A a;
	private B b;

	public Tuple() {
		/* It should be possible to construct empty tuples. */
	}

	public Tuple(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

	public void set(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public static <A, B> List<A> getAList(List<Tuple<A, B>> t) {
		final List<A> list = new ArrayList<A>();

		for (Tuple<A, B> tuple : t) {
			list.add(tuple.getA());
		}

		return list;
	}

	public static <A, B> List<B> getBList(List<Tuple<A, B>> t) {
		final List<B> list = new ArrayList<B>();

		for (Tuple<A, B> tuple : t) {
			list.add(tuple.getB());
		}

		return list;
	}

	@Override
	public String toString() {
		return "A[" + a + "], B[" + b + "]";
	}
}
