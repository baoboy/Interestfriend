package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.result.Result;
import com.interestfriend.utils.DateUtils;

public class GrowthListParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		int cid = jsonObj.getInt("cid");
		JSONArray jsonArr = jsonObj.getJSONArray("growths");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<Growth> growths = new ArrayList<Growth>();
		long start = 0L, end = 0L;
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			// growth info
			int growth_id = obj.getInt("growth_id");
			int publisher = obj.getInt("publisher_id");
			String content = obj.getString("content");
			String published = obj.getString("time");

			// growth images
			JSONArray jsonImages = obj.getJSONArray("images");
			List<GrowthImage> images = new ArrayList<GrowthImage>();
			for (int j = 0; j < jsonImages.length(); j++) {
				JSONObject obj2 = (JSONObject) jsonImages.opt(j);
				int imgId = obj2.getInt("img_id");
				String img = obj2.getString("img_url");
				GrowthImage gimg = new GrowthImage(cid, growth_id, imgId, img);
				images.add(gimg);
			}

			Growth growth = new Growth();
			growth.setCid(cid);
			growth.setContent(content);
			growth.setGrowth_id(growth_id);
			growth.setImages(images);
			growth.setPublished(published);
			growth.setPublisher_id(publisher);
			growths.add(growth);

		}

		GrowthList gl = new GrowthList(cid);
		gl.setGrowths(growths);
		Result ret = new Result();
		ret.setData(gl);
		return ret;
	}

}
