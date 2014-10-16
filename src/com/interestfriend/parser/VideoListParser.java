package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.Video;
import com.interestfriend.data.VideoList;
import com.interestfriend.data.result.Result;

public class VideoListParser implements IParser {
	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		int cid = jsonObj.getInt("cid");
		JSONArray jsonArr = jsonObj.getJSONArray("videos");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<Video> videos = new ArrayList<Video>();
		long start = 0L, end = 0L;
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			// video info
			int video_id = obj.getInt("video_id");
			int publisher = obj.getInt("publisher_id");
			String video_img = obj.getString("video_img");
			String video_path = obj.getString("video_path");
			int video_size = obj.getInt("video_size");
			int video_duration = obj.getInt("video_duration");
			String time = obj.getString("time");
			Video vd = new Video();
			vd.setCid(cid);
			vd.setPublisher_id(publisher);
			vd.setVideo_size(video_size);
			vd.setVideo_path(video_path);
			vd.setVideo_img(video_img);
			vd.setVideo_id(video_id);
			vd.setVideo_duration(video_duration);
			vd.setTime(time);
			videos.add(vd);

		}
		VideoList vl = new VideoList(cid);
		vl.setVideos(videos);
		Result ret = new Result();
		ret.setData(vl);
		return ret;
	}

}
