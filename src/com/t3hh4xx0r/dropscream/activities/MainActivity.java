package com.t3hh4xx0r.dropscream.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.t3hh4xx0r.dropscream.AccelerometerManager;
import com.t3hh4xx0r.dropscream.AccelerometerManager.AccelerometerListener;
import com.t3hh4xx0r.dropscream.R;

public class MainActivity extends Activity implements AccelerometerListener {
	static int dropCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		findViewById(android.R.id.content).setBackgroundColor(Color.GREEN);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (AccelerometerManager.isSupported(this)) {
			AccelerometerManager.startListening(this);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (AccelerometerManager.isListening()) {
			AccelerometerManager.stopListening();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (AccelerometerManager.isListening()) {
			AccelerometerManager.stopListening();
		}
	}

	@Override
	public void onDropStarted() {
		dropCount = dropCount + 1;
		((TextView) findViewById(R.id.drop_count)).setText(String
				.valueOf(dropCount));
		Toast.makeText(this, "DROP STARTED", Toast.LENGTH_SHORT).show();
		findViewById(android.R.id.content).setBackgroundColor(Color.RED);
	}

	@Override
	public void onDropStopped() {
		Toast.makeText(this, "DROP STOPPED", Toast.LENGTH_SHORT).show();
		findViewById(android.R.id.content).setBackgroundColor(Color.GREEN);
	}

}
