package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.CirclesList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.CircleMemberState;
import com.interestfriend.data.enums.CircleState;
import com.interestfriend.data.result.Result;
import com.interestfriend.utils.SharedUtils;

public class MyCircleListParser implements IParser {

	@Override
	public Result<CirclesList> parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("circles");
		long lastReqTime;
		if (jsonObj.has("circle_last_request_time")) {
			lastReqTime = jsonObj.getLong("circle_last_request_time");
			SharedUtils.setCircleLastRequestTime(lastReqTime);
		}
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<MyCircles> lists = new ArrayList<MyCircles>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int id = obj.getInt("circle_id");
			int creator_id = obj.getInt("creator_id");
			String circle_name = obj.getString("circle_name");
			String circle_description = obj.getString("circle_description");
			String circle_avatar = obj.getString("circle_avatar");
			String group_id = obj.getString("group_id");
			int diatance = obj.getInt("distance");
			String circle_state = obj.getString("circle_state");
			String circle_creator_name = obj.getString("circle_creator_name");
			String circle_create_time = obj.getString("circle_create_time");
			String circle_category = obj.getString("circle_category");
			MyCircles circle = new MyCircles();
			circle.setCircle_description(circle_description);
			circle.setCircle_id(id);
			circle.setCircle_logo(circle_avatar);
			circle.setCircle_name(circle_name);
			circle.setCreator_id(creator_id);
			circle.setGroup_id(group_id);
			circle.setDistance(diatance);
			circle.setCircle_state(CircleState.convert(circle_state));
			circle.setCircle_category_name(circle_category);
			circle.setCircle_create_time(circle_create_time);
			circle.setCircle_creator_name(circle_creator_name);
			lists.add(circle);
		}
		CirclesList cl = new CirclesList();
		cl.setListCircles(lists);
		Result<CirclesList> ret = new Result<CirclesList>();
		ret.setData(cl);
		return ret;
	}
}
