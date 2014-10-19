package com.interestfriend.utils;

import com.interestfriend.applation.MyApplation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * * SharedPreferences 的公具类
 * 
 * @author teeker_bin
 * 
 */
public class SharedUtils {
	private static final String SP_NAME = "tf";
	private static SharedPreferences sharedPreferences = MyApplation
			.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	private static Editor editor = sharedPreferences.edit();
	public static final String SP_UID = "uid";

	public static String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();

	}

	public static long getLong(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);

	}

	public static void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public static void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void setUid(String uid) {
		setString(SP_UID, uid);
	}

	public static String getUid() {
		return getString(SP_UID, "0");
	}

	public static int getIntUid() {
		String uid = getUid();
		if (uid.length() > 0) {
			return Integer.parseInt(uid);
		}
		return 0;
	}

	public static void setUserName(String value) {
		editor.putString("username", value);
		editor.commit();
	}

	public static String getUserName() {
		return sharedPreferences.getString("username", "");

	}

	public static String getAPPUserName() {
		return sharedPreferences.getString("app_user_name", "");

	}

	public static void setAPPUserName(String value) {
		editor.putString("app_user_name", value);
		editor.commit();
	}

	public static String getAPPUserAvatar() {
		return sharedPreferences.getString("app_user_avatar", "");

	}

	public static void setAPPUserAvatar(String value) {
		editor.putString("app_user_avatar", value);
		editor.commit();
	}

	public static void setCircleMemberLastReqTime(int cid, long lastReqTime) {
		editor.putLong("circle_member_last_req_time" + cid, lastReqTime);
		editor.commit();
	}

	public static long getCircleMemberLastReqTime(int cid) {
		return sharedPreferences.getLong("circle_member_last_req_time" + cid,
				0l);

	}
}
