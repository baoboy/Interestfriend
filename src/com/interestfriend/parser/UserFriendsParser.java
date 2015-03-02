package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.ChatUser;
import com.interestfriend.data.UserFriendList;
import com.interestfriend.data.result.Result;

public class UserFriendsParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("friends");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<ChatUser> lists = new ArrayList<ChatUser>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int userID = obj.getInt("user_friend_id");
			String userName = obj.getString("user_friend_name");
			String userAvatar = obj.getString("user_friend_avatar");
			String userChatId = obj.getString("user_friend_chat_id");
			String user_friend_circle = obj.getString("user_friend_circle");
			ChatUser user = new ChatUser();
			user.setUser_avatar(userAvatar);
			user.setUser_chat_id(userChatId);
			user.setUser_id(userID);
			user.setUser_name(userName);
			user.setUser_friend_circle(user_friend_circle);
			lists.add(user);
		}
		UserFriendList list = new UserFriendList();
		list.setLists(lists);
		Result ret = new Result();
		ret.setData(list);
		return ret;
	}
}
