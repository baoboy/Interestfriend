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

import java.io.Serializable;
import java.util.HashMap;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.SimpleParser;
import com.interestfriend.utils.SharedUtils;

public class InviteMessage implements Serializable {
	private static final String ADD_FRIEND_API = "AddUserFriendServlet";

	private String from_user_name;
	private String from_user_avatar = "";
	private int from_user_id;
	private String from_user_chat_id = "";
	private String from_circle = "";
	// 时间
	private long time;
	// 添加理由
	private String reason;

	// 未验证，已同意等状态
	private InviteMesageStatus status;

	private int id;

	public String getFrom_circle() {
		return from_circle;
	}

	public void setFrom_circle(String from_circle) {
		this.from_circle = from_circle;
	}

	public String getFrom_user_chat_id() {
		return from_user_chat_id;
	}

	public void setFrom_user_chat_id(String from_user_chat_id) {
		this.from_user_chat_id = from_user_chat_id;
	}

	public String getFrom_user_name() {
		return from_user_name;
	}

	public void setFrom_user_name(String from_user_name) {
		this.from_user_name = from_user_name;
	}

	public String getFrom_user_avatar() {
		return from_user_avatar;
	}

	public void setFrom_user_avatar(String from_user_avatar) {
		this.from_user_avatar = from_user_avatar;
	}

	public int getFrom_user_id() {
		return from_user_id;
	}

	public void setFrom_user_id(int from_user_id) {
		this.from_user_id = from_user_id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public InviteMesageStatus getStatus() {
		return status;
	}

	public void setStatus(InviteMesageStatus status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public enum InviteMesageStatus {
		/** 被邀请 */
		BEINVITEED,
		/** 被拒绝 */
		BEREFUSED,
		/** 对方同意 */
		BEAGREED,
		/** 对方申请 */
		BEAPPLYED,
		/** 我同意了对方的请求 */
		AGREED,
		/** 我拒绝了对方的请求 */
		REFUSED

	}

	public RetError addFriend() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", SharedUtils.getUid());
		params.put("user_name", SharedUtils.getAPPUserName());
		params.put("user_avatar", SharedUtils.getAPPUserAvatar());
		params.put("user_chat_id", SharedUtils.getAPPUserChatID());
		params.put("user_friend_id", from_user_id);
		params.put("user_friend_name", from_user_name);
		params.put("user_friend_avatar", from_user_avatar);
		params.put("user_friend_chat_id", from_user_chat_id);
		params.put("user_friend_circle", from_circle);
		Result ret = ApiRequest.request(ADD_FRIEND_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
