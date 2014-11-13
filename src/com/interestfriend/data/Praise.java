package com.interestfriend.data;

import java.io.Serializable;

public class Praise implements Serializable {
	private int user_id;
	private String user_avatar = "";

	@Override
	public String toString() {
		return "user_id:" + user_id;
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
