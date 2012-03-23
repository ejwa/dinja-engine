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
package com.ejwa.dinja.demo.activity;

import android.os.Bundle;
import android.util.Log;
import com.ejwa.dinja.engine.activity.DinjaActivity;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.googlecode.javacpp.IntPointer;
import java.nio.IntBuffer;

public class BasicPhysicsActivity extends DinjaActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//final Sphere sphere = new Sphere("sphere", 1, 8, 8);
		//final Scene scene = new Scene(new Camera(), new Physics("physics", sphere));

		//registerView(new SceneView(scene));
	}
}
