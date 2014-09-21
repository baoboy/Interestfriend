package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * ��Ÿ����ݿ��йصĳ���
 * 
 */
public class MyCirclesProvider {

	// �����ÿ��Provider�ı�ʶ����Manifest��ʹ��
	public static final String AUTHORITY = "com.changlianxi.circle.members";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.changlianxi";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.changlianxi";

	/**
	 * ��circle_members����صĳ���
	 * 
	 */
	public static final class MyCirclesColumns implements BaseColumns {
		// CONTENT_URI�����ݿ�ı������������CONTENT_URI����ѯ��Ӧ�ı�
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/members");
		public static final String TABLE_NAME = "my_circles";
		public static final String CIRCLE_ID = "circle_id";
		public static final String CIRCLE_NAME = "circle_name";
		public static final String CIRCLE_DESCRIPTION = "circle_description";
		public static final String CIRCLE_LOGO = "circle_logo";

	}

}
