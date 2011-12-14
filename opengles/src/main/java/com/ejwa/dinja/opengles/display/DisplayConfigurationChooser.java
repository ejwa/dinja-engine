/*
 * Copyright © 2011 Ejwa Software. All rights reserved.
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
package com.ejwa.dinja.opengles.display;

import android.opengl.GLSurfaceView.EGLConfigChooser;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

class DisplayConfigurationChooser implements EGLConfigChooser {
	private static final int EGL_OPENGL_ES2_BIT = 4;
	private static final int RED_BITS = 5;
	private static final int GREEN_BITS = 6;
	private static final int BLUE_BITS = 5;
	private static final int ALPHA_BITS = 0;
	private static final int DEPTH_BITS = 0;
	private static final int STENCIL_BITS = 0;

	private static int configuration[] = {
		EGL10.EGL_RED_SIZE, 4,
		EGL10.EGL_GREEN_SIZE, 4,
		EGL10.EGL_BLUE_SIZE, 4,
		EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT /* Enables 2.0 rendering */,
		EGL10.EGL_NONE
	};

	private int findAttribute(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute) {
		final int value[] = new int[1];

		if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
			return value[0];
		}

		return 0;
	}

	@Override
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
		final int numConfigurations[] = new int[1];
		egl.eglChooseConfig(display, configuration, null, 0, numConfigurations);

		if (numConfigurations[0] <= 0) {
			throw new IllegalArgumentException("No usable configurations for display");
		}

		final EGLConfig glConfigurations[] = new EGLConfig[numConfigurations[0]];
		egl.eglChooseConfig(display, configuration, glConfigurations, numConfigurations[0], numConfigurations);

		for (EGLConfig c : glConfigurations) {
			/*
			 * We look for a matching display and return it if it's found.
			 */
			final int dBits = findAttribute(egl, display, c, EGL10.EGL_DEPTH_SIZE);
			final int sBits = findAttribute(egl, display, c, EGL10.EGL_STENCIL_SIZE);
			final int rBits = findAttribute(egl, display, c, EGL10.EGL_RED_SIZE);
			final int gBits = findAttribute(egl, display, c, EGL10.EGL_GREEN_SIZE);
			final int bBits = findAttribute(egl, display, c, EGL10.EGL_BLUE_SIZE);
			final int aBits = findAttribute(egl, display, c, EGL10.EGL_ALPHA_SIZE);

			if (dBits >= DEPTH_BITS && sBits >= STENCIL_BITS &&
			    rBits == RED_BITS && gBits == GREEN_BITS && bBits == BLUE_BITS && aBits == ALPHA_BITS) {
				return c;
			}
		}

		throw new IllegalArgumentException("No matching configurations for display");
	}
}
