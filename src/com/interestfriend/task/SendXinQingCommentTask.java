package com.interestfriend.task;

import com.interestfriend.data.XinQingComment;
import com.interestfriend.data.enums.RetError;

public class SendXinQingCommentTask extends
		BaseAsyncTask<XinQingComment, Void, RetError> {
	private XinQingComment comment;
	private int xinqing_publisher_id;

	public SendXinQingCommentTask(int xinqing_publisher_id) {
		this.xinqing_publisher_id = xinqing_publisher_id;
	}

	@Override
	protected RetError doInBackground(XinQingComment... params) {
		comment = params[0];
		RetError ret = comment.sendComment(xinqing_publisher_id);
		return ret;
	}

}
