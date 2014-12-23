package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;

public class GetUserInfoTask extends
		BaseAsyncTask<CircleMember, Void, RetError> {
	private CircleMember user;

	@Override
	protected RetError doInBackground(CircleMember... arg0) {
		user = arg0[0];
		RetError ret = user.getMemberInfo();
		return ret;
	}
}
