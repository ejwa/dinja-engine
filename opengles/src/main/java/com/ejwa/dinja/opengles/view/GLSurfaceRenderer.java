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
package com.ejwa.dinja.opengles.view;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.ejwa.dinja.opengles.OpenGLES2;
import com.ejwa.dinja.opengles.OpenGLES2Code;
import com.ejwa.dinja.opengles.OpenGLESProperty;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceRenderer implements Renderer {
	private final Random r = new Random();

	@Override
	public void onDrawFrame(GL10 gl) {
		OpenGLES2.glClearColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
		OpenGLES2.glClear(OpenGLES2Code.GL_DEPTH_BUFFER_BIT | OpenGLES2Code.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		OpenGLES2.glViewport(0, 0, width, height);

		Log.i(GLSurfaceRenderer.class.getName(), "OpenGL properties of this device:");
		Log.i(GLSurfaceRenderer.class.getName(), "Version: " + OpenGLESProperty.get(OpenGLESProperty.Identifier.GL_VERSION));
		Log.i(GLSurfaceRenderer.class.getName(), "Vendor: " + OpenGLESProperty.get(OpenGLESProperty.Identifier.GL_VENDOR));
		Log.i(GLSurfaceRenderer.class.getName(), "Shading language version: " + OpenGLESProperty.get(OpenGLESProperty.Identifier.GL_SHADING_LANGUAGE_VERSION));
		Log.i(GLSurfaceRenderer.class.getName(), "Renderer: " + OpenGLESProperty.get(OpenGLESProperty.Identifier.GL_RENDERER));
		Log.i(GLSurfaceRenderer.class.getName(), "Extensions: " + OpenGLESProperty.get(OpenGLESProperty.Identifier.GL_EXTENSIONS));

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		/* Not doing anything right now! */
	}
}
