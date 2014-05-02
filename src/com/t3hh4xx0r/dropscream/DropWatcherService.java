package com.t3hh4xx0r.dropscream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.t3hh4xx0r.dropscream.AccelerometerManager.AccelerometerListener;

public class DropWatcherService extends Service implements
		AccelerometerListener {
	int dropCount = 0;

	public DropWatcherService() {
	}

	public static void start(Context c) {
		c.startService(new Intent(c, DropWatcherService.class));
	}

	public static void stop(Context c) {
		c.stopService(new Intent(c, DropWatcherService.class));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (AccelerometerManager.isListening()) {
			AccelerometerManager.stopListening();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (AccelerometerManager.isSupported(this)) {
			AccelerometerManager.startListening(this);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDropStarted() {
		dropCount = dropCount + 1;

		Toast.makeText(this, "DROP STARTED", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDropStopped() {
		Toast.makeText(this, "DROPPED " + dropCount, Toast.LENGTH_SHORT).show();
	}
}
