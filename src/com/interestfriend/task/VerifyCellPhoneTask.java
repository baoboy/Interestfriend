package com.interestfriend.task;

import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;

public class VerifyCellPhoneTask extends
		BaseAsyncTask<User, Void, RetError> {
	private User register;

	@Override
	protected RetError doInBackground(User... params) {
		register = params[0];
		RetError ret = register.vefifyCellPhone();
		return ret;
	}
}
