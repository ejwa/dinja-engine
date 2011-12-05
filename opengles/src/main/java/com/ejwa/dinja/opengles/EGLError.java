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
package com.ejwa.dinja.opengles;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.khronos.egl.EGL10;

public final class EGLError {
	private static final Map<Integer, String> ERRORS = new HashMap<Integer, String>();

	static {
		ERRORS.put(EGL10.EGL_SUCCESS, "The last function succeeded without error.");
		ERRORS.put(EGL10.EGL_NOT_INITIALIZED, "EGL is not initialized, or could not be initialized, for the specified EGL display connection.");
		ERRORS.put(EGL10.EGL_BAD_ACCESS, "EGL cannot access a requested resource (for example a context is bound in another thread).");
		ERRORS.put(EGL10.EGL_BAD_ALLOC, "EGL failed to allocate resources for the requested operation.");
		ERRORS.put(EGL10.EGL_BAD_ATTRIBUTE, "An unrecognized attribute or attribute value was passed in the attribute list.");
		ERRORS.put(EGL10.EGL_BAD_CONTEXT, "An context argument does not name a valid EGL rendering context.");
		ERRORS.put(EGL10.EGL_BAD_CONFIG, "An config argument does not name a valid EGL frame buffer configuration.");
		ERRORS.put(EGL10.EGL_BAD_CURRENT_SURFACE, "The current surface of the calling thread is a window, pixel buffer or pixmap that is no longer valid.");
		ERRORS.put(EGL10.EGL_BAD_DISPLAY, "An display argument does not name a valid EGL display connection.");
		ERRORS.put(EGL10.EGL_BAD_SURFACE, "An surface argument does not name a valid surface (window, pixel buffer or pixmap) configured for GL rendering.");
		ERRORS.put(EGL10.EGL_BAD_MATCH, "Arguments are inconsistent (for example, a valid context requires buffers not supplied by a valid surface).");
		ERRORS.put(EGL10.EGL_BAD_PARAMETER, "One or more argument values are invalid.");
		ERRORS.put(EGL10.EGL_BAD_NATIVE_PIXMAP, "A native pixmap type argument does not refer to a valid native pixmap.");
		ERRORS.put(EGL10.EGL_BAD_NATIVE_WINDOW, "A native window type argument does not refer to a valid native window.");
		ERRORS.put(0x300e, "A power management event has occurred. The application must destroy all contexts and " +
				   "reinitialise OpenGL ES state and objects to continue rendering.");
	}

	private EGLError() {
		/* No instances of this class allowed. */
	}

	public static String getDescription(int errorCode) {
		return ERRORS.get(errorCode);
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static void check(Class callingClass, EGL10 egl) {
		int code = egl.eglGetError();

		if (code != EGL10.EGL_SUCCESS) {
			do {
				Log.e(callingClass.getName(), String.format("EGL error: %s", getDescription(code)));
			} while ((code = egl.eglGetError()) != EGL10.EGL_SUCCESS);

			throw new GLException("An EGL error occured.");
		}
	}
}
