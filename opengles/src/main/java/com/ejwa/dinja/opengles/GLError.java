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
package com.ejwa.dinja.opengles;

import android.util.Log;

public final class GLError {
	public enum Code {
		GL_NO_ERROR(0x0, "No error has been recorded."),
		GL_INVALID_ENUM(0x500, "An unacceptable value is specified for an enumerated argument."),
		GL_INVALID_VALUE(0x501, "A numeric argument is out of range"),
		GL_INVALID_OPERATION(0x502, "The specified operation is not allowed in the current state."),
		GL_OUT_OF_MEMORY(0x505, "There is not enough memory left to execute the command.");

		private final int id;
		private final String description;

		Code(int id, String description) {
			this.id = id;
			this.description = description;
		}

		public int getId() {
			return id;
		}

		public String getDescription() {
			return description;
		}
	}

	private GLError() {
		/* No instances of this class allowed. */
	}

	public static Code getNext() {
		final int error = OpenGLES2.glGetError();

		/*
		 * Not the most effieicent way of handling this. However, as
		 * it's only for error handling it's an acceptable solution.
		 */
		if (Code.GL_NO_ERROR.getId() != error) {
			for (Code c : Code.class.getEnumConstants()) {
				if (c.getId() == error) {
					return c;
				}
			}
		}

		return Code.GL_NO_ERROR;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static void check(Class callingClass) {
		Code code = getNext();

		if (code != Code.GL_NO_ERROR) {
			do {
				Log.e(callingClass.getName(), String.format("GL error: %s", code.getDescription()));
			} while ((code = getNext()) != Code.GL_NO_ERROR);

			throw new GLException("An GL error occured.");
		}
	}
}
