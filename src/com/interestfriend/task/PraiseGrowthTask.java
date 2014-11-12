package com.interestfriend.task;

import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class PraiseGrowthTask extends BaseAsyncTask<Growth, Void, RetError> {
	private Growth growth;

	@Override
	protected RetError doInBackground(Growth... params) {
		growth = params[0];
		RetError ret = growth.praiseGrowth();
		if (ret == RetError.NONE) {
			growth.setPraise(!growth.isPraise());
			growth.setStatus(com.interestfriend.data.AbstractData.Status.UPDATE);
			growth.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
