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

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GLSurface extends GLSurfaceView {
	private final List<PrimitiveData> primitiveDatas = Collections.synchronizedList(new ArrayList<PrimitiveData>());
	private final List<Program> programs = Collections.synchronizedList(new ArrayList<Program>());

	public GLSurface(Context context) {
		super(context);
		setEGLContextFactory(new ContextFactory());
		setEGLConfigChooser(new DisplayConfigurationChooser());
		setRenderer(new GLSurfaceRenderer(this));
	}

	public void registerPrimitiveData(PrimitiveData primitiveData) {
		primitiveDatas.add(primitiveData);
	}

	public void unregisterPrimitiveData(PrimitiveData primitiveData) {
		primitiveDatas.remove(primitiveData);
	}

	public List<PrimitiveData> getPrimitiveDatas() {
		return primitiveDatas;
	}

	public void registerProgram(Program program) {
		programs.add(program);
	}

	public void unregisterProgram(Program program) {
		programs.remove(program);
	}

	public List<Program> getPrograms() {
		return programs;
	}
}
