package com.interestfriend.applation;

import android.app.Application;

import com.interestfriend.utils.CheckImageLoaderConfiguration;

public class MyApplation extends Application {
	private static MyApplation instance;

	@Override
	public void onCreate() {
		super.onCreate();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);

		instance = this;
	}

	public static MyApplation getInstance() {
		return instance;
	}
}
