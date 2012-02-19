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
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class IndexedFaceSet {
	@Attribute
	private String coordIndex;

	@Attribute(required = false)
	private boolean colorPerVertex = true;

	@Attribute(required = false)
	private boolean convex = true;

	@Attribute(required = false)
	private boolean normalPerVertex = true;

	@Attribute(required = false)
	private boolean solid = true;

	@Attribute
	private String texCoordIndex;

	@Element(name = "Color", required = false)
	private Color color;

	@Element(name = "Coordinate")
	private Coordinate coordinate;

	@Element(name = "Normal", required = false)
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

	public boolean isColorPerVertex() {
		return colorPerVertex;
	}

	public void setColorPerVertex(boolean colorPerVertex) {
		this.colorPerVertex = colorPerVertex;
	}

	public boolean isConvex() {
		return convex;
	}

	public void setConvex(boolean convex) {
		this.convex = convex;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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
