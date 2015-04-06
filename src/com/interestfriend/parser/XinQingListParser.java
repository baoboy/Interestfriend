package com.interestfriend.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.interestfriend.data.XinQing;
import com.interestfriend.data.XinQingComment;
import com.interestfriend.data.XinQingList;
import com.interestfriend.data.XinQingPraise;
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
			int isPraise = obj.getInt("isPraise");
			xin.setPraise(isPraise > 0);
			// comments
			JSONArray commentsJson = obj.getJSONArray("comments");
			List<XinQingComment> comments = new ArrayList<XinQingComment>();
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
				XinQingComment comment = new XinQingComment();
				comment.setComment_content(comment_content);
				comment.setComment_id(comment_id);
				comment.setComment_time(comment_time);
				comment.setPublisher_id(publisher_id);
				comment.setXinqing_id(xinqing_id);
				comment.setPublisher_avatar(comm_publisher_avatar);
				comment.setPublisher_name(comm_publisher_name);
				comment.setReply_someone_name(reply_someone_name);
				comment.setReply_someone_id(reply_someone_id);
				comments.add(comment);
			}
			sortComment(comments);
			JSONArray jsonPraise = obj.getJSONArray("praises");
			List<XinQingPraise> praises = new ArrayList<XinQingPraise>();
			for (int k = 0; k < jsonPraise.length(); k++) {
				JSONObject obj2 = (JSONObject) jsonPraise.opt(k);
				int user_id = obj2.getInt("user_id");
				String user_avatar = obj2.getString("user_avatar");
				XinQingPraise praise = new XinQingPraise();
				praise.setUser_avatar(user_avatar);
				praise.setUser_id(user_id);
				praise.setXinqing_id(xinqing_id);
				praises.add(praise);
			}
			xin.setPraises(praises);
			xin.setComments(comments);
			xin.setPraise_count(praises.size());
			lists.add(xin);
		}
		XinQingList list = new XinQingList();
		list.setLists(lists);
		Result ret = new Result();
		ret.setData(list);
		return ret;
	}

	private void sortComment(List<XinQingComment> comments) {
		Collections.sort(comments, new Comparator<XinQingComment>() {
			@Override
			public int compare(XinQingComment lhs, XinQingComment rhs) {
				return rhs.getComment_time().compareTo(lhs.getComment_time());
			}
		});

	}
}
