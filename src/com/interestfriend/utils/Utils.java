package com.interestfriend.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.interestfriend.R;

public class Utils {
	/**
	 * “˛≤ÿ»Ìº¸≈Ã
	 */
	public static void hideSoftInput(Context context) {
		if (context == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) {
			return;
		}
		imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * œ‘ æ»Ìº¸≈Ã
	 */
	public static void showSoftInput(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
	} // …Ë÷√«–ªª∂Øª≠£¨¥””“±ﬂΩ¯»Î£¨◊Û±ﬂÕÀ≥ˆ

	public static void leftOutRightIn(Context context) {
		((Activity) context).overridePendingTransition(R.anim.in_from_right,
				R.anim.out_to_left);
	}

	/**
	 * ”“≤‡ÕÀ≥ˆ
	 * 
	 * @param context
	 */
	public static void rightOut(Context context) {
		((Activity) context).overridePendingTransition(R.anim.right_in,
				R.anim.right_out);

	}
}
