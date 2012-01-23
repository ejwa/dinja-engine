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
package com.ejwa.dinja.engine.model.ease;

public class QuadraticInOutEase extends AbstractEase implements IEase {
	@Override
	public float getValue(float timeElapsed, float startValue, float endValue, float duration) {
		float percent = timeElapsed / (duration / 2);
		float value;

		if (percent < 1) {
			value = (endValue - startValue) / 2 * percent * percent + startValue;
		} else {
			percent--;
			value = -(endValue - startValue) / 2 * (percent * (percent - 2) - 1) + startValue;
		}

		return clamp(startValue, endValue, value);
	}
}
