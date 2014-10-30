package com.interestfriend.task;

import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetGrowthFormDBTask extends
		BaseAsyncTask<GrowthList, Void, RetError> {
	private GrowthList list;

	@Override
	protected RetError doInBackground(GrowthList... params) {
		list = params[0];
		long time = System.currentTimeMillis();
		list.read(DBUtils.getDBsa(1));
		System.out.println("time:::::::::::::::read"
				+ (System.currentTimeMillis() - time));
		return RetError.NONE;
	}

}
