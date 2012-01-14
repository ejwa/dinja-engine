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
package com.ejwa.dinja.engine.view;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ejwa.dinja.engine.model.Camera;
import com.ejwa.dinja.engine.model.Scene;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import java.util.HashMap;
import java.util.Map;

public class DebugView implements Viewable {
	private final Activity activity;
	private final float updatesPerSecond;
	private final Map<Camera, TextView> cameraDebugTexts = new HashMap<Camera, TextView>();
	private final Map<Mesh, TextView> meshDebugTexts = new HashMap<Mesh, TextView>();
	private final Map<Scene, TextView> sceneDebugTexts = new HashMap<Scene, TextView>();

	public DebugView(Activity activity, Object ...debuggableObjects) {
		this(1, activity ,debuggableObjects);
	}

	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public DebugView(float updatesPerSecond, Activity activity, Object ...debuggableObjects) {
		this.activity = activity;
		this.updatesPerSecond = updatesPerSecond;

		final LinearLayout rootLayout = new LinearLayout(activity);
		rootLayout.setOrientation(LinearLayout.VERTICAL);

		for (Object o : debuggableObjects) {
			final TextView textView = new TextView(activity);

			if (o instanceof Camera) {
				cameraDebugTexts.put((Camera) o, textView);
			} else if (o instanceof Mesh) {
				meshDebugTexts.put((Mesh) o, textView);
			} else if (o instanceof Scene) {
				sceneDebugTexts.put((Scene) o, textView);
			} else {
				throw new IllegalArgumentException("Debuggable objects need to be one of the following: " +
				                                   "Camera, Mesh or Scene.");
			}

			rootLayout.addView(textView);
		}

		activity.addContentView(rootLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		                        ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public Activity getActivity() {
		return activity;
	}

	public float getUpdatesPerSecond() {
		return updatesPerSecond;
	}

	public Map<Camera, TextView> getCameraDebugTexts() {
		return cameraDebugTexts;
	}

	public Map<Mesh, TextView> getMeshDebugTexts() {
		return meshDebugTexts;
	}

	public Map<Scene, TextView> getSceneDebugTexts() {
		return sceneDebugTexts;
	}
}
