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
package com.ejwa.dinja.engine.model.node;

import com.ejwa.dinja.engine.model.transform.Rotatable;
import com.ejwa.dinja.engine.model.transform.Scalable;
import com.ejwa.dinja.engine.model.transform.Translatable;
import com.ejwa.dinja.engine.model.transform.Rotator;
import com.ejwa.dinja.engine.model.transform.Scaler;
import com.ejwa.dinja.engine.model.transform.Translator;

public class Group extends BaseNode implements Rotatable, Scalable, Translatable {
	private final Translator translator = new Translator(modelMatrix);
	private final Rotator rotator = new Rotator(modelMatrix, translator.get());
	private final Scaler scaler = new Scaler(modelMatrix);

	public Group(String name) {
		super(name);
	}

	@Override
	public Translator getTranslator() {
		return translator;
	}

	@Override
	public Rotator getRotator() {
		return rotator;
	}

	@Override
	public Scaler getScaler() {
		return scaler;
	}
}
