package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 * 
 */
public class MyUserFriendProvider {

	// 这个是每个Provider的标识，在Manifest中使用
	public static final String AUTHORITY = "com.quyou.user.friends";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quyou";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quyou";

	public static final class MyUserFriendlumns implements BaseColumns {
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/friends");

	}

}
