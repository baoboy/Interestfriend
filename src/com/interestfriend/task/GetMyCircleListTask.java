package com.interestfriend.task;

import com.interestfriend.data.MyCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetMyCircleListTask extends
		BaseAsyncTask<MyCircleList, Void, RetError> {
	private MyCircleList list;

	@Override
	protected RetError doInBackground(MyCircleList... params) {
		list = params[0];
		RetError ret = list.getCircleList();
		if (ret == RetError.NONE) {
			list.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
