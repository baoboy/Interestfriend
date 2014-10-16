package com.interestfriend.task;

import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.utils.SharedUtils;

public class GetUserInfoTask extends BaseAsyncTask<User, Void, RetError> {
	private User user;

	@Override
	protected RetError doInBackground(User... arg0) {
		user = arg0[0];
		RetError ret = user.getUserInfo();
		if (ret == RetError.NONE) {
			SharedUtils.setAPPUserAvatar(user.getUser_avatar());
			SharedUtils.setAPPUserName(user.getUser_name());
		}
		return ret;
	}
}
