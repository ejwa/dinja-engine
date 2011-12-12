/*
 * Copyright © 2011 Ejwa Software. All rights reserved.
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
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;

public class Transform extends BaseDef implements Transformable {
	@Attribute
	private String rotation;

	@Attribute
	private String scale;

	@Attribute
	private String translation;

	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
	@ElementUnion({
		@Element(name = "Group", type = Group.class),
		@Element(name = "PointLight", type = PointLight.class),
		@Element(name = "Viewpoint", type = ViewPoint.class),
		@Element(name = "Transform", type = Transform.class)
	})
	private Transformable transformable;

	public String getRotation() {
		return rotation;
	}

	public Vector4f getRotationVector() {
		return StringConverter.getVector4FromString(rotation, " ");
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public String getScale() {
		return scale;
	}

	public Vector3f getScaleVector() {
		return StringConverter.getVector3FromString(scale, " ");
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getTranslation() {
		return translation;
	}

	public Vector3f getTranslationVector() {
		return StringConverter.getVector3FromString(translation, " ");
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public Transformable getTransformable() {
		return transformable;
	}

	public void setTransformable(Transformable transformable) {
		this.transformable = transformable;
	}
}
