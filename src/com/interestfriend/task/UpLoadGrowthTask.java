package com.interestfriend.task;

import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class UpLoadGrowthTask extends BaseAsyncTask<Growth, Void, RetError> {
	private Growth growth;

	@Override
	protected RetError doInBackground(Growth... params) {
		growth = params[0];
		RetError ret = growth.uploadForAdd();
		if (ret == RetError.NONE) {
			growth.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
