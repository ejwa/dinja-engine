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
package com.ejwa.dinja.engine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;
import android.view.MotionEvent;
import com.ejwa.dinja.engine.controller.CameraController;
import com.ejwa.dinja.engine.controller.Controllable;
import com.ejwa.dinja.engine.controller.DebugController;
import com.ejwa.dinja.engine.controller.SceneController;
import com.ejwa.dinja.engine.controller.animator.AnimatorController;
import com.ejwa.dinja.engine.controller.animator.IAnimator;
import com.ejwa.dinja.engine.controller.input.FingerMovementInputController;
import com.ejwa.dinja.engine.controller.input.FingerPositionInputController;
import com.ejwa.dinja.engine.controller.input.IFingerMovementInputListener;
import com.ejwa.dinja.engine.controller.input.IFingerPositionInputListener;
import com.ejwa.dinja.engine.controller.input.ITiltForceInputListener;
import com.ejwa.dinja.engine.controller.input.TiltForceInputController;
import com.ejwa.dinja.engine.view.DebugView;
import com.ejwa.dinja.engine.view.SceneView;
import com.ejwa.dinja.engine.view.Viewable;
import com.ejwa.dinja.opengles.GLException;
import com.ejwa.dinja.opengles.display.GLSurface;
import com.ejwa.dinja.opengles.display.IFrameTimeListener;
import com.ejwa.dinja.opengles.display.IFrameUpdateListener;
import com.ejwa.dinja.opengles.display.ISurfaceChangeListener;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.FragmentShader;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.VertexShader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.openmali.FastMath;

/**
 * The base class for a dinja engine activity, which all applications should use.
 * <code>DinjaActivity</code> handles all the "glue" needed to be able to create a GL surface, register views and controllers.
 *
 * @author Adam Waldenberg <adam.waldenberg@ejwa.se>
 * @since 0.1
 */
public class DinjaActivity extends Activity {
	private static final int FASTMATH_PRECISION = 0x4000;

	private GLSurface glSurfaceView;
	private Map<ITiltForceInputListener, TiltForceInputController> tiltForceListeners;
	private Map<IFingerPositionInputListener, FingerPositionInputController> fingerPositionListeners;
	private Map<IFingerMovementInputListener, FingerMovementInputController> fingerMovementListeners;

	/**
	 * Called when this dinja activity is starting. This is where most initialization is done. By default,
	 * <code>DinjaActivity</code> initializes the GL surface here. If you override this method (and you probably will), you
	 * should call <code>super.onCreate(savedInstanceState)</code> in your overriding <code>onCreate()</code> method.
	 *
	 * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle
	 *                           contains the data it most recently supplied in onSaveInstanceState(Bundle).
	 *                           <b>Note: Otherwise it is null.</b>
	 * @see Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tiltForceListeners = new HashMap<ITiltForceInputListener, TiltForceInputController>();
		fingerPositionListeners = new HashMap<IFingerPositionInputListener, FingerPositionInputController>();
		fingerMovementListeners = new HashMap<IFingerMovementInputListener, FingerMovementInputController>();

		FastMath.setPrecision(FASTMATH_PRECISION);
		glSurfaceView = new GLSurface(getApplication());
		setContentView(glSurfaceView);
	}

	/**
	 * Called as part of the activity lifecycle when the dinja activity is going into the background, but has not (yet) been
	 * killed. Most applications will not have to consider overriding this method. <code>DinjaActivity</code> pauses the
	 * GL Surface here. If you override this method, you should call <code>super.onPause()</code> in your overriding
	 * <code>onPause()</code> method.
	 *
	 * @see Activity#onPause()
	 */
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

	/**
	 * Called after <code>onCreate()</code> or after, <code>onPause()</code>. Everything that should happen after the dinja
	 * activity is created or after it has been inactive, should be done here. <code>DinjaActivity</code> resumes the GL
	 * surface here. It also sets up shaders and threads. If you override this method, you should call
	 * <code>super.onResume()</code> in your overriding <code>onResume()</code> method.
	 *
	 * @see Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
		Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);
		setShaders();
	}

	/**
	 * Registers a controller object to this activity. A controller is usually linked to a model object or a view. What is
	 * important is that the passed object implements the {@link Controllable} interface.
	 *
	 * @param controllable A controller object implementing the {@link Controllable} interface.
	 * @see #unregisterController
	 * @see Controllable
	 * @see <a href="http://en.wikipedia.org/wiki/Model-View-Controller">Wikipedia.org: Model-View-Controller</a>
	 */
	protected final void registerController(Controllable controllable) {
		if (controllable instanceof IFrameTimeListener) {
			glSurfaceView.registerFrameTimeListener((IFrameTimeListener) controllable);
		}

		if (controllable instanceof IFrameUpdateListener) {
			if (controllable instanceof IAnimator) {
				glSurfaceView.registerFrameUpdateListener(new AnimatorController((IAnimator) controllable, glSurfaceView));
			} else {
				glSurfaceView.registerFrameUpdateListener((IFrameUpdateListener) controllable);
			}
		}

		if (controllable instanceof ISurfaceChangeListener) {
			glSurfaceView.registerSurfaceChangeListener((ISurfaceChangeListener) controllable);
		}

		if (controllable instanceof ITiltForceInputListener) {
			final TiltForceInputController input = new TiltForceInputController(this, (ITiltForceInputListener) controllable);
			tiltForceListeners.put((ITiltForceInputListener) controllable, input);
		}

		if (controllable instanceof IFingerPositionInputListener) {
			final FingerPositionInputController input = new FingerPositionInputController((IFingerPositionInputListener) controllable, glSurfaceView);
			fingerPositionListeners.put((IFingerPositionInputListener) controllable, input);
		}

		if (controllable instanceof IFingerMovementInputListener) {
			final FingerMovementInputController input = new FingerMovementInputController((IFingerMovementInputListener) controllable, glSurfaceView);
			fingerMovementListeners.put((IFingerMovementInputListener) controllable, input);
		}
	}

