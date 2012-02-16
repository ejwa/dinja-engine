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
package com.ejwa.dinja.utility.xml;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XMLReader<T> {
	private T xmlObject;

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	protected T read(Class<T> c, AssetManager assetManager, String xmlFile) {
		if (xmlObject == null) {
			final Serializer serializer = new Persister();
			InputStream xmlStream = null;

			try {
				xmlStream = assetManager.open(xmlFile);
				xmlObject = serializer.read(c, xmlStream);
			} catch (Exception ex) {
				Log.e(XMLReader.class.getName(), String.format("Could not de-serialize '%s'", xmlFile));
				Log.e(XMLReader.class.getName(), ex.getMessage());
			} finally {
				try {
					if (xmlStream != null) {
						xmlStream.close();
					}
				} catch (IOException ex) {
					Log.e(XMLReader.class.getName(), String.format("Couldn't close '%s'", xmlFile));
					Log.e(XMLReader.class.getName(), ex.getMessage());
				}
			}
		}

		return xmlObject;
	}
}
