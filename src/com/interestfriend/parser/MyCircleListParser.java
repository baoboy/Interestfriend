package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.CirclesList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.result.Result;

public class MyCircleListParser implements IParser {

	@Override
	public Result<CirclesList> parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("circles");
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
			MyCircles circle = new MyCircles();
			circle.setCircle_description(circle_description);
			circle.setCircle_id(id);
			circle.setCircle_logo(circle_avatar);
			circle.setCircle_name(circle_name);
			circle.setCreator_id(creator_id);
			circle.setGroup_id(group_id);
			circle.setDistance(diatance);
			lists.add(circle);
		}
		CirclesList cl = new CirclesList();
		cl.setListCircles(lists);
		Result<CirclesList> ret = new Result<CirclesList>();
		ret.setData(cl);
		return ret;
	}

}
