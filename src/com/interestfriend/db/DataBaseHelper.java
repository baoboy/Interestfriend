package com.interestfriend.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.interestfriend.applation.MyApplation;
import com.interestfriend.utils.SharedUtils;

public class DataBaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "tf";
	private static DataBaseHelper instance;
	private static final int DATABASE_VERSION = 1;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static DataBaseHelper getInstance() {
		return getInstance(MyApplation.getInstance());
	}

	public static DataBaseHelper getInstance(Context context) {
		String uid = SharedUtils.getUid();
		return getInstance(context, uid);
	}

	public static DataBaseHelper getInstance(Context context, String postfix) {
		if (instance == null) {
			instance = new DataBaseHelper(context, DATABASE_NAME + postfix,
					null, DATABASE_VERSION);

		}
		return instance;
	}

	public static void setIinstanceNull() {
		instance = null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDB(db);
	}

	private void createDB(SQLiteDatabase db) {
		// my circles
		db.execSQL("create table IF NOT EXISTS " + Const.MY_CIRCLE_TABLE_NAME
				+ "( " + Const.MY_CIRCLE_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS " + Const.GROWTHS_TABLE_NAME
				+ "( " + Const.GROWTHS_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS "
				+ Const.GROWTH_IMAGE_TABLE_NAME + "( "
				+ Const.GROWTH_IMAGE_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS " + Const.CIRCLE_MEMBER_TABLE
				+ "( " + Const.CIRCLE_MEMBER_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS " + Const.VIDEO_TABLE_NAME
				+ "( " + Const.VIDEO_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS " + Const.COMMENT_TABLE_NAME
				+ "( " + Const.COMMENT_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS "
				+ Const.VIDEO_COMMENT_TABLE_NAME + "( "
				+ Const.VIDEO_COMMENT_TABLE_STRUCTURE + " )");

		db.execSQL("create table IF NOT EXISTS " + Const.PRAISE_TABLE_NAME
				+ "( " + Const.PRAISE_TABLE_STRUCTURE + " )");
		db.execSQL(Const.USERNAME_TABLE_CREATE);
		db.execSQL(Const.INIVTE_MESSAGE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
