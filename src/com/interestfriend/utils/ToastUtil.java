package com.interestfriend.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.interestfriend.applation.MyApplation;

public class ToastUtil {
	/**
	 * 显示提示信息
	 * 
	 * @param str
	 */
	public static void showToast(String str, int duration) {
		Toast toast = Toast.makeText(MyApplation.getInstance(), str, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}
}
