package com.interestfriend.task;

import com.interestfriend.data.CirclesList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetCircleListTask extends
		BaseAsyncTask<CirclesList, Void, RetError> {
	private CirclesList list;

	@Override
	protected RetError doInBackground(CirclesList... params) {
		list = params[0];
		RetError ret = list.getCircleList();
		if (ret == RetError.NONE) {
			list.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
