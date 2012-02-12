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
import com.ejwa.dinja.engine.model.node.scene.Scene;
import com.ejwa.dinja.engine.model.node.mesh.Mesh;
import com.ejwa.dinja.engine.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public class DebugView implements Viewable {
	private final Activity activity;
	private final float updatesPerSecond;
	private final List<Tuple<Camera, TextView>> cameraDebugTexts = new ArrayList<Tuple<Camera, TextView>>();
	private final List<Tuple<Mesh, TextView>> meshDebugTexts = new ArrayList<Tuple<Mesh, TextView>>();
	private final List<Tuple<Scene, TextView>> sceneDebugTexts = new ArrayList<Tuple<Scene, TextView>>();

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
				cameraDebugTexts.add(new Tuple((Camera) o, textView));
			} else if (o instanceof Mesh) {
				meshDebugTexts.add(new Tuple((Mesh) o, textView));
			} else if (o instanceof Scene) {
				sceneDebugTexts.add(new Tuple((Scene) o, textView));
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

	public List<Tuple<Camera, TextView>> getCameraDebugTexts() {
		return cameraDebugTexts;
	}

	public List<Tuple<Mesh, TextView>> getMeshDebugTexts() {
		return meshDebugTexts;
	}

	public List<Tuple<Scene, TextView>> getSceneDebugTexts() {
		return sceneDebugTexts;
	}
}
