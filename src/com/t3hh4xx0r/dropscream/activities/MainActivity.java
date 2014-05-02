package com.t3hh4xx0r.dropscream.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.t3hh4xx0r.dropscream.AccelerometerManager;
import com.t3hh4xx0r.dropscream.AccelerometerManager.AccelerometerListener;
import com.t3hh4xx0r.dropscream.DropWatcherService;
import com.t3hh4xx0r.dropscream.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		DropWatcherService.start(this);
	}

}
