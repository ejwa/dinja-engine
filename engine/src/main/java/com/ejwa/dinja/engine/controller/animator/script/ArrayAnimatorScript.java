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
package com.ejwa.dinja.engine.controller.animator.script;

import com.ejwa.dinja.engine.controller.Controllable;
import com.ejwa.dinja.engine.controller.animator.IAnimator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayAnimatorScript extends AbstractAnimatorScript implements Controllable, IAnimator {
	private final List<IAnimator> animators;
	private int currentAnimatorIndex;

	public ArrayAnimatorScript(IAnimator ...animators) {
		super(animators[0]);
		this.animators = Arrays.asList(animators);
	}

	@Override
	public boolean isCompleted() {
		return animator.isCompleted() && currentAnimatorIndex >= animators.size();
	}

	@Override
	public void restart() {
		currentAnimatorIndex = 0;
		updateAnimator();

		for (IAnimator a : animators) {
			a.restart();
		}
	}

	@Override
	public void reverse() {
		Collections.reverse(animators);
		currentAnimatorIndex = 0;
		updateAnimator();

		for (IAnimator a : animators) {
			a.reverse();
		}
	}

	private void updateAnimator() {
		if (currentAnimatorIndex < animators.size()) {
			animator = animators.get(currentAnimatorIndex);
		}
	}

	@Override
	public void onFrameUpdate(long milliSecondsSinceLastFrame) {
		animator.onFrameUpdate(milliSecondsSinceLastFrame);

		if (animator.isCompleted()) {
			currentAnimatorIndex++;
			updateAnimator();
		}
	}
}
