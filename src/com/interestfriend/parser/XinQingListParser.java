package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.XinQing;
import com.interestfriend.data.XinQingList;
import com.interestfriend.data.result.Result;

public class XinQingListParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		List<XinQing> lists = new ArrayList<XinQing>();
		JSONArray jsonArr = jsonObj.getJSONArray("xinqings");
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			XinQing xin = new XinQing();
			int xinqing_id = obj.getInt("xinqing_id");
			xin.setXinqing_id(xinqing_id);
			xin.setContent(obj.getString("content"));
			xin.setImage_url(obj.getString("image_url"));
			xin.setPublish_time(obj.getString("publish_time"));
			xin.setPublisher_avatar(obj.getString("publisher_avatar"));
			xin.setPublisher_name(obj.getString("publisher_name"));
			xin.setPublisher_id(obj.getInt("publisher_id"));
			lists.add(xin);
		}
		XinQingList list = new XinQingList();
		list.setLists(lists);
		Result ret = new Result();
		ret.setData(list);
		return ret;
	}
}
