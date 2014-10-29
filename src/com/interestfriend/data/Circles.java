package com.interestfriend.data;

import java.io.File;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.enums.CircleState;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.MapResult;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MapParser;
import com.interestfriend.parser.StringParser;
import com.interestfriend.utils.SharedUtils;

public class Circles extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CREATE_CIRCLE_API = "CreateCircleServlet";
	private static String QUIT_CIRCLE_API = "QuitCircleServlet";
	private static String DISSOLVE_CIRCLE_API = "DissolveCircleServlet";

	private int circle_id = 0;
	private String circle_name = "";
	private String circle_description = "";
	private String circle_logo = "";
	private String group_id = "";
	private int circle_category;
	private int distance;
	private int group_chat_unread;
	private int growth_unread;
	private int creator_id;
	private CircleState circle_state;
	private String circle_creator_name = "";
	private String circle_create_time = "";
	private String circle_category_name = "";

	public String getCircle_category_name() {
		return circle_category_name;
	}

	public void setCircle_category_name(String circle_category_name) {
		this.circle_category_name = circle_category_name;
	}

	public String getCircle_creator_name() {
		return circle_creator_name;
	}

	public void setCircle_creator_name(String circle_creator_name) {
		this.circle_creator_name = circle_creator_name;
	}

	public String getCircle_create_time() {
		return circle_create_time;
	}

	public void setCircle_create_time(String circle_create_time) {
		this.circle_create_time = circle_create_time;
	}

	public CircleState getCircle_state() {
		return circle_state;
	}

	public void setCircle_state(CircleState circle_state) {
		this.circle_state = circle_state;
	}

	public int getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(int creator_id) {
		this.creator_id = creator_id;
	}

	public int getGrowth_unread() {
		return growth_unread;
	}

	public void setGrowth_unread(int growth_unread) {
		this.growth_unread = growth_unread;
	}

	public int getUnread() {
		return group_chat_unread;
	}

	public void setUnread(int unread) {
		this.group_chat_unread = unread;
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
		params.put("huanxin_username", SharedUtils.getUserName());
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

	public RetError quitCircle() {
		IParser parser = new StringParser("lastReqTime");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_id", circle_id);
		Result ret = ApiRequest.request(QUIT_CIRCLE_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			this.status = Status.DEL;
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	/**
	 * 解散圈子
	 * 
	 * @return
	 */
	public RetError dissolveCircle() {
		IParser parser = new StringParser("lastReqTime");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_id", circle_id);
		Result ret = ApiRequest.request(DISSOLVE_CIRCLE_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			this.status = Status.DEL;
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		String tableName = Const.MY_CIRCLE_TABLE_NAME;
		if (this.status == Status.DEL) {
			db.delete(tableName, "circle_id=?", new String[] { circle_id + "" });
			return;
		}
		ContentValues values = new ContentValues();
		values.put("circle_logo", circle_logo);
		values.put("circle_name", circle_name);
		values.put("circle_description", circle_description);
		values.put("group_id", group_id);
		values.put("circle_id", circle_id);
		values.put("creator_id", creator_id);
		values.put("circle_creator_name", circle_creator_name);
		values.put("circle_create_time", circle_create_time);
		values.put("circle_category", circle_category_name);
		db.insert(tableName, null, values);
	}

	public int findCircleByID(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.MY_CIRCLE_TABLE_NAME,
				new String[] { "circle_name", }, "circle_id=?",
				new String[] { circle_id + "" }, null, null, null);
		return cursor.getCount();
	}

	public void findCircleCreatorByID(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.MY_CIRCLE_TABLE_NAME,
				new String[] { "creator_id", }, "circle_id=?",
				new String[] { circle_id + "" }, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			this.creator_id = cursor
					.getInt(cursor.getColumnIndex("creator_id"));
		}
	}

	@Override
	public void read(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.MY_CIRCLE_TABLE_NAME, new String[] {
				"circle_logo", "circle_name", "circle_description", "group_id",
				"creator_id", "circle_creator_name", "circle_create_time",
				"circle_category", }, "circle_id=?", new String[] { circle_id
				+ "" }, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			this.creator_id = cursor
					.getInt(cursor.getColumnIndex("creator_id"));
			this.circle_logo = cursor.getString(cursor
					.getColumnIndex("circle_logo"));
			this.circle_name = cursor.getString(cursor
					.getColumnIndex("circle_name"));
			this.circle_description = cursor.getString(cursor
					.getColumnIndex("circle_description"));
			this.group_id = cursor.getString(cursor.getColumnIndex("group_id"));
			this.circle_creator_name = cursor.getString(cursor
					.getColumnIndex("circle_creator_name"));
			this.circle_category_name = cursor.getString(cursor
					.getColumnIndex("circle_category"));
			this.circle_create_time = cursor.getString(cursor
					.getColumnIndex("circle_create_time"));
		}
	}
}
