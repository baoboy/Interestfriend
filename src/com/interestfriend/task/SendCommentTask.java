package com.interestfriend.task;

import com.interestfriend.data.Comment;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class SendCommentTask extends BaseAsyncTask<Comment, Void, RetError> {
	private Comment comment;

	@Override
	protected RetError doInBackground(Comment... params) {
		comment = params[0];
		RetError ret = comment.sendComment();
		if (ret == RetError.NONE) {
			comment.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
