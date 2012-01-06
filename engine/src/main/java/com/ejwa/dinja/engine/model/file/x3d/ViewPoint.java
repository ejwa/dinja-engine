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
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.simpleframework.xml.Attribute;

public class ViewPoint extends BaseDef implements Transformable {
	@Attribute
	private String centerOfRotation;

	@Attribute
	private float fieldOfView;

	@Attribute
	private String orientation;

	@Attribute
	private String position;

	public String getCenterOfRotation() {
		return centerOfRotation;
	}

	public Vector3f getCenterOfRotationVector() {
		return StringConverter.getVector3FromString(centerOfRotation, " ");
	}

	public void setCenterOfRotation(String centerOfRotation) {
		this.centerOfRotation = centerOfRotation;
	}

	public float getFieldOfView() {
		return fieldOfView;
	}

	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}

	public String getOrientation() {
		return orientation;
	}

	public Vector4f getOrientationVector() {
		return StringConverter.getVector4FromString(orientation, " ");
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getPosition() {
		return position;
	}

	public Vector3f getPositionVector() {
		return StringConverter.getVector3FromString(position, " ");
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
