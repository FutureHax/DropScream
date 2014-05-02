package com.t3hh4xx0r.dropscream;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerManager {

	public interface AccelerometerListener {

		public void onDropStarted();

		public void onDropStopped();

	}

	private static Context aContext = null;

	private static Sensor sensor;
	private static SensorManager sensorManager;
	// you could use an OrientationListener array instead
	// if you plans to use more than one listener
	private static AccelerometerListener listener;

	/** indicates whether or not Accelerometer Sensor is supported */
	private static Boolean supported;
	/** indicates whether or not Accelerometer Sensor is running */
	private static boolean running = false;

	/**
	 * Returns true if the manager is listening to orientation changes
	 */
	public static boolean isListening() {
		return running;
	}

	/**
	 * Unregisters listeners
	 */
	public static void stopListening() {
		running = false;
		try {
			if (sensorManager != null && sensorEventListener != null) {
				sensorManager.unregisterListener(sensorEventListener);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Returns true if at least one Accelerometer sensor is available
	 */
	public static boolean isSupported(Context context) {
		aContext = context;
		if (supported == null) {
			if (aContext != null) {

				sensorManager = (SensorManager) aContext
						.getSystemService(Context.SENSOR_SERVICE);

				// Get all sensors in device
				List<Sensor> sensors = sensorManager
						.getSensorList(Sensor.TYPE_ACCELEROMETER);

				supported = new Boolean(sensors.size() > 0);

			} else {
				supported = Boolean.FALSE;
			}
		}
		return supported;
	}

	/**
	 * Registers a listener and start listening
	 * 
	 * @param accelerometerListener
	 *            callback for accelerometer events
	 */
	public static void startListening(
			AccelerometerListener accelerometerListener) {

		sensorManager = (SensorManager) aContext
				.getSystemService(Context.SENSOR_SERVICE);

		// Take all sensors in device
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (sensors.size() > 0) {

			sensor = sensors.get(0);

			// Register Accelerometer Listener
			running = sensorManager.registerListener(sensorEventListener,
					sensor, SensorManager.SENSOR_DELAY_GAME);

			listener = accelerometerListener;
		}

	}

	/**
	 * The listener that listen to events from the accelerometer listener
	 */
	private static SensorEventListener sensorEventListener = new SensorEventListener() {

		private long now = 0;
		private long timeDiff = 0;
		private long lastUpdate = 0;
		private long firstSteady = 0;
		private boolean isSteady = false;
		
		private float x = 0;
		private float y = 0;
		private float z = 0;
		boolean startedDrop = false;
		float[] gravity = new float[3];
		float[] linear_acceleration = new float[3];

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		public void onSensorChanged(SensorEvent event) {
			// use the event timestamp as reference
			// so the manager precision won't depends
			// on the AccelerometerListener implementation
			// processing time
			now = event.timestamp;

			x = event.values[0];
			y = event.values[1];
			z = event.values[2];

			final float alpha = 0.8f;

			gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
			gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
			gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

			linear_acceleration[0] = x - gravity[0];
			linear_acceleration[1] = y - gravity[1];
			linear_acceleration[2] = z - gravity[2];

			// Log.d("THE X Y Z", "X = " + x + " : Y = " + y + " : Z = " + z);
			Log.d("THE ACCELS", "X = " + linear_acceleration[0] + " : Y = "
					+ linear_acceleration[1] + " : Z = "
					+ linear_acceleration[2]);
			// if not interesting in shake events
			// just remove the whole if then else block
			if (lastUpdate == 0) {
				lastUpdate = now;
				Toast.makeText(aContext, "No Motion detected",
						Toast.LENGTH_SHORT).show();
			} else {
				timeDiff = now - lastUpdate;

				if (timeDiff > 0) {
					if (startedDrop && linear_acceleration[1] > 0) {
						if (isSteady && now - firstSteady > 1000) {
							startedDrop = false;
							listener.onDropStopped();
							firstSteady = 0;
							isSteady = false;
						} else {
							if (linear_acceleration[1] < 1) {
								isSteady = true;
								if (firstSteady == 0) {
									firstSteady = now;
								}
							}
						}
					} else {
						isSteady = false;
						if (linear_acceleration[1] < -4) {
							if (!startedDrop) {
								startedDrop = true;
								listener.onDropStarted();
							}
						}
					}
					
					lastUpdate = now;
				} else {
					Toast.makeText(aContext, "No Motion detected",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

}