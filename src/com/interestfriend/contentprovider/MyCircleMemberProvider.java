package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 * 
 */
public class MyCircleMemberProvider {

	// 这个是每个Provider的标识，在Manifest中使用
	public static final String AUTHORITY = "com.quyou.circle.members";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quyou";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quyou";

	/**
	 * 跟circle_members表相关的常量
	 * 
	 */
	public static final class MyCircleMemberColumns implements BaseColumns {
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/members");
		public static final String TABLE_NAME = "circle_members";
		public static final String CIRCLE_ID = "circle_id";
		public static final String USER_ID = "user_id";
		public static final String USER_NAME = "user_name";
		public static final String USER_CELLPHONE = "user_cellphone";
		public static final String USER_AVATAR = "user_avatar";
		public static final String USER_BIRTHDAY = "user_birthday";
		public static final String USER_GENDER = "user_gender";
		public static final String SORT_KEY = "sortkey";
		public static final String PINYIN_FIR = "pinyinFir";
		public static final String USER_CHAT_ID = "user_chat_id";
		public static final String USER_REGISTER_TIME = "user_register_time";

	}

}
