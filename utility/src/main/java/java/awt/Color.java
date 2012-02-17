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
package java.awt;

public class Color {
	public final static Color BLACK = new Color(0, 0, 0);
	public final static Color BLUE = new Color(0, 0, 255);
	public final static Color CYAN = new Color(0, 255, 255);
	public final static Color DARK_GRAY = new Color(64, 64, 64);
	public final static Color GRAY = new Color(128, 128, 128);
	public final static Color GREEN = new Color(0, 255, 0);
	public final static Color LIGHT_GRAY = new Color(192, 192, 192);
	public final static Color MAGENTA = new Color(255, 0, 255);
	public final static Color ORANGE = new Color(255, 200, 0);
	public final static Color PINK = new Color(255, 175, 175);
	public final static Color RED = new Color(255, 0, 0);
	public final static Color WHITE = new Color(255, 255, 255);
	public final static Color YELLOW = new Color(255, 255, 0);

	private final int red;
	private final int green;
	private final int blue;

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return red & 0xff;
	}

	public int getGreen() {
		return green & 0xff;
	}

	public int getBlue() {
		return blue & 0xff;
	}

	public int getAlpha() {
		return 255; /* 255 equals completely opaque */
	}
}
