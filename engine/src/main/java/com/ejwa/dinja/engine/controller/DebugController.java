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
package com.ejwa.dinja.engine.controller;

import android.os.SystemClock;
import com.ejwa.dinja.engine.model.Camera;
import com.ejwa.dinja.engine.model.Scene;
import com.ejwa.dinja.engine.model.mesh.Mesh;
import com.ejwa.dinja.engine.view.DebugView;
import com.ejwa.dinja.opengles.display.IFrameTimeListener;

public class DebugController implements Controllable, IFrameTimeListener {
	private final DebugView debugView;
	private final DebugUpdater updater = new DebugUpdater();
	private long previousStoredTime = 0;

	public DebugController(DebugView debugView) {
		this.debugView = debugView;
	}

	@Override
	public void onFrameTime(long nanoSecondsForFrame) {
		if (SystemClock.uptimeMillis() - previousStoredTime > debugView.getUpdatesPerSecond() * 1000) {
			updater.setNanoSecondsForFrame(nanoSecondsForFrame);
			debugView.getActivity().runOnUiThread(updater);
			previousStoredTime = SystemClock.uptimeMillis();
		}
	}

	private class DebugUpdater implements Runnable {
		private long nanoSecondsForFrame;

		public synchronized long getNanoSecondsForFrame() {
			return nanoSecondsForFrame;
		}

		public synchronized void setNanoSecondsForFrame(long nanoSecondsForFrame) {
			final float speed = 0.2f;
			this.nanoSecondsForFrame = (long) (nanoSecondsForFrame * speed + this.nanoSecondsForFrame * (1f - speed));
		}


		@Override
		public void run() {
			for (Camera c : debugView.getCameraDebugTexts().keySet()) {
				debugView.getCameraDebugTexts().get(c).setText("Camera view matrix:\n" + c.getViewMatrix());
			}

			for (Mesh m : debugView.getMeshDebugTexts().keySet()) {
				debugView.getMeshDebugTexts().get(m).setText(
					"Mesh name: " + m.getName() + "\n" +
					"Mesh model matrix:\n" + m.getModelMatrix()
				);
			}

			for (Scene s : debugView.getSceneDebugTexts().keySet()) {
				debugView.getSceneDebugTexts().get(s).setText(
					"Frames per second: " + (int) (1000000000f / getNanoSecondsForFrame()) + "\n" +
					"Meshes in scene: " + s.getMeshes().size()
				);
			}
		}
	}
}
