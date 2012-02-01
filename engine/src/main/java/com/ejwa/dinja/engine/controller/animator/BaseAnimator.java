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
package com.ejwa.dinja.engine.controller.animator;

import com.ejwa.dinja.engine.controller.Controllable;
import com.ejwa.dinja.engine.model.ease.EaseFactory;
import com.ejwa.dinja.engine.model.ease.IEase;

public class BaseAnimator<A, T> implements Controllable, IAnimator {
	private IAnimatorListener animatorListener;
	private boolean completed;
	private boolean paused;

	protected final A animatable;
	protected T origin;
	protected T destination;
	protected final float duration;
	protected IEase ease;
	protected float time;

	public BaseAnimator(IAnimatorListener animatorListener, A animatable, T origin, T destination,
	                        float duration, Class<? extends IEase> ease) {
		this(animatable, origin, destination, duration, ease);
		this.animatorListener = animatorListener;
	}

	public BaseAnimator(IAnimatorListener animatorListener, A animatable, T origin, T destination, float duration) {
		this(animatable, origin, destination, duration);
		this.animatorListener = animatorListener;
	}

	public BaseAnimator(A animatable, T origin, T destination, float duration, Class<? extends IEase> ease) {
		this(animatable, origin, destination, duration);
		this.ease = EaseFactory.get(ease);
	}

	public BaseAnimator(A animatable, T origin, T destination, float duration) {
		this.animatable = animatable;
		this.duration = duration;
		this.origin = origin;
		this.destination = destination;
	}

	@Override
	public synchronized IAnimatorListener getAnimatorListener() {
		return animatorListener;
	}

	@Override
	public synchronized void pause() {
		if (!paused && animatorListener instanceof IAnimatorPausListener) {
			paused = true;

			if (animatorListener instanceof IAnimatorPausListener) {
				((IAnimatorPausListener) animatorListener).onAnimatorPaused();
			}
		}
	}

	@Override
	public synchronized void restart() {
		resume();
		setCompleted(false);
		time = 0;
	}

	@Override
	public synchronized void resume() {
		if (paused) {
			paused = false;

			if (animatorListener instanceof IAnimatorPausListener) {
				((IAnimatorPausListener) animatorListener).onAnimatorResumed();
			}
		}
	}

	@Override
	public synchronized void reverse() {
		final T tmp = origin;

		origin = destination;
		destination = tmp;
		restart();
	}

	@Override
	public synchronized boolean isCompleted() {
		return completed;
	}

	public synchronized void setCompleted(boolean completed) {
		this.completed = completed;
	}

	private void complete() {
		if (!completed) {
			completed = true;

			if (animatorListener != null) {
				animatorListener.onAnimatorCompleted();
			}
		}
	}

	@Override
	public synchronized void onFrameUpdate(long milliSecondsSinceLastFrame) {
		if (!paused && !completed) {
			if (animatorListener != null && time == 0) {
				animatorListener.onAnimatorStarted();
			}

			if (time < duration) {
				time += milliSecondsSinceLastFrame / 1000f;
			} else {
				time = duration;
				complete();
			}
		}
	}
}
