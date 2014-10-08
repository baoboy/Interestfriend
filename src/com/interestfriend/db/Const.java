package com.interestfriend.db;

public class Const {
	public static final String MY_CIRCLE_TABLE_NAME = "my_circles";
	public static final String MY_CIRCLE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " circle_id integer, group_id varchar, circle_name varchar, circle_description varchar, circle_logo varchar";
	public static final String GROWTHS_TABLE_NAME = "growths";
	public static final String GROWTHS_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " growth_id integer, cid integer, publisher_id integer, content varchar, time varchar";
	public static final String GROWTH_IMAGE_TABLE_NAME = "growth_images";
	public static final String GROWTH_IMAGE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " cid integer, growth_id integer, img_id integer, img varchar";
}
