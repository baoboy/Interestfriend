package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;

public class ReceiveJoinCircleTask extends
		BaseAsyncTask<CircleMember, Void, RetError> {
	private CircleMember member;
	private String request_join_circle_user_huanxin_username;
	private String join_circle_name = "";

	public ReceiveJoinCircleTask(
			String request_join_circle_user_huanxin_username,
			String join_circle_name) {
		this.request_join_circle_user_huanxin_username = request_join_circle_user_huanxin_username;
		this.join_circle_name = join_circle_name;
	}

	@Override
	protected RetError doInBackground(CircleMember... params) {
		member = params[0];
		RetError ret = member.receiveJoinCircleRequest(
				request_join_circle_user_huanxin_username, join_circle_name);
		return ret;
	}

}
