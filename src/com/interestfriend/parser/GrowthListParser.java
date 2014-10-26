package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.Comment;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.result.Result;

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
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			// growth info
			int growth_id = obj.getInt("growth_id");
			int publisher = obj.getInt("publisher_id");
			String content = obj.getString("content");
			String published = obj.getString("time");
			String publisher_name = obj.getString("publisher_name");
			String publisher_avatar = obj.getString("publisher_avatar");
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
			// comments
			JSONArray commentsJson = obj.getJSONArray("comments");
			List<Comment> comments = new ArrayList<Comment>();
			for (int j = 0; j < commentsJson.length(); j++) {
				JSONObject obj2 = (JSONObject) commentsJson.opt(j);
				int comment_id = obj2.getInt("comment_id");
				int publisher_id = obj2.getInt("publisher_id");
				String comment_time = obj2.getString("comment_time");
				String comment_content = obj2.getString("comment_content");
				publisher_name = obj.getString("publisher_name");
				publisher_avatar = obj.getString("publisher_avatar");
				Comment comment = new Comment();
				comment.setComment_content(comment_content);
				comment.setComment_id(comment_id);
				comment.setComment_time(comment_time);
				comment.setPublisher_id(publisher_id);
				comment.setGrowth_id(growth_id);
				comment.setPublisher_avatar(publisher_avatar);
				comment.setPublisher_name(publisher_name);
				comments.add(comment);

			}
			Growth growth = new Growth();
			growth.setCid(cid);
			growth.setContent(content);
			growth.setGrowth_id(growth_id);
			growth.setImages(images);
			growth.setComments(comments);
			growth.setPublished(published);
			growth.setPublisher_id(publisher);
			growth.setPublisher_avatar(publisher_avatar);
			growth.setPublisher_name(publisher_name);
			int index = comments.size() > 2 ? 2 : comments.size();
			for (int k = 0; k < index; k++) {
				growth.getCommentsListView().add(comments.get(k));
			}
			growths.add(growth);

		}
		GrowthList gl = new GrowthList(cid);
		gl.setGrowths(growths);
		Result ret = new Result();
		ret.setData(gl);
		return ret;
	}

}
