package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;

public class JoinCircleTask extends BaseAsyncTask<CircleMember, Void, RetError> {
	private CircleMember member;
	private int circle_creator;
	private String circle_name = "";

	public JoinCircleTask(int circle_creator, String circle_name) {
		this.circle_creator = circle_creator;
		this.circle_name = circle_name;
	}

	@Override
	protected RetError doInBackground(CircleMember... params) {
		member = params[0];
		RetError ret = member.joinOffcialCircle(circle_creator, circle_name);
		return ret;
	}

}
