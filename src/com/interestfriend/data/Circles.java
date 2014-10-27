package com.interestfriend.data;

import java.io.File;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.MapResult;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MapParser;

public class Circles extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CREATE_CIRCLE_API = "CreateCircleServlet";

	private int circle_id = 0;
	private String circle_name = "";
	private String circle_description = "";
	private String circle_logo = "";
	private String group_id = "";
	private int circle_category;
	private int distance;
	private int unread;

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getCircle_category() {
		return circle_category;
	}

	public void setCircle_category(int circle_category) {
		this.circle_category = circle_category;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public int getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(int circle_id) {
		this.circle_id = circle_id;
	}

	public String getCircle_name() {
		return circle_name;
	}

	public void setCircle_name(String circle_name) {
		this.circle_name = circle_name;
	}

	public String getCircle_description() {
		return circle_description;
	}

	public void setCircle_description(String circle_description) {
		this.circle_description = circle_description;
	}

	public String getCircle_logo() {
		return circle_logo;
	}

	public void setCircle_logo(String circle_logo) {
		this.circle_logo = circle_logo;
	}

	@Override
	public String toString() {
		return "circle_id:" + circle_id + "  circle_name:" + circle_name
				+ "   circle_description:" + circle_description
				+ "  circle_avatar:" + circle_logo;
	}

	/**
	 * 创建新的圈子
	 * 
	 * @return
	 */
	public RetError createNewCircle() {
		String[] keys = { "circle_logo", "group_id", "circle_id" };
		IParser parser = new MapParser(keys);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_name", circle_name);
		params.put("circle_description", circle_description);
		params.put("category", circle_category);
		params.put("longitude", MyApplation.getnLontitude());
		params.put("latitude", MyApplation.getnLatitude());
		Result ret = ApiRequest.requestWithFile(CREATE_CIRCLE_API, params,
				new File(circle_logo), parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			MapResult mret = (MapResult) ret;
			circle_logo = (String) (mret.getMaps().get("circle_logo"));
			group_id = (String) (mret.getMaps().get("group_id"));
			circle_id = Integer.valueOf((String) mret.getMaps()
					.get("circle_id"));
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		String tableName = Const.MY_CIRCLE_TABLE_NAME;
		ContentValues values = new ContentValues();
		values.put("circle_logo", circle_logo);
		values.put("circle_name", circle_name);
		values.put("circle_description", circle_description);
		values.put("group_id", group_id);
		values.put("circle_id", circle_id);
		db.insert(tableName, null, values);
	}

	public int findCircleByID(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.MY_CIRCLE_TABLE_NAME,
				new String[] { "circle_name", }, "circle_id=?",
				new String[] { circle_id + "" }, null, null, null);
		return cursor.getCount();
	}

	@Override
	public void read(SQLiteDatabase db) {

	}
}
