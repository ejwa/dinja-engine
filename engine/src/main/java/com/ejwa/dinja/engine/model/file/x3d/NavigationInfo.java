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
package com.ejwa.dinja.engine.model.file.x3d;

import com.ejwa.dinja.engine.util.StringConverter;
import org.openmali.vecmath2.Vector3f;
import org.simpleframework.xml.Attribute;

public class NavigationInfo {
	@Attribute
	private String avatarSize;

	@Attribute
	private boolean headlight;

	@Attribute
        private String type;

	@Attribute
	private float visibilityLimit;

	public String getAvatarSize() {
		return avatarSize;
	}

	public Vector3f getAvatarSizeVector() {
		return StringConverter.getVector3FromString(avatarSize, " ");
	}

	public void setAvatarSize(String avatarSize) {
		this.avatarSize = avatarSize;
	}

	public boolean isHeadlight() {
		return headlight;
	}

	public void setHeadlight(boolean headlight) {
		this.headlight = headlight;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getVisibilityLimit() {
		return visibilityLimit;
	}

	public void setVisibilityLimit(float visibilityLimit) {
		this.visibilityLimit = visibilityLimit;
	}
}
