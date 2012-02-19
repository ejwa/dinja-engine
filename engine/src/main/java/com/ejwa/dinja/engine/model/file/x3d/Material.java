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

import com.ejwa.dinja.engine.model.file.StringConverter;
import org.openmali.vecmath2.Colorf;
import org.simpleframework.xml.Attribute;

public class Material {
	@Attribute(required = false)
	private float ambientIntensity = 0.2f;

	@Attribute(required = false, empty = "0.8 0.8 0.8")
	private String diffuseColor;

	@Attribute(required = false)
	private float shininess = 0.2f;

	@Attribute(required = false, empty = "0 0 0")
	private String specularColor;

	@Attribute(required = false)
	private float transparency = 0;

	public float getAmbientIntensity() {
		return ambientIntensity;
	}

	public void setAmbientIntensity(float ambientIntensity) {
		this.ambientIntensity = ambientIntensity;
	}

	public String getDiffuseColor() {
		return diffuseColor;
	}

	public Colorf getDiffuseColorVector() {
		return StringConverter.getColor3FromString(diffuseColor, " ");
	}

	public void setDiffuseColor(String diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public String getSpecularColor() {
		return specularColor;
	}

	public Colorf getSpecularColorVector() {
		return StringConverter.getColor3FromString(specularColor, " ");
	}

	public void setSpecularColor(String specularColor) {
		this.specularColor = specularColor;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}
}
