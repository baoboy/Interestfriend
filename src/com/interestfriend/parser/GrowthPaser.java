package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.Comment;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.Praise;
import com.interestfriend.data.result.Result;

public class GrowthPaser implements IParser {
	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		int cid = jsonObj.getInt("circle_id");
		JSONObject growthJson = jsonObj.getJSONObject("growth");
		if (growthJson == null) {
			return Result.defContentErrorResult();
		}

		// growth info
		int growth_id = growthJson.getInt("growth_id");
		int publisher = growthJson.getInt("publisher_id");
		String content = growthJson.getString("content");
		String published = growthJson.getString("time");
		String publisher_name = growthJson.getString("publisher_name");
		String publisher_avatar = growthJson.getString("publisher_avatar");
		int isPraise = growthJson.getInt("isPraise");
		int praise_count = growthJson.getInt("praise_count");
		// growth images
		JSONArray jsonImages = growthJson.getJSONArray("images");
		List<GrowthImage> images = new ArrayList<GrowthImage>();
		for (int j = 0; j < jsonImages.length(); j++) {
			JSONObject obj2 = (JSONObject) jsonImages.opt(j);
			int imgId = obj2.getInt("img_id");
			String img = obj2.getString("img_url");
			GrowthImage gimg = new GrowthImage(cid, growth_id, imgId, img);
			images.add(gimg);
		}
		// comments
		JSONArray commentsJson = growthJson.getJSONArray("comments");
		List<Comment> comments = new ArrayList<Comment>();
		for (int j = 0; j < commentsJson.length(); j++) {
			JSONObject obj2 = (JSONObject) commentsJson.opt(j);
			int comment_id = obj2.getInt("comment_id");
			int publisher_id = obj2.getInt("publisher_id");
			String comment_time = obj2.getString("comment_time");
			String comment_content = obj2.getString("comment_content");
			String comm_publisher_name = obj2.getString("publisher_name");
			String comm_publisher_avatar = obj2.getString("publisher_avatar");
			String reply_someone_name = obj2.getString("reply_someone_name");
			int reply_someone_id = obj2.getInt("reply_someone_id");
			Comment comment = new Comment();
			comment.setComment_content(comment_content);
			comment.setComment_id(comment_id);
			comment.setComment_time(comment_time);
			comment.setPublisher_id(publisher_id);
			comment.setGrowth_id(growth_id);
			comment.setPublisher_avatar(comm_publisher_avatar);
			comment.setPublisher_name(comm_publisher_name);
			comment.setReply_someone_name(reply_someone_name);
			comment.setReply_someone_id(reply_someone_id);
			comments.add(comment);

		}
		// JSONArray jsonPraise = obj.getJSONArray("praises");
		List<Praise> praises = new ArrayList<Praise>();
		// for (int k = 0; k < jsonPraise.length(); k++) {
		// JSONObject obj2 = (JSONObject) jsonPraise.opt(k);
		// int user_id = obj2.getInt("user_id");
		// String user_avatar = obj2.getString("user_avatar");
		// Praise praise = new Praise();
		// praise.setUser_avatar(user_avatar);
		// praise.setUser_id(user_id);
		// praise.setGrowth_id(growth_id);
		// praises.add(praise);
		// }
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
		growth.setPraise(isPraise > 0);
		growth.setPraise_count(praise_count);
		growth.setPraises(praises);
		Growth gl = new Growth();
		Result ret = new Result();
		ret.setData(gl);
		return ret;
	}

}
