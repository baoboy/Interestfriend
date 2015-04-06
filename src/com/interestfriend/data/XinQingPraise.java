package com.interestfriend.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.AbstractData.Status;
import com.interestfriend.db.Const;

public class XinQingPraise extends AbstractData {
	private int user_id;
	private String user_avatar = "";
	private int xinqing_id;

	@Override
	public String toString() {
		return "user_id:" + user_id + "  xinqing_id" + xinqing_id;
	}

	public int getXinqing_id() {
		return xinqing_id;
	}

	public void setXinqing_id(int xinqing_id) {
		this.xinqing_id = xinqing_id;
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

}
