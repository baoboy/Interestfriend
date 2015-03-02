/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.interestfriend.data;

import com.easemob.chat.EMContact;

public class ChatUser extends EMContact {
	private static final String USER_FRIENDS_API = "GetUserFriendServlet";

	private int unreadMsgCount;
	private String user_name = "";
	private String user_avatar = "";
	private int user_id;
	private String user_chat_id = "";
	private String user_friend_circle = "";

	public String getUser_friend_circle() {
		return user_friend_circle;
	}

	public void setUser_friend_circle(String user_friend_circle) {
		this.user_friend_circle = user_friend_circle;
	}

	public String getUser_chat_id() {
		return user_chat_id;
	}

	public void setUser_chat_id(String user_chat_id) {
		this.user_chat_id = user_chat_id;
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ChatUser)) {
			return false;
		}
		return getUsername().equals(((ChatUser) o).getUsername());
	}

	@Override
	public String toString() {
		return user_name;
	}

}
