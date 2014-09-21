package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 * 
 */
public class MyCirclesProvider {

	// 这个是每个Provider的标识，在Manifest中使用
	public static final String AUTHORITY = "com.changlianxi.circle.members";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.changlianxi";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.changlianxi";

	/**
	 * 跟circle_members表相关的常量
	 * 
	 */
	public static final class MyCirclesColumns implements BaseColumns {
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/members");
		public static final String TABLE_NAME = "my_circles";
		public static final String CIRCLE_ID = "circle_id";
		public static final String CIRCLE_NAME = "circle_name";
		public static final String CIRCLE_DESCRIPTION = "circle_description";
		public static final String CIRCLE_LOGO = "circle_logo";

	}

}
