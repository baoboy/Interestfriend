package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 * 
 */
public class MyCircleVideoProvider {

	// 这个是每个Provider的标识，在Manifest中使用
	public static final String AUTHORITY = "com.quyou.circle.videos";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quyou";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quyou";

	/**
	 * 跟circle_members表相关的常量
	 * 
	 */
	public static final class MyCircleVideoColumns implements BaseColumns {
		// CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/videos");
		public static final String TABLE_NAME = "videos";
		public static final String VIDEO_ID = "video_id";
		public static final String CID = "cid";
		public static final String PUBLISHER_ID = "publisher_id";
		public static final String VIDEO_SIZE = "video_size";
		public static final String VIDEO_DURATION = "video_duration";
		public static final String VIDEO_IMG = "video_img";
		public static final String VIDEO_PATH = "video_path";
		public static final String TIME = "time";

	}

}
