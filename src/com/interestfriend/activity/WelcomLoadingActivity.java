package com.interestfriend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.interestfriend.R;

public class WelcomLoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom_loading);
		new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startActivity(new Intent(WelcomLoadingActivity.this,
						HomeActivity.class));
				finish();
			}
		}.start();
	}

}
