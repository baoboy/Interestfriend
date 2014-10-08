package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 * 
 */
public class MyCircleGorwthsProvider {

	// 这个是每个Provider的标识，在Manifest中使用
	public static final String AUTHORITY = "com.quyou.circle.growths";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quyou";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quyou";

	/**
	 * 跟growths表相关的常量
	 * 
	 */
	public static final class MyCirclesGorwthsColumns implements BaseColumns {
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/growths");
		public static final String TABLE_NAME = "growths";
		public static final String GROWTH_ID = "growth_id";
		public static final String CID = "cid";
		public static final String PUBLISHER_ID = "publisher_id";
		public static final String CONTENT = "content";
		public static final String TIME = "time";

	}

}
