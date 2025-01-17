package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;

public class UpdateUserAddressTask extends
		BaseAsyncTask<CircleMember, Void, RetError> {
	private CircleMember member;

	@Override
	protected RetError doInBackground(CircleMember... params) {
		member = params[0];
		RetError ret = member.upDateUserAddress();
		return ret;
	}

}
