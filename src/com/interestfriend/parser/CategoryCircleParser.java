package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.CategoryCircle;
import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.result.Result;

public class CategoryCircleParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("catagory");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<CategoryCircle> cateLists = new ArrayList<CategoryCircle>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int code = obj.getInt("code");
			String name = obj.getString("name");
			String logo = obj.getString("logo");
			CategoryCircle c = new CategoryCircle();
			c.setName(name);
			c.setCode(code);
			c.setLogo(logo);
			cateLists.add(c);

		}
		CategoryCircleList lists = new CategoryCircleList();
		lists.setCateLists(cateLists);
	Result ret = new Result();
		ret.setData(lists);
		return ret;
	}

}
