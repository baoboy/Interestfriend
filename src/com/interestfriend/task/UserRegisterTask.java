package com.interestfriend.task;

import com.interestfriend.data.Register;
import com.interestfriend.data.enums.RetError;

public class UserRegisterTask extends BaseAsyncTask<Register, Void, RetError> {
	private Register register;

	@Override
	protected RetError doInBackground(Register... params) {
		register = params[0];
		RetError ret = register.userRegister();
		return ret;
	}

}
