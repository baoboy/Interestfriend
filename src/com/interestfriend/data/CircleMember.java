package com.interestfriend.data;

import java.io.File;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.enums.CircleMemberState;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.data.result.StringResult;
import com.interestfriend.db.Const;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MemberSelfPaser;
import com.interestfriend.parser.SimpleParser;
import com.interestfriend.parser.StringParser;
import com.interestfriend.utils.BitmapUtils;
import com.interestfriend.utils.SharedUtils;

public class CircleMember extends AbstractData {
	private static final String JOIN_OFFCIAL_CIRCLE_API = "JoinOfficialCircleServlet";
	private static final String UPDATE_USER_INFO = "UpdateUserInfoServlet";
	private static String UPDATE_MEMBER_AVATAR = "UpdateUserAvatarServlet";
	private static String KICK_MEMBER_API = "KickOutMemberServlet";
	private static String RECEIVE_JOIN_CIRCL_EREQUEST = "ReceiveJoinCircleRequestServlet";
	private static String REFUSE_JOIN_CIRCL_EREQUEST = "RefuseJoinCircleRequestServlet";
	private static final String GET_USER_INFO = "GetUserInfoServlet";
	private static final String UPDATE_USER_ADDRESS = "UpdateUserAddressServlet";

	private int user_id;
	private int circle_id;
	private String group_id = "";
	private String user_name = "";// 用户姓名
	private String user_cellphone = "";// 用户电话
	private String user_avatar = "";// 用户注册头像
	private String user_gender = "";// 用户注册性别
	private String user_birthday = "";// 用户注册生日
	private String user_register_time = "";
	private String user_chat_id = "";
	private String sortkey = "";
	private String pinyinFir = "";
	private CircleMemberState state;
	private String user_declaration = "";
	private String user_description = "";
	private String user_address = "";
	private String user_province = "";
	private String user_province_key = "";

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}

	public String getUser_province() {
		return user_province;
	}

	public void setUser_province(String user_province) {
		this.user_province = user_province;
	}

	public String getUser_province_key() {
		return user_province_key;
	}

	public void setUser_province_key(String user_province_key) {
		this.user_province_key = user_province_key;
	}

	public String getUser_declaration() {
		return user_declaration;
	}

	public void setUser_declaration(String user_declaration) {
		this.user_declaration = user_declaration;
	}

	public String getUser_description() {
		return user_description;
	}

	public void setUser_description(String user_description) {
		this.user_description = user_description;
	}

	public CircleMemberState getState() {
		return state;
	}

	public void setState(CircleMemberState state) {
		this.state = state;
	}

	public String getSortkey() {
		return sortkey;
	}

	public void setSortkey(String sortkey) {
		this.sortkey = sortkey;
	}

	public String getPinyinFir() {
		return pinyinFir;
	}

	public void setPinyinFir(String pinyinFir) {
		this.pinyinFir = pinyinFir;
	}

	public String getUser_chat_id() {
		return user_chat_id;
	}

	public void setUser_chat_id(String user_chat_id) {
		this.user_chat_id = user_chat_id;
	}

	public String getUser_register_time() {
		return user_register_time;
	}

	public void setUser_register_time(String user_register_time) {
		this.user_register_time = user_register_time;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_cellphone() {
		return user_cellphone;
	}

	public void setUser_cellphone(String user_cellphone) {
		this.user_cellphone = user_cellphone;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}

	public String getUser_birthday() {
		return user_birthday;
	}

	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(int circle_id) {
		this.circle_id = circle_id;
	}

	@Override
	public String toString() {
		return "user_name:" + user_name + "  user_cellphone：" + user_cellphone
				+ "  user_birthday:" + user_birthday + "  user_gender:"
				+ user_gender + "  user_avatar:" + user_avatar;
	}

	public void getNameAndAvatar(SQLiteDatabase db) {
		String conditionsKey = "user_id=?";
		String[] conditionsValue = { this.user_id + "" };
		Cursor cursor = db.query(Const.CIRCLE_MEMBER_TABLE, new String[] {
				"user_name", "user_avatar" }, conditionsKey, conditionsValue,
				null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			String name = cursor.getString(cursor.getColumnIndex("user_name"));
			String avatar = cursor.getString(cursor
					.getColumnIndex("user_avatar"));
			this.user_avatar = avatar;
			this.user_name = name;
		}
		cursor.close();
	}

	public void getNameAndAvatarByUserChatId(SQLiteDatabase db) {
		String conditionsKey = "user_chat_id=?";
		String[] conditionsValue = { this.user_chat_id + "" };
		Cursor cursor = db.query(Const.CIRCLE_MEMBER_TABLE, new String[] {
				"user_name", "user_avatar" }, conditionsKey, conditionsValue,
				null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			String name = cursor.getString(cursor.getColumnIndex("user_name"));
			String avatar = cursor.getString(cursor
					.getColumnIndex("user_avatar"));
			this.user_avatar = avatar;
			this.user_name = name;
		}
		cursor.close();
	}

	/**
	 * 加入圈子
	 * 
	 * @return
	 */
	public RetError joinOffcialCircle(int circle_creator, String circle_name) {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", user_id);
		params.put("circle_id", circle_id);
		params.put("group_id", group_id);
		params.put("circle_creator", circle_creator);
		params.put("huanxin_username", SharedUtils.getHXId());
		params.put("user_name", SharedUtils.getAPPUserName());
		params.put("circle_name", circle_name);
		Result ret = ApiRequest
				.request(JOIN_OFFCIAL_CIRCLE_API, params, parser);

		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	/**
	 * 接受加入圈子请求
	 * 
	 * 
	 * @return
	 */
	public RetError receiveJoinCircleRequest(
			String request_join_circle_user_huanxin_username,
			String join_circle_name) {
		IParser parser = new StringParser("circle_last_request_time");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("request_join_circle_user_id", user_id);
		params.put("join_circle_id", circle_id);
		params.put("group_id", group_id);
		params.put("request_join_circle_user_huanxin_username",
				request_join_circle_user_huanxin_username);
		params.put("join_circle_name", join_circle_name);

		Result ret = ApiRequest.request(RECEIVE_JOIN_CIRCL_EREQUEST, params,
				parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			StringResult sr = (StringResult) ret;
			SharedUtils.setCircleLastRequestTime(Long.valueOf(sr.getStr()));
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	/**
	 * 拒绝加入圈子请求
	 * 
	 * 
	 * @return
	 */
	public RetError refuseJoinCircleRequest(
			String request_join_circle_user_huanxin_username,
			String join_circle_name) {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("request_join_circle_user_huanxin_username",
				request_join_circle_user_huanxin_username);
		params.put("join_circle_name", join_circle_name);

		Result ret = ApiRequest.request(REFUSE_JOIN_CIRCL_EREQUEST, params,
				parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public RetError getMemberInfo() {
		IParser parser = new MemberSelfPaser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_member_id", user_id);
		Result ret = ApiRequest.request(GET_USER_INFO, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			CircleMember member = (CircleMember) ret.getData();
			this.user_avatar = member.getUser_avatar();
			this.user_name = member.getUser_name();
			this.user_birthday = member.getUser_birthday();
			this.user_declaration = member.getUser_declaration();
			this.user_description = member.getUser_description();
			this.user_register_time = member.getUser_register_time();
			this.user_gender = member.getUser_gender();
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public RetError updateAvatar() {
		IParser parser = new StringParser("user_avatar");
		HashMap<String, Object> params = new HashMap<String, Object>();
		File file = BitmapUtils.getImageFile(user_avatar);
		Result ret = ApiRequest.requestWithFile(UPDATE_MEMBER_AVATAR, params,
				file, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			StringResult sr = (StringResult) ret;
			this.user_avatar = sr.getStr();
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	/**
	 * 成员编辑 主要针对编辑自己
	 * 
	 * @param cloumn
	 * @param value
	 * @return
	 */
	public RetError upDateUserInfo(String cloumn, String value) {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("cloumn", cloumn);
		params.put("value", value);
		params.put("user_pinyin", pinyinFir);
		params.put("user_sort_key", sortkey);
		Result ret = ApiRequest.request(UPDATE_USER_INFO, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public RetError upDateUserAddress() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_address", user_address);
		params.put("user_province", user_province);
		params.put("user_province_key", user_province_key);
		Result ret = ApiRequest.request(UPDATE_USER_ADDRESS, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public RetError kickOutMember() {
		IParser parser = new StringParser("lastReqTime");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("kickout_user_id", user_id);
		params.put("kickout_user_chat_id", user_chat_id);
		params.put("group_id", group_id);
		params.put("circle_id", circle_id);
		params.put("circle_name", MyApplation.getCircle_name());
		Result ret = ApiRequest.request(KICK_MEMBER_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			this.status = Status.DEL;
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.CIRCLE_MEMBER_TABLE;
		if (this.status == Status.DEL) {
			db.delete(dbName, "user_id=? and circle_id=?", new String[] {
					this.user_id + "", this.circle_id + "" });
		}
		String conditionsKey = "circle_id=? and user_id=?";
		String[] conditionsValue = { this.circle_id + "", this.user_id + "" };
		ContentValues cv = new ContentValues();
		cv.put("circle_id", circle_id);
		cv.put("user_id", user_id);
		cv.put("user_name", user_name);
		cv.put("user_cellphone", user_cellphone);
		cv.put("user_avatar", user_avatar);
		cv.put("user_birthday", user_birthday);
		cv.put("user_gender", user_gender);
		cv.put("sortkey", sortkey);
		cv.put("pinyinFir", pinyinFir);
		cv.put("user_chat_id", user_chat_id);
		cv.put("user_register_time", user_register_time);
		cv.put("user_declaration", user_declaration);
		cv.put("user_description", user_description);
		cv.put("user_address", user_address);
		cv.put("user_province", user_province);
		cv.put("user_province_key", user_province_key);

		if (this.state == CircleMemberState.UPDATE) {
			db.update(dbName, cv, conditionsKey, conditionsValue);
		}
	}

	@Override
	public void read(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.CIRCLE_MEMBER_TABLE, new String[] {
				"circle_id", "user_name", "user_avatar", "user_birthday",
				"user_gender", "user_chat_id", "user_register_time",
				"user_declaration", "user_description" }, "user_id=?",
				new String[] { user_id + "" }, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			this.circle_id = cursor.getInt(cursor.getColumnIndex("circle_id"));
			this.user_name = cursor.getString(cursor
					.getColumnIndex("user_name"));
			this.user_avatar = cursor.getString(cursor
					.getColumnIndex("user_avatar"));
			this.user_birthday = cursor.getString(cursor
					.getColumnIndex("user_birthday"));
			this.user_gender = cursor.getString(cursor
					.getColumnIndex("user_gender"));
			this.user_chat_id = cursor.getString(cursor
					.getColumnIndex("user_chat_id"));
			this.user_register_time = cursor.getString(cursor
					.getColumnIndex("user_register_time"));
			this.user_declaration = cursor.getString(cursor
					.getColumnIndex("user_declaration"));
			this.user_description = cursor.getString(cursor
					.getColumnIndex("user_description"));
			this.user_address = cursor.getString(cursor
					.getColumnIndex("user_address"));
			this.user_province = cursor.getString(cursor
					.getColumnIndex("user_province"));
			this.user_province_key = cursor.getString(cursor
					.getColumnIndex("user_province_key"));
		}
	}

	public String toDbUnionInsertString() {
		return circle_id + "," + user_id + ",'" + user_name + "','"
				+ user_cellphone + "','" + user_avatar + "','" + user_birthday
				+ "','" + user_gender + "','" + sortkey + "','" + pinyinFir
				+ "','" + user_chat_id + "','" + user_register_time + "','"
				+ user_declaration + "','" + user_description + "','"
				+ user_address + "','" + user_province + "','"
				+ user_province_key + "'";
	}

	public static String getDbInsertKeyString() {
		return " (circle_id, user_id, user_name, user_cellphone, user_avatar, user_birthday, user_gender, sortkey, pinyinFir, user_chat_id, user_register_time, user_declaration, user_description, user_address, user_province, user_province_key"
				+ ") ";
	}
}
