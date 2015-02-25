package com.interestfriend.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.db.Const;

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
		cv.put("creator_id", getCreator_id());
		cv.put("circle_creator_name", getCircle_creator_name());
		cv.put("circle_create_time", getCircle_create_time());
		cv.put("circle_category", getCircle_category_name());
		cv.put("circle_member_num", getCircle_member_num());
		if (status == Status.UPDATE) {
			String conditionsKey = "circle_id=? ";
			String[] conditionsValue = { getCircle_id() + "", };
			db.update(dbName, cv, conditionsKey, conditionsValue);
			return;

		}
		db.insert(dbName, null, cv);
	}

	public String toDbUnionInsertString() {
		return getCircle_id() + "," + getCreator_id() + ",'" + getGroup_id()
				+ "','" + getCircle_name() + "','" + getCircle_description()
				+ "','" + getCircle_logo() + "','" + getCircle_creator_name()
				+ "','" + getCircle_create_time() + "','"
				+ getCircle_category_name() + "'," + getCircle_member_num();
	}

	public static String getDbInsertKeyString() {
		return " (circle_id, creator_id, group_id, circle_name, circle_description, circle_logo,circle_creator_name,circle_create_time,circle_category,circle_member_num)";
	}
}
