package com.interestfriend.task;

import com.interestfriend.data.ChatUser;
import com.interestfriend.data.enums.RetError;

public class DelUserFriendTask extends BaseAsyncTask<ChatUser, Void, RetError> {
	private ChatUser user;

	@Override
	protected RetError doInBackground(ChatUser... params) {
		user = params[0];
		RetError ret = user.delFriend();
		return ret;
	}

}
