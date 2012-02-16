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

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import com.ejwa.dinja.opengles.display.draw.IFrameDrawListener;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GLSurface extends GLSurfaceView {
	private final List<PrimitiveData> primitiveDatas = Collections.synchronizedList(new ArrayList<PrimitiveData>());
	private final List<IFrameDrawListener> frameDrawListeners = Collections.synchronizedList(new ArrayList<IFrameDrawListener>());
	private final List<IFrameTimeListener> frameTimeListeners = Collections.synchronizedList(new ArrayList<IFrameTimeListener>());
	private final List<IFrameUpdateListener> frameUpdateListeners = Collections.synchronizedList(new ArrayList<IFrameUpdateListener>());
	private final List<ISurfaceChangeListener> surfaceChangeListeners = Collections.synchronizedList(new ArrayList<ISurfaceChangeListener>());

	public GLSurface(Context context) {
		super(context);

		setEGLContextFactory(new ContextFactory());
		setZOrderOnTop(true);
		setEGLConfigChooser(new DisplayConfigurationChooser());
		getHolder().setFormat(PixelFormat.RGBA_8888);
		setRenderer(new GLSurfaceRenderer(this));
	}

	public void registerPrimitiveData(PrimitiveData primitiveData) {
		if (!primitiveDatas.contains(primitiveData)) {
			primitiveDatas.add(primitiveData);
		}
	}

	public void unregisterPrimitiveData(PrimitiveData primitiveData) {
		primitiveDatas.remove(primitiveData);
	}

	public List<PrimitiveData> getPrimitiveDatas() {
		return primitiveDatas;
	}

	public void registerFrameDrawListener(IFrameDrawListener frameDrawListener) {
		if (!frameDrawListeners.contains(frameDrawListener)) {
			frameDrawListeners.add(frameDrawListener);
		}
	}

	public void unregisterFrameDrawListener(IFrameDrawListener frameDrawListener) {
		frameDrawListeners.remove(frameDrawListener);
	}

	public List<IFrameDrawListener> getFrameDrawListeners() {
		return frameDrawListeners;
	}

	public void registerFrameTimeListener(IFrameTimeListener frameTimeListener) {
		if (!frameTimeListeners.contains(frameTimeListener)) {
			frameTimeListeners.add(frameTimeListener);
		}
	}

	public void unregisterFrameTimeListener(IFrameTimeListener frameTimeListener) {
		frameTimeListeners.remove(frameTimeListener);
	}

	public List<IFrameTimeListener> getFrameTimeListeners() {
		return frameTimeListeners;
	}

	public void registerFrameUpdateListener(IFrameUpdateListener frameUpdateListener) {
		if (!frameUpdateListeners.contains(frameUpdateListener)) {
			frameUpdateListeners.add(frameUpdateListener);
		}
	}

	public void unregisterFrameUpdateListener(IFrameUpdateListener frameUpdateListener) {
		frameUpdateListeners.remove(frameUpdateListener);
	}

	public List<IFrameUpdateListener> getFrameUpdateListeners() {
		return frameUpdateListeners;
	}

	public void registerSurfaceChangeListener(ISurfaceChangeListener surfaceChangeListener) {
		if (!surfaceChangeListeners.contains(surfaceChangeListener)) {
			surfaceChangeListeners.add(surfaceChangeListener);
		}

		if (isShown()) {
			surfaceChangeListener.onSurfaceChange(this);
		}
	}

	public void unregisterSurfaceChangeListener(ISurfaceChangeListener surfaceChangeListener) {
		surfaceChangeListeners.remove(surfaceChangeListener);
	}

	public List<ISurfaceChangeListener> getSurfaceChangeListeners() {
		return surfaceChangeListeners;
	}
}
