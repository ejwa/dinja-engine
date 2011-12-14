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
package com.ejwa.dinja.engine.activity;

import android.app.Activity;
import android.os.Bundle;
import com.ejwa.dinja.engine.view.Viewable;
import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.display.GLSurface;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.FragmentShader;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.VertexShader;
import java.io.InputStream;

public class DinjaActivity extends Activity {
	private GLSurface glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		glSurfaceView = new GLSurface(getApplication());
		setContentView(glSurfaceView);
	}

	@Override
	protected void onPause() {
		glSurfaceView.onPause();
		super.onPause();
	}

	private void setShaders() {
		final InputStream vertexShader = getClass().getResourceAsStream("/vertex.glsl");
		final InputStream fragmentShader = getClass().getResourceAsStream("/fragment.glsl");

		try {
			glSurfaceView.registerProgram(new Program(new VertexShader(vertexShader), new FragmentShader(fragmentShader)));
		} catch (Exception ex) {
			throw new GLException("Failed to open shader source files.", ex);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
		setShaders();
	}

	protected final void registerView(Viewable view) {
		for (PrimitiveData p : view) {
			glSurfaceView.registerPrimitiveData(p);
		}
	}

	protected final void unregisterView(Viewable view) {
		for (PrimitiveData p : view) {
			glSurfaceView.unregisterPrimitiveData(p);
		}
	}
}
