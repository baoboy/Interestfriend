package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * ��Ÿ����ݿ��йصĳ���
 * 
 */
public class MyCircleGorwthsProvider {

	// �����ÿ��Provider�ı�ʶ����Manifest��ʹ��
	public static final String AUTHORITY = "com.quyou.circle.growths";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quyou";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quyou";

	/**
	 * ��growths����صĳ���
	 * 
	 */
	public static final class MyCirclesGorwthsColumns implements BaseColumns {
		// CONTENT_URI�����ݿ�ı������������CONTENT_URI����ѯ��Ӧ�ı�
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
