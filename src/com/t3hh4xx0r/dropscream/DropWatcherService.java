package com.t3hh4xx0r.dropscream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.t3hh4xx0r.dropscream.AccelerometerManager.AccelerometerListener;

public class DropWatcherService extends Service implements
		AccelerometerListener {
	int dropCount = 0;
	MediaPlayer m;

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
		stopScream(true);
		if (AccelerometerManager.isListening()) {
			AccelerometerManager.stopListening();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (AccelerometerManager.isSupported(this)) {
			AccelerometerManager.startListening(this);
		}
		m = new MediaPlayer();
		prepareScream();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDropStarted() {
		PreferenceManager
				.getDefaultSharedPreferences(this)
				.edit()
				.putInt("dropCount",
						PreferenceManager.getDefaultSharedPreferences(this)
								.getInt("dropCount", 0) + 1).apply();
		dropCount = PreferenceManager.getDefaultSharedPreferences(this).getInt(
				"dropCount", 0);

		playScream();
	}

	@Override
	public void onDropStopped() {
		Toast.makeText(this, "You've dropped your device " + dropCount + " times.", Toast.LENGTH_SHORT).show();
	}

	public void stopScream(boolean andRelease) {
		try {
			if (m.isPlaying()) {
				m.stop();
			}
			if (andRelease) {
				m.release();
			}
		} catch (Exception e) {

		}
	}

	public void prepareScream() {
		try {
			if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}

			AssetFileDescriptor descriptor = getAssets().openFd(
					"wilhelm_scream.mp3");
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			m.prepare();
			m.setVolume(1f, 1f);
			m.setLooping(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playScream() {
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				"enabled", false)) {
			m.start();
		}
	}
}
