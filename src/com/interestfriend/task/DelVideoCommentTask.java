package com.interestfriend.task;

import com.interestfriend.data.VideoComment;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class DelVideoCommentTask extends
		BaseAsyncTask<VideoComment, Void, RetError> {
	private VideoComment comment;

	@Override
	protected RetError doInBackground(VideoComment... params) {
		comment = params[0];
		RetError ret = comment.deleteComment();
		if (ret == RetError.NONE) {
			comment.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
