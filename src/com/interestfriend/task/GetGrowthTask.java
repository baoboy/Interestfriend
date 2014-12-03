package com.interestfriend.task;

import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.GrowthState;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetGrowthTask extends BaseAsyncTask<Growth, Void, RetError> {
	private Growth growth;

	@Override
	protected RetError doInBackground(Growth... params) {
		growth = params[0];
		RetError ret = growth.getGrowthByGrwothID();
		if (ret == RetError.NONE) {
			growth.setState(GrowthState.DEL);
			growth.write(DBUtils.getDBsa(2));
			growth.setState(GrowthState.ADD);
			growth.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
