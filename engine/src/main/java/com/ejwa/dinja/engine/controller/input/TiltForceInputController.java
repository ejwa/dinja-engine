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
package com.ejwa.dinja.engine.controller.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class TiltForceInputController implements SensorEventListener {
	private final ITiltForceInputListener tiltForceinputListener;
	private final SensorManager sensorManager;

	public TiltForceInputController(Context context, ITiltForceInputListener tiltForceinputListener) {
		this.tiltForceinputListener = tiltForceinputListener;
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		final List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (!sensors.isEmpty()) {
			final Sensor tiltSensor = sensors.get(0);
			sensorManager.registerListener(this, tiltSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	public void unregister() {
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent sensor) {
		tiltForceinputListener.onTiltForceInput(sensor.values[0], sensor.values[1], sensor.values[2]);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		/* Accuracy doesn't interest us */
	}
}
