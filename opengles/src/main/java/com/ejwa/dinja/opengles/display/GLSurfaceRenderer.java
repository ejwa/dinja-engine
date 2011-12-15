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

import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;
import com.ejwa.dinja.opengles.library.OpenGLES2;
import com.ejwa.dinja.opengles.Property;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import java.util.Random;
import java.util.Set;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceRenderer implements Renderer {
	private final Random r = new Random();
	private long previousFrameTime = 0;
	private final GLSurface glSurface;

	public GLSurfaceRenderer(GLSurface glSurface) {
		this.glSurface = glSurface;
	}

	private long getMilliSecondsSinceLastFrame() {
		final long msSinceLastFrame;

		msSinceLastFrame = SystemClock.uptimeMillis() - previousFrameTime;
		previousFrameTime = SystemClock.uptimeMillis();
		return msSinceLastFrame;
	}

	private void updatePrimitives(long milliSecondsSinceLastFrame) {
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		updatePrimitives(getMilliSecondsSinceLastFrame());
		OpenGLES2.glClearColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
		OpenGLES2.glClear(OpenGLES2.GL_DEPTH_BUFFER_BIT | OpenGLES2.GL_COLOR_BUFFER_BIT);

		for (Program program : glSurface.getPrograms()) {
			program.use();

			synchronized (glSurface.getPrimitiveDatas()) {
				for (PrimitiveData p : glSurface.getPrimitiveDatas()) {
					OpenGLES2.glDrawElements(program, p);
				}
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		OpenGLES2.glViewport(0, 0, width, height);
	}

	@Override
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.i(GLSurfaceRenderer.class.getName(), "OpenGL properties of this device:");
		Log.i(GLSurfaceRenderer.class.getName(), "Version: " + Property.GL_VERSION.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Vendor: " + Property.GL_VENDOR.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Shading language version: " + Property.GL_SHADING_LANGUAGE_VERSION.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Renderer: " + Property.GL_RENDERER.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Extensions: " + Property.GL_EXTENSIONS.get());

		synchronized (glSurface.getPrograms()) {
			for (Program program : glSurface.getPrograms()) {
				program.compile();

				for (PrimitiveData p : glSurface.getPrimitiveDatas()) {
					final Set<String> attributes = p.getVertexAttributeArrays().keySet();

					program.registerVertexAttributeHandles(p.getVertices().getVariableName());
					program.registerVertexAttributeHandles(attributes.toArray(new String[attributes.size()]));
				}
			}
		}
	}
}