	/**
	 * Unregisters a a controller object previously registered with {@link #registerController}.
	 *
	 * @param controllable A controller object implementing the {@link Controllable} interface.
	 * @see #registerController
	 * @see Controllable
	 * @see <a href="http://en.wikipedia.org/wiki/Model-View-Controller">Wikipedia.org: Model-View-Controller</a>
	 */
	protected final void unregisterController(Controllable controllable) {
		if (controllable instanceof IFrameTimeListener) {
			glSurfaceView.unregisterFrameTimeListener((IFrameTimeListener) controllable);
		}

		if (controllable instanceof IFrameUpdateListener) {
			glSurfaceView.unregisterFrameUpdateListener((IFrameUpdateListener) controllable);
		}

		if (controllable instanceof ISurfaceChangeListener) {
			glSurfaceView.unregisterSurfaceChangeListener((ISurfaceChangeListener) controllable);
		}

		if (controllable instanceof ITiltForceInputListener) {
			final TiltForceInputController input = tiltForceListeners.get((ITiltForceInputListener) controllable);
			input.unregister();
			tiltForceListeners.remove((ITiltForceInputListener) controllable);
		}

		if (controllable instanceof IFingerPositionInputListener) {
			fingerPositionListeners.remove((IFingerPositionInputListener) controllable);
		}

		if (controllable instanceof IFingerMovementInputListener) {
			fingerMovementListeners.remove((IFingerMovementInputListener) controllable);
		}
	}

	/**
	 * Registers a view object to this activity. A view usually displays something related to the model and is most often
	 * linked to one or several model objects. What is important is that the passed object implements the {@link Viewable}
	 * interface.
	 *
	 * @param view A view object implementing the {@link Viewable} interface.
	 * @see #unregisterView
	 * @see Viewable
	 * @see <a href="http://en.wikipedia.org/wiki/Model-View-Controller">Wikipedia.org: Model-View-Controller</a>
	 */
	protected final void registerView(Viewable view) {
		if (view instanceof SceneView) {
			final SceneView sceneView = (SceneView) view;
			registerController(new CameraController(sceneView));
			registerController(new SceneController(sceneView));
		}

		if (view instanceof DebugView) {
			registerController(new DebugController((DebugView) view));
		}

		if (view instanceof Iterable) {
			for (PrimitiveData p : (Iterable<PrimitiveData>) view) {
				glSurfaceView.registerPrimitiveData(p);
			}
		}
	}

	/**
	 * Unregisters a a view object previously registered with {@link #registerView}.
	 *
	 * @param view A view object implementing the {@link Viewable} interface.
	 * @see #registerView
	 * @see Viewable
	 * @see <a href="http://en.wikipedia.org/wiki/Model-View-Controller">Wikipedia.org: Model-View-Controller</a>
	 */
	protected final void unregisterView(Viewable view) {
		/* TODO: Handle default controllers when unregistering certain views. */
		if (view instanceof Iterable) {
			for (PrimitiveData p : (Iterable<PrimitiveData>) view) {
				glSurfaceView.unregisterPrimitiveData(p);
			}
		}
	}

	/**
	 * Called when a touch screen event was not handled by any of the views under it. <code>DinjaActivity</code> handles
	 * input events and calls registered controllers here. It is <b>not advisable</b> to override this method. You should
	 * instead register a controller using {@link #registerController}. If none of the controllers or controller interfaces
	 * included in the engine suit your needs, you can always make your own.
	 *
	 * @param event The touch screen event being processed.
	 * @return Always returns <code>true</code>, as the event is always considered as handled.
	 * @see Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (FingerPositionInputController c : fingerPositionListeners.values()) {
			c.onTouchEvent(event);
		}

		for (FingerMovementInputController c : fingerMovementListeners.values()) {
			c.onTouchEvent(event);
		}

		return true;
	}
}
