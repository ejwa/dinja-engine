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

import com.ejwa.dinja.engine.model.ease.IEase;
import com.ejwa.dinja.engine.model.properties.Translatable;
import org.openmali.vecmath2.Vector3f;

public class TranslationAnimator extends BaseAnimator<Translatable, Vector3f> {
	public TranslationAnimator(Translatable translatable, Vector3f destination, float duration, Class<? extends IEase> ease) {
		super(translatable, translatable.getTranslator().get(), destination, duration, ease);
	}

	public TranslationAnimator(IAnimatorListener animatorListener, Translatable translatable,
	                           Vector3f destination, float duration, Class<? extends IEase> ease) {
		super(animatorListener, translatable, translatable.getTranslator().get(), destination, duration, ease);
	}

	@Override
	public void onFrameUpdate(long milliSecondsSinceLastFrame) {
		super.onFrameUpdate(milliSecondsSinceLastFrame);

		final float x = ease.getValue(time, origin.getX(), destination.getX(), duration);
		final float y = ease.getValue(time, origin.getY(), destination.getY(), duration);
		final float z = ease.getValue(time, origin.getZ(), destination.getZ(), duration);

		final Vector3f translation = Vector3f.fromPool(x, y, z);
		animatable.getTranslator().set(translation);
		Vector3f.toPool(translation);
	}
}
