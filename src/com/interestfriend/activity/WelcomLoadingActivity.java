package com.interestfriend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.interestfriend.R;
import com.interestfriend.utils.SharedUtils;

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
				if (SharedUtils.getIntUid() > 0) {
					startActivity(new Intent(WelcomLoadingActivity.this,
							HomeActivity.class));
				} else {
					startActivity(new Intent(WelcomLoadingActivity.this,
							WelcomActivity.class));
				}

				finish();
			}
		}.start();

	}

}
