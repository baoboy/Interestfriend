package com.interestfriend.task;

import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.RetError;

public class GetGrowthTask extends BaseAsyncTask<Growth, Void, RetError> {
	private Growth growth;

	@Override
	protected RetError doInBackground(Growth... params) {
		growth = params[0];
		RetError ret = growth.getGrowthByGrwothID();
		return ret;
	}
}
