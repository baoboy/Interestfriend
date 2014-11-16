package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.result.Result;
import com.interestfriend.utils.SharedUtils;

public class UploadGrowthParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}

		if (!jsonObj.has("gid")) {
			return Result.defContentErrorResult();
		}
		String time = jsonObj.getString("time");
		SharedUtils.setGrowthLastRequestTime(time);
		Growth g = new Growth();
		List<GrowthImage> imgs = new ArrayList<GrowthImage>();
		int gid = jsonObj.getInt("gid");
		JSONArray jsonArr = jsonObj.getJSONArray("images");
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int id = obj.getInt("img_id");
			String img = obj.getString("img_url");
			GrowthImage gImage = new GrowthImage(0, gid, id, img);
			imgs.add(gImage);
		}
		g.setGrowth_id(gid);
		g.setImages(imgs);
		g.setPublished(time);
		g.setLast_update_time(time);
		Result ret = new Result();
		ret.setData(g);
		return ret;
	}

}
