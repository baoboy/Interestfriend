package com.interestfriend.utils;

import android.content.Context;
import android.content.Intent;

/**
 * �㲥������Ϣ��
 * 
 * @author teeker_bin
 * 
 */
public class BroadCast {
	/**
	 * ���Ϳ�ֵ�Ĺ㲥
	 * 
	 * @param context
	 * @param action
	 */
	public static void sendBroadCast(Context context, String action) {
		Intent mIntent = new Intent(action);
		context.sendBroadcast(mIntent);
	}

	/**
	 * ���ʹ�ֵ�㲥
	 * 
	 * @param context
	 * @param intent
	 */
	public static void sendBroadCast(Context context, Intent intent) {
		context.sendBroadcast(intent);
	}
}
