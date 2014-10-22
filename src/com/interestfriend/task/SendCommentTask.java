package com.interestfriend.task;

import com.interestfriend.data.Comment;
import com.interestfriend.data.enums.RetError;

public class SendCommentTask extends BaseAsyncTask<Comment, Void, RetError> {
	private Comment comment;

	@Override
	protected RetError doInBackground(Comment... params) {
		comment = params[0];
		RetError ret = comment.sendComment();
		return ret;
	}

}
