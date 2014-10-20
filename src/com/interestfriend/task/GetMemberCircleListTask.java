package com.interestfriend.task;

import com.interestfriend.data.CirclesList;
import com.interestfriend.data.enums.RetError;

public class GetMemberCircleListTask extends
		BaseAsyncTask<CirclesList, Void, RetError> {
	private CirclesList list;

	@Override
	protected RetError doInBackground(CirclesList... params) {
		list = params[0];
		RetError ret = list.getMemberCircleList();
		return ret;
	}
}
