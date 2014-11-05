package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.Comment;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.Video;
import com.interestfriend.data.VideoComment;
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
			String publisher_name = obj.getString("publisher_name");
			String publisher_avatar = obj.getString("publisher_avatar");
			String video_content = obj.getString("video_content");
			Video vd = new Video();
			vd.setCid(cid);
			vd.setPublisher_id(publisher);
			vd.setVideo_size(video_size);
			vd.setVideo_path(video_path);
			vd.setVideo_img(video_img);
			vd.setVideo_id(video_id);
			vd.setVideo_duration(video_duration);
			vd.setTime(time);
			vd.setPublisher_avatar(publisher_avatar);
			vd.setPublisher_name(publisher_name);
			vd.setVideo_content(video_content);
			// comments
			JSONArray commentsJson = obj.getJSONArray("comments");
			List<VideoComment> comments = new ArrayList<VideoComment>();
			for (int j = 0; j < commentsJson.length(); j++) {
				JSONObject obj2 = (JSONObject) commentsJson.opt(j);
				int comment_id = obj2.getInt("comment_id");
				int publisher_id = obj2.getInt("publisher_id");
				String comment_time = obj2.getString("comment_time");
				String comment_content = obj2.getString("comment_content");
				String comm_publisher_name = obj2.getString("publisher_name");
				String comm_publisher_avatar = obj2
						.getString("publisher_avatar");
				String reply_someone_name = obj2
						.getString("reply_someone_name");
				int reply_someone_id = obj2.getInt("reply_someone_id");
				VideoComment comment = new VideoComment();
				comment.setComment_content(comment_content);
				comment.setComment_id(comment_id);
				comment.setComment_time(comment_time);
				comment.setPublisher_id(publisher_id);
				comment.setVideo_id(video_id);
				comment.setPublisher_avatar(comm_publisher_avatar);
				comment.setPublisher_name(comm_publisher_name);
				comment.setReply_someone_name(reply_someone_name);
				comment.setReply_someone_id(reply_someone_id);
				comments.add(comment);
			}
			vd.setComments(comments);
			int index = comments.size() > 2 ? 2 : comments.size();
			for (int k = 0; k < index; k++) {
				vd.getCommentsListView().add(comments.get(k));
			}
			videos.add(vd);

		}
		VideoList vl = new VideoList(cid);
		vl.setVideos(videos);
		Result ret = new Result();
		ret.setData(vl);
		return ret;
	}

}
