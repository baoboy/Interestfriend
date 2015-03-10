package com.interestfriend.task;

import com.interestfriend.data.ChatUserDao;
import com.interestfriend.data.UserFriendList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetUserFriendTask extends
		BaseAsyncTask<UserFriendList, Void, RetError> {
	private UserFriendList list;

	@Override
	protected RetError doInBackground(UserFriendList... params) {
		list = params[0];
		RetError ret = list.getUserFriendList();
		if (ret == RetError.NONE) {
			list.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
