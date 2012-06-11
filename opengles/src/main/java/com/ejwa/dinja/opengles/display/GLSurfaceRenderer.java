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
package com.ejwa.dinja.opengles.display;

import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.ejwa.dinja.opengles.Property;
import com.ejwa.dinja.opengles.display.draw.IFrameDrawListener;
import com.ejwa.dinja.opengles.display.draw.IFramePeekListener;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceRenderer implements Renderer {
	private long previousFrameTime = -1;
	private final GLSurface glSurface;

	public GLSurfaceRenderer(GLSurface glSurface) {
		this.glSurface = glSurface;
	}

	private long getMilliSecondsSinceLastFrame() {
		if (previousFrameTime == -1) {
			previousFrameTime = SystemClock.uptimeMillis();
			return 0;
		} else {
			final long msSinceLastFrame = SystemClock.uptimeMillis() - previousFrameTime;
			previousFrameTime = SystemClock.uptimeMillis();
			return msSinceLastFrame;
		}
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void onDrawFrame(GL10 gl) {
		final long timeSinceLastFrame = getMilliSecondsSinceLastFrame();
		long enterTime = System.nanoTime();

		for (int i = 0; i < glSurface.getFrameUpdateListeners().size(); i++) {
			final IFrameUpdateListener f = glSurface.getFrameUpdateListeners().get(i);
			f.onFrameUpdate(timeSinceLastFrame);
		}

		for (int i = 0; i < glSurface.getFrameDrawListeners().size(); i++) {
			final IFrameDrawListener frameDrawListener = glSurface.getFrameDrawListeners().get(i);

			if (frameDrawListener.isEnabled()) {
				synchronized (glSurface.getPrimitiveDataList()) {
					frameDrawListener.onDrawFrame(glSurface.getPrimitiveDataList());

					if (frameDrawListener instanceof IFramePeekListener) {
						final long peekTime = System.nanoTime();
						((IFramePeekListener) frameDrawListener).onPeekFrame(glSurface.getPrimitiveDataList());
						enterTime += (System.nanoTime() - peekTime);
					}
				}
			}
		}

		for (int i = 0; i < glSurface.getFrameTimeListeners().size(); i++) {
			final IFrameTimeListener f = glSurface.getFrameTimeListeners().get(i);

			/* Each frame time callback gets a fresh time... */
			f.onFrameTime(System.nanoTime() - enterTime);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		OpenGLES2Native.glViewport(0, 0, width, height);

		for (ISurfaceChangeListener s : glSurface.getSurfaceChangeListeners()) {
			s.onSurfaceChange(glSurface);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.i(GLSurfaceRenderer.class.getName(), "OpenGL properties of this device:");
		Log.i(GLSurfaceRenderer.class.getName(), "Version: " + Property.GL_VERSION.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Vendor: " + Property.GL_VENDOR.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Shading language version: " + Property.GL_SHADING_LANGUAGE_VERSION.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Renderer: " + Property.GL_RENDERER.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Extensions: " + Property.GL_EXTENSIONS.get());

		for (int i = 0; i < glSurface.getFrameDrawListeners().size(); i++) {
			final IFrameDrawListener frameDrawListener = glSurface.getFrameDrawListeners().get(i);

			if (frameDrawListener.isEnabled()) {
				synchronized (glSurface.getPrimitiveDataList()) {
					frameDrawListener.onPrepareDraw(glSurface.getPrimitiveDataList());
				}
			}
		}
	}
}
