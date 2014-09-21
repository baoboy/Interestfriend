package com.interestfriend.data;

import java.io.File;
import java.util.HashMap;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.SimpleParser;

public class Register {
	private static final String VERIFY_CELLPHONE_API = "VerifyCellPhoneServlet";
	private static final String USER_REGISTER_API = "UserRegisterServlet";

	private String user_cellphone = "";// 用户注册电话
	private String user_name = "";// 用户注册姓名
	private String user_avatar = "";// 用户注册头像
	private String user_gender = "";// 用户注册性别
	private String user_birthday = "";// 用户注册生日
	private String user_password = "";// 用户注册密码

	public String getUser_cellphone() {
		return user_cellphone;
	}

	public void setUser_cellphone(String user_cellphone) {
		this.user_cellphone = user_cellphone;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	/**
	 * 验证手机号手机号是否已被注册
	 * 
	 * @return
	 */
	public RetError vefifyCellPhone() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_cellphone", user_cellphone);
		Result ret = ApiRequest.request(VERIFY_CELLPHONE_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	/**
	 * 用户注册
	 * 
	 * @return
	 */
	public RetError userRegister() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", user_name);
		params.put("user_cellphone", user_cellphone);
		params.put("user_password", user_password);
		params.put("user_gender", user_gender);
		params.put("user_birthday", user_birthday);
		Result ret = ApiRequest.requestWithFile(USER_REGISTER_API, params,
				new File(user_avatar), parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}

	}
}
