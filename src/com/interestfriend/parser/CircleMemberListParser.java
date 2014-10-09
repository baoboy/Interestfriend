package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.CircleMemberList;
import com.interestfriend.data.result.Result;

public class CircleMemberListParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("members");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<CircleMember> lists = new ArrayList<CircleMember>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int userID = obj.getInt("userID");
			String userName = obj.getString("userName");
			CircleMember member = new CircleMember();
			member.setUser_id(userID);
			member.setUser_name(userName);
			lists.add(member);
		}
		CircleMemberList list = new CircleMemberList();
		list.setCircleMemberLists(lists);
		Result ret = new Result();
		ret.setData(list);
		return ret;
	}

}
