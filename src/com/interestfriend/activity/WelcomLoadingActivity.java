package com.interestfriend.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.huanxin.helper.QuYouHXSDKHelper;
import com.interestfriend.R;
import com.interestfriend.utils.SharedUtils;

public class WelcomLoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcom_loading);
		new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (QuYouHXSDKHelper.getInstance().isLogined()) {
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
