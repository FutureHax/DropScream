package com.t3hh4xx0r.dropscream.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.t3hh4xx0r.dropscream.DropWatcherService;
import com.t3hh4xx0r.dropscream.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		findViewById(R.id.link).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://xkcd.com/1363/"));
				startActivity(Intent.createChooser(i, "View in..."));
			}
		});
		
		findViewById(R.id.scream).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://en.wikipedia.org/wiki/Wilhelm_scream"));
				startActivity(Intent.createChooser(i, "View in..."));
			}
		});
		DropWatcherService.start(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean enabled = PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean("enabled", false);

		getMenuInflater().inflate(R.menu.menu_main, menu);
		final Switch onSwitch = (Switch) menu.findItem(R.id.enable)
				.getActionView();
		onSwitch.setChecked(enabled);
		onSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				PreferenceManager
						.getDefaultSharedPreferences(buttonView.getContext())
						.edit().putBoolean("enabled", isChecked).apply();
			}
		});
		return true;
	}

}
