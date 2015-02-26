package com.interestfriend.db;

public class Const {
	public static final String MY_CIRCLE_TABLE_NAME = "my_circles";
	public static final String MY_CIRCLE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " circle_id integer,creator_id integer, group_id varchar, circle_name varchar, circle_description varchar, circle_logo varchar,circle_creator_name varchar,circle_create_time varchar, circle_category varchar,circle_member_num integer";

	public static final String GROWTHS_TABLE_NAME = "growths";
	public static final String GROWTHS_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " growth_id integer, cid integer, publisher_id integer, content varchar, time varchar, publisher_name varchar, publisher_avatar varcher, isPraise integer, praise_count integer , last_update_time";

	public static final String GROWTH_IMAGE_TABLE_NAME = "growth_images";
	public static final String GROWTH_IMAGE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " cid integer, growth_id integer, img_id integer, img varchar";

	public static final String CIRCLE_MEMBER_TABLE = "circle_members";
	public static final String CIRCLE_MEMBER_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " circle_id integer, user_id integer,  user_name varchar, user_cellphone varchar, "
			+ " user_avatar varchar, "
			+ " sortkey varchar, pinyinFir varchar, user_birthday varchar, user_gender varchar, user_chat_id varchar, user_register_time varchar, user_declaration varchar, user_description varchar, user_address varchar, user_province varchar, user_province_key varchar";

	public static final String VIDEO_TABLE_NAME = "videos";
	public static final String VIDEO_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " video_id integer, cid integer, publisher_id integer, video_size integer, video_duration integer, video_img varchar, video_path varchar, time varchar , publisher_name varchar, publisher_avatar varcher, video_content varchar";

	public static final String COMMENT_TABLE_NAME = "comments";
	public static final String COMMENT_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " comment_id integer, publisher_id integer, growth_id integer, comment_content varchar, comment_time varchar , publisher_name varchar, publisher_avatar varcher,reply_someone_name varchar, reply_someone_id integer";

	public static final String VIDEO_COMMENT_TABLE_NAME = "video_comments";
	public static final String VIDEO_COMMENT_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " comment_id integer, publisher_id integer, video_id integer, comment_content varchar, comment_time varchar , publisher_name varchar, publisher_avatar varcher,reply_someone_name varchar, reply_someone_id integer";
	public static final String PRAISE_TABLE_NAME = "praise";
	public static final String PRAISE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " user_id integer, user_avatar integer ,growth_id integer  ";
}
