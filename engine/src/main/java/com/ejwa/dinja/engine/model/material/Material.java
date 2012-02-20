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
package com.ejwa.dinja.engine.model.material;

import com.ejwa.dinja.engine.model.node.mesh.MeshPrimitiveData;
import com.ejwa.dinja.opengles.ActiveTexture;
import com.ejwa.dinja.opengles.blend.BlendDestinationFactor;
import com.ejwa.dinja.opengles.blend.BlendSourceFactor;
import com.ejwa.dinja.opengles.blend.Blending;
import com.ejwa.dinja.opengles.shader.argument.TextureRGB565Sampler;
import com.ejwa.dinja.opengles.shader.argument.TextureRGBA8888Sampler;
import java.awt.Color;
import org.openmali.vecmath2.Colorf;

public class Material {
	private static final float DEFAULT_AMBIENT_INTENSITY = 0.2f;
	private static final Color DEFAULT_DIFFUSE_COLOR = Color.GRAY;
	private static final Color DEFAULT_SPECULAR_COLOR = Color.WHITE;
        private static final float DEFAULT_SHININESS = 0.5f;

	private Colorf ambientIntensity = new Colorf(DEFAULT_AMBIENT_INTENSITY);
	private Colorf diffuseColor = new Colorf(DEFAULT_DIFFUSE_COLOR);
	private Colorf specularColor = new Colorf(DEFAULT_SPECULAR_COLOR);
        private float shininess = DEFAULT_SHININESS;

	private final MeshPrimitiveData meshPrimitiveData;
	private Texture texture;

	public Material(MeshPrimitiveData meshPrimitiveData) {
		super();
		this.meshPrimitiveData = meshPrimitiveData;
	}

	public Colorf getAmbientIntensity() {
		return ambientIntensity;
	}

	public void setAmbientIntensity(Colorf ambientIntensity) {
		this.ambientIntensity = ambientIntensity;
	}

	public Colorf getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Colorf diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Colorf getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Colorf specularColor) {
		this.specularColor = specularColor;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;

		if (texture.isHasAlpha()) {
			meshPrimitiveData.addSampler(new TextureRGBA8888Sampler(MeshPrimitiveData.TEXTURE_SAMPLER_NAME,
			                             ActiveTexture.GL_TEXTURE0, texture.getWidth(), texture.getHeight(),
			                             texture.getPixelsRGBA8888()));
			meshPrimitiveData.setBlending(new Blending(BlendSourceFactor.GL_SRC_ALPHA,
			                              BlendDestinationFactor.GL_ONE_MINUS_SRC_ALPHA));
		} else  {
			meshPrimitiveData.addSampler(new TextureRGB565Sampler(MeshPrimitiveData.TEXTURE_SAMPLER_NAME,
			                             ActiveTexture.GL_TEXTURE0, texture.getWidth(), texture.getHeight(),
			                             texture.getPixelsRGB565()));
		}
	}
}
