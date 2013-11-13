package com.lmn.gatest.ativity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lmn.gatest.GATestApp;
import com.lmn.gatest.R;
import com.lmn.gatest.tracker.CustomDimension;
import com.lmn.gatest.util.Util;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();

	private Button btnClickMe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnClickMe = (Button) findViewById(R.id.btnClickMe);
		btnClickMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// Track click event
				GATestApp.TRACKER_MANAGER.trackEvent(TAG, "ui_event", "click",
						"Click Me button", null);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Track activity start
		GATestApp.TRACKER_MANAGER.trackScreenStart(TAG);

		// Track orientation (custom dimension)
		GATestApp.TRACKER_MANAGER.trackCustomDimension(TAG,
				CustomDimension.ORIENTATION_INDEX,
				Util.getOrientationName(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}