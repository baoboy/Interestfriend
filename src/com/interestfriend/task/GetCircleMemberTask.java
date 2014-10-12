package com.interestfriend.task;

import com.interestfriend.data.CircleMemberList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetCircleMemberTask extends
		BaseAsyncTask<CircleMemberList, Void, RetError> {
	private CircleMemberList list;

	@Override
	protected RetError doInBackground(CircleMemberList... params) {
		list = params[0];
		RetError ret = list.getCircleMemberList();
		if (ret == RetError.NONE) {
			list.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
