package com.interestfriend.task;

import com.interestfriend.data.InviteMessage;
import com.interestfriend.data.enums.RetError;

public class RefuseInvitationTask extends
		BaseAsyncTask<InviteMessage, Void, RetError> {
	private InviteMessage message;

	@Override
	protected RetError doInBackground(InviteMessage... params) {
		message = params[0];
		RetError ret = message.refuseInvitation();
		return ret;
	}

}
