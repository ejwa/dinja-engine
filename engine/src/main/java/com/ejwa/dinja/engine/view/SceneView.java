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
package com.ejwa.dinja.engine.view;

import android.util.Log;
import com.ejwa.dinja.engine.model.Scene;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import java.util.Iterator;

public class SceneView implements Viewable {
	private final Scene scene;

	public SceneView(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	public Iterator<PrimitiveData> iterator() {
		return new SceneViewIterator();
	}

	private class SceneViewIterator implements Iterator<PrimitiveData> {
		private int pos = 0;

		@Override
		public boolean hasNext() {
			return scene.getMeshes().size() > pos;
		}

		@Override
		public PrimitiveData next() {
			return scene.getMeshes().get(pos++).getPrimitiveData();
		}

		@Override
		public void remove() {
			scene.getMeshes().remove(pos);
		}
	}
}
