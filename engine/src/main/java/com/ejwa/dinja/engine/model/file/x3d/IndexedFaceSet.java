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
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class IndexedFaceSet {
	@Attribute
	private boolean normalPerVertex;

	@Attribute
	private String coordIndex;

	@Attribute
	private boolean solid;

	@Attribute
	private String texCoordIndex;

	@Element(name = "Coordinate")
	private Coordinate coordinate;

	@Element(name = "Normal")
	private Normal normal;

	@Element(name = "TextureCoordinate")
	private TextureCoordinate textureCoordinate;

	public String getCoordIndex() {
		return coordIndex;
	}

	public List<Integer> getCoordIndexList() {
		return StringConverter.getIntegerListFromString(coordIndex, " ");
	}

	public void setCoordIndex(String coordIndex) {
		this.coordIndex = coordIndex;
	}

	public boolean isNormalPerVertex() {
		return normalPerVertex;
	}

	public void setNormalPerVertex(boolean normalPerVertex) {
		this.normalPerVertex = normalPerVertex;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public String getTexCoordIndex() {
		return texCoordIndex;
	}

	public List<Integer> getTexCoordIndexList() {
		return StringConverter.getIntegerListFromString(texCoordIndex, " ");
	}

	public void setTexCoordIndex(String texCoordIndex) {
		this.texCoordIndex = texCoordIndex;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Normal getNormal() {
		return normal;
	}

	public void setNormal(Normal normal) {
		this.normal = normal;
	}

	public TextureCoordinate getTextureCoordinate() {
		return textureCoordinate;
	}

	public void setTextureCoordinate(TextureCoordinate textureCoordinate) {
		this.textureCoordinate = textureCoordinate;
	}
}
