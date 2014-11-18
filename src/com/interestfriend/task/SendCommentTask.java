package com.interestfriend.task;

import com.interestfriend.data.Comment;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class SendCommentTask extends BaseAsyncTask<Comment, Void, RetError> {
	private Comment comment;
	private int growth_publisher_id;

	public SendCommentTask(int growth_publisher_id) {
		this.growth_publisher_id = growth_publisher_id;
	}

	@Override
	protected RetError doInBackground(Comment... params) {
		comment = params[0];
		RetError ret = comment.sendComment(growth_publisher_id);
		if (ret == RetError.NONE) {
			comment.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
