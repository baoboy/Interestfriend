package com.interestfriend.data;

import com.interestfriend.db.Const;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class MyCircles extends Circles {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void read(SQLiteDatabase db) {
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.MY_CIRCLE_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("circle_id", getCircle_id());
		cv.put("circle_name", getCircle_name());
		cv.put("circle_description", getCircle_description());
		cv.put("circle_logo", getCircle_logo());
		cv.put("group_id", getGroup_id());
		db.insert(dbName, null, cv);
	}
}
