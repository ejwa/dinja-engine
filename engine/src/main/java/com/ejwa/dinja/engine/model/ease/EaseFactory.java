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

import java.util.HashMap;
import java.util.Map;

public final class EaseFactory {
	private static final Map<Class<? extends IEase>, IEase> EASE_OBJECTS = new HashMap<Class<? extends IEase>, IEase>();

	private EaseFactory() {
		/* No instances of this class allowed. */
	}

	public static IEase get(Class<? extends IEase> easeClass) {
		IEase easeObject = EASE_OBJECTS.get(easeClass);

		if (easeObject == null) {
			try {
				easeObject = easeClass.newInstance();
				EASE_OBJECTS.put(easeClass, easeObject);
				return easeObject;
			} catch (Exception ex) {
				throw new IllegalStateException("Failed to create instance for easing factory.", ex);
			}
		}

		return easeObject;
	}
}
