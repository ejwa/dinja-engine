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
package com.ejwa.dinja.opengles.display;

import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;
import com.ejwa.dinja.opengles.Capability;
import com.ejwa.dinja.opengles.DataType;
import com.ejwa.dinja.opengles.library.OpenGLES2Native;
import com.ejwa.dinja.opengles.Property;
import com.ejwa.dinja.opengles.primitive.PrimitiveData;
import com.ejwa.dinja.opengles.shader.Program;
import com.ejwa.dinja.opengles.shader.argument.AbstractSampler;
import com.ejwa.dinja.opengles.shader.argument.AbstractUniform;
import com.ejwa.dinja.opengles.shader.argument.AbstractVertexAttributeArray;
import com.googlecode.javacpp.Pointer;
import java.util.Set;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceRenderer implements Renderer {
	private long previousFrameTime = -1;
	private final GLSurface glSurface;

	public GLSurfaceRenderer(GLSurface glSurface) {
		this.glSurface = glSurface;
	}

	private long getMilliSecondsSinceLastFrame() {
		if (previousFrameTime == -1) {
			previousFrameTime = SystemClock.uptimeMillis();
			return 0;
		} else {
			final long msSinceLastFrame;

			msSinceLastFrame = SystemClock.uptimeMillis() - previousFrameTime;
			previousFrameTime = SystemClock.uptimeMillis();
			return msSinceLastFrame;
		}
	}

	private static void drawElements(Program program, PrimitiveData primitiveData) {
		final Pointer indices =  primitiveData.getIndices();
		final DataType indicesType = primitiveData.getVertices().getData().capacity() / 3 >= 256 ? DataType.GL_UNSIGNED_SHORT : DataType.GL_UNSIGNED_BYTE;
		final int vertexAttributeHandle = program.getVertexAttributeHandle(primitiveData.getVertices().getVariableName());

		if (vertexAttributeHandle != -1) {
			OpenGLES2Native.glVertexAttribPointer(vertexAttributeHandle, 3, DataType.GL_FLOAT.getId() , false, 0, primitiveData.getVertices().getData());
			OpenGLES2Native.glEnableVertexAttribArray(vertexAttributeHandle);
		}

		for (int i = 0; i < primitiveData.getUniforms().size(); i++) {
			final AbstractUniform u = primitiveData.getUniforms().get(i);
			final int uniformHandle = program.getUniformHandle(u.getVariableName());
			u.send(uniformHandle);
		}

		for (int i = 0; i < primitiveData.getVertexAttributeArrays().size(); i++) {
			final AbstractVertexAttributeArray va = primitiveData.getVertexAttributeArrays().get(i);
			final int vAttributeArrayHandle = program.getVertexAttributeHandle(va.getVariableName());
			OpenGLES2Native.glVertexAttribPointer(vAttributeArrayHandle, va.getComponents(), DataType.GL_FLOAT.getId(), false, 0, va.getData());
			OpenGLES2Native.glEnableVertexAttribArray(vAttributeArrayHandle);
		}

		for (int i = 0; i < primitiveData.getSamplers().size(); i++) {
			final AbstractSampler s = primitiveData.getSamplers().get(i);
			final int samplerHandle = program.getUniformHandle(s.getVariableName());
			s.send(samplerHandle);
		}

		primitiveData.getBlending().enable();
		OpenGLES2Native.glDrawElements(primitiveData.getPrimitiveType().getId(), indices.capacity(), indicesType.getId(), indices);
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void onDrawFrame(GL10 gl) {
		final long enterTime = System.nanoTime();
		final long timeSinceLastFrame = getMilliSecondsSinceLastFrame();

		for (int i = 0; i < glSurface.getFrameUpdateListeners().size(); i++) {
			final IFrameUpdateListener f = glSurface.getFrameUpdateListeners().get(i);
			f.onFrameUpdate(timeSinceLastFrame);
		}

		OpenGLES2Native.glClear(GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT);

		for (int i = 0; i < glSurface.getPrograms().size(); i++) {
			final Program program = glSurface.getPrograms().get(i);
			program.use();

			synchronized (glSurface.getPrimitiveDatas()) {
				for (int j = 0; j < glSurface.getPrimitiveDatas().size(); j++) {
					drawElements(program, glSurface.getPrimitiveDatas().get(j));
				}
			}
		}

		for (int i = 0; i < glSurface.getFrameTimeListeners().size(); i++) {
			final IFrameTimeListener f = glSurface.getFrameTimeListeners().get(i);

			/* Each frame time callback gets a fresh time... */
			f.onFrameTime(System.nanoTime() - enterTime);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		OpenGLES2Native.glViewport(0, 0, width, height);
		Capability.GL_DEPTH_TEST.enable();
		Capability.GL_CULL_FACE.enable();
		Capability.GL_BLEND.enable();

		for (ISurfaceChangeListener s : glSurface.getSurfaceChangeListeners()) {
			s.onSurfaceChange(glSurface);
		}
	}

	@Override
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.i(GLSurfaceRenderer.class.getName(), "OpenGL properties of this device:");
		Log.i(GLSurfaceRenderer.class.getName(), "Version: " + Property.GL_VERSION.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Vendor: " + Property.GL_VENDOR.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Shading language version: " + Property.GL_SHADING_LANGUAGE_VERSION.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Renderer: " + Property.GL_RENDERER.get());
		Log.i(GLSurfaceRenderer.class.getName(), "Extensions: " + Property.GL_EXTENSIONS.get());

		for (Program program : glSurface.getPrograms()) {
			program.compile();

			synchronized (program.getGlobalUniforms()) {
				for (AbstractUniform u : program.getGlobalUniforms()) {
					program.registerUniformHandles(u.getVariableName());
				}
			}

			synchronized (glSurface.getPrimitiveDatas()) {
				for (PrimitiveData p : glSurface.getPrimitiveDatas()) {
					final Set<String> attributes = p.getVertexAttributeArrays().getNames();
					final Set<String> uniforms = p.getUniforms().getNames();

					program.registerVertexAttributeHandles(p.getVertices().getVariableName());
					program.registerVertexAttributeHandles(attributes.toArray(new String[attributes.size()]));
					program.registerUniformHandles(uniforms.toArray(new String[uniforms.size()]));

					for (AbstractSampler s : p.getSamplers()) {
						program.registerUniformHandles(s.getVariableName());
						s.bind();
					}
				}
			}
		}
	}
}
