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
package com.ejwa.dinja.opengles.display.draw;

import android.opengl.GLSurfaceView;
import com.ejwa.dinja.opengles.Capability;
import com.ejwa.dinja.opengles.DataType;
import com.ejwa.dinja.opengles.display.ISurfaceChangeListener;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import com.ejwa.dinja.opengles.shader.argument.UniformTuple3i;
import com.ejwa.dinja.opengles.texture.TextureFormat;
import com.ejwa.dinja.opengles.texture.TextureType;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmali.vecmath2.Point2i;
import org.openmali.vecmath2.Tuple3i;

public class SelectionDraw extends AbstractDraw implements IFramePeekListener, ISurfaceChangeListener {
	static { Loader.load(OpenGLES2Native.class); }

	private static final String VERTEX_SHADER = "/click_vertex.glsl";
	private static final String FRAGMENT_SHADER = "/click_fragment.glsl";
	private static final String COLOR_UNIFORM_NAME = "uColor";

	private final Map<Tuple3i, PrimitiveData> selectionColors = new HashMap<Tuple3i, PrimitiveData>();
	private final List<ISelectionDrawListener> selectionDrawListeners = Collections.synchronizedList(new ArrayList<ISelectionDrawListener>());
	private final Point2i selectedPoint = new Point2i();
	private final BytePointer selectedPixel = new BytePointer(4);
	private final Tuple3i selectedColor = new Tuple3i();
	private PrimitiveData selectedPrimitiveData;
	private int surfaceHeight;

	public SelectionDraw() {
		super(new Program(VERTEX_SHADER, FRAGMENT_SHADER));
	}

	public void registerSelectionDrawListener(ISelectionDrawListener selectionDrawListener) {
		if (!selectionDrawListeners.contains(selectionDrawListener)) {
			selectionDrawListeners.add(selectionDrawListener);
		}
	}

	public void unregisterSelectionDrawListener(ISelectionDrawListener selectionDrawListener) {
		selectionDrawListeners.remove(selectionDrawListener);
	}

	public List<ISelectionDrawListener> getSelectionDrawListeners() {
		return selectionDrawListeners;
	}

	@Override
	@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidInstantiatingObjectsInLoops"})
	public void onPrepareDraw(List<PrimitiveData> primitiveDataList) {
		int r = 150;
		int g = 200;
		int b = 0;

		for (int i = 0; i < primitiveDataList.size(); i++) {
			final PrimitiveData pd = primitiveDataList.get(i);
			final Tuple3i color = new Tuple3i();

			color.setX(r % 256);
			color.setY(g % 256);
			color.setZ(b % 256);

			pd.addUniform(new UniformTuple3i(COLOR_UNIFORM_NAME, color));
			selectionColors.put(color, pd);

			if (r++ == 255 && g++ == 255) {
				b++;
			}
		}

		super.onPrepareDraw(primitiveDataList);
	}

	private void refreshSelectionPoints() {
		synchronized(selectionDrawListeners) {
			for (int i = 0; i < selectionDrawListeners.size(); i++) {
				selectionDrawListeners.get(i).onRefreshSelectedPoint(selectedPoint);
			}
		}

	}

	private void refreshSelectedPrimitiveData() {
		selectedColor.set(selectedPixel.get(0) & 0xff, selectedPixel.get(1) & 0xff, selectedPixel.get(2) & 0xff);
		final PrimitiveData pd = selectionColors.get(selectedColor);

		if (selectedPrimitiveData == null || !selectedPrimitiveData.equals(pd)) {
			selectedPrimitiveData = pd;

			synchronized(selectionDrawListeners) {
				for (int i = 0; i < selectionDrawListeners.size(); i++) {
					selectionDrawListeners.get(i).onSelectedPrimitiveData(pd);
				}
			}
		}
	}

	@Override
	public void onDrawFrame(List<PrimitiveData> primitiveDataList) {
		Capability.GL_CULL_FACE.enable();
		Capability.GL_DEPTH_TEST.enable();
		Capability.GL_SCISSOR_TEST.enable();

		program.use();
		refreshSelectionPoints();

		synchronized (this) {
			OpenGLES2Native.glScissor(selectedPoint.getX(), surfaceHeight - selectedPoint.getY(), 1, 1);
		}

		for (int i = 0; i < primitiveDataList.size(); i++) {
			final PrimitiveData pd = primitiveDataList.get(i);
			final Pointer indices =  pd.getIndices();
			final DataType indicesType = pd.getVertices().getData().capacity() / 3 >= 256 ? DataType.GL_UNSIGNED_SHORT : DataType.GL_UNSIGNED_BYTE;
			final int vertexAttributeHandle = program.getVertexAttributeHandle(pd.getVertices().getVariableName());


			if (vertexAttributeHandle != -1) {
				OpenGLES2Native.glVertexAttribPointer(vertexAttributeHandle, 3, DataType.GL_FLOAT.getId() , false, 0, pd.getVertices().getData());
				OpenGLES2Native.glEnableVertexAttribArray(vertexAttributeHandle);
			}

			for (int j = 0; j < pd.getUniforms().size(); j++) {
				final AbstractUniform u = pd.getUniforms().get(j);
				final int uniformHandle = program.getUniformHandle(u.getVariableName());
				u.send(uniformHandle);
			}

			OpenGLES2Native.glDrawElements(pd.getPrimitiveType().getId(), indices.capacity(), indicesType.getId(), indices);
		}

		Capability.GL_CULL_FACE.disable();
		Capability.GL_DEPTH_TEST.disable();
		Capability.GL_SCISSOR_TEST.disable();
	}

	@Override
	public void onPeekFrame(List<PrimitiveData> primitiveDataList) {
		synchronized (this) {
			OpenGLES2Native.glReadPixels(selectedPoint.getX(), surfaceHeight - selectedPoint.getY(), 1, 1,
			                             TextureFormat.GL_RGBA.getId(), TextureType.GL_UNSIGNED_BYTE.getId(), selectedPixel);
		}

		refreshSelectedPrimitiveData();
	}

	@Override
	public void onSurfaceChange(GLSurfaceView glSurfaceView) {
		synchronized (this) {
			surfaceHeight = glSurfaceView.getHeight();
		}
	}
}
