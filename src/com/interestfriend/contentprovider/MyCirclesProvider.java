package com.interestfriend.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * ��Ÿ����ݿ��йصĳ���
 * 
 */
public class MyCirclesProvider {

	// �����ÿ��Provider�ı�ʶ����Manifest��ʹ��
	public static final String AUTHORITY = "com.quyou.circle.circles";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quyou";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.quyou";

	/**
	 * ��circle_members����صĳ���
	 * 
	 */
	public static final class MyCirclesColumns implements BaseColumns {
		// CONTENT_URI�����ݿ�ı������������CONTENT_URI����ѯ��Ӧ�ı�
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/circles");
		public static final String TABLE_NAME = "my_circles";
		public static final String CIRCLE_ID = "circle_id";
		public static final String CIRCLE_NAME = "circle_name";
		public static final String CIRCLE_DESCRIPTION = "circle_description";
		public static final String CIRCLE_LOGO = "circle_logo";
		public static final String GROUP_ID = "group_id";
		public static final String CREATOR_ID = "creator_id";
		public static final String CIRCLE_CREATOR_NAME = "circle_creator_name";
		public static final String CIRCLE_CREATE_TIME = "circle_create_time";
		public static final String CIRCLE_CATEGORY = "circle_category";
		public static final String CIRCLE_MEMBER_NUM = "circle_member_num";

	}

}
