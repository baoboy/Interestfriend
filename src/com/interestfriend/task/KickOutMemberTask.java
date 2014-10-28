package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class KickOutMemberTask extends
		BaseAsyncTask<CircleMember, Void, RetError> {
	private CircleMember member;

	@Override
	protected RetError doInBackground(CircleMember... params) {
		member = params[0];
		RetError ret = member.kickOutMember();
		if (ret == RetError.NONE) {
			member.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
