package com.interestfriend.data;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.SimpleParser;

public class Comment extends AbstractData {
	private final String COMMENT_API = "CommentServlet";

	private int comment_id;
	private int growth_id;
	private int publisher_id;
	private String comment_content = "";
	private String comment_time = "";

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getGrowth_id() {
		return growth_id;
	}

	public void setGrowth_id(int growth_id) {
		this.growth_id = growth_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "comment_content:" + this.comment_content;
	}

	public RetError sendComment() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("comment_content", comment_content);
		params.put("growth_id", growth_id);
		Result ret = ApiRequest.request(COMMENT_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}

	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.COMMENT_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("growth_id", this.growth_id);
		cv.put("comment_id", this.comment_id);
		cv.put("publisher_id", this.publisher_id);
		cv.put("comment_time", this.comment_time);
		cv.put("comment_content", this.comment_content);
		db.insert(dbName, null, cv);

	}
}
