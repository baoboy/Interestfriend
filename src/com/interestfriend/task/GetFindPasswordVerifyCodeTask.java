package com.interestfriend.task;

import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;

public class GetFindPasswordVerifyCodeTask extends
		BaseAsyncTask<User, Void, RetError> {

	private User user;

	@Override
	protected RetError doInBackground(User... params) {
		user = params[0];
		RetError ret = user.getFindPasswordVerifyCode();
		return ret;
	}
}
