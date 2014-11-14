package com.interestfriend.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.db.Const;

public class Praise extends AbstractData {
	private int user_id;
	private String user_avatar = "";
	private int growth_id;

	@Override
	public String toString() {
		return "user_id:" + user_id;
	}

	public int getGrowth_id() {
		return growth_id;
	}

	public void setGrowth_id(int growth_id) {
		this.growth_id = growth_id;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.PRAISE_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("growth_id", this.growth_id);
		cv.put("user_id", this.user_id);
		cv.put("user_avatar", this.user_avatar);
		db.insert(dbName, null, cv);

	}
}
