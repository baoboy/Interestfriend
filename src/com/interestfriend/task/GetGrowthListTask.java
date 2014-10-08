package com.interestfriend.task;

import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetGrowthListTask extends
		BaseAsyncTask<GrowthList, Void, RetError> {
	private GrowthList list;

	@Override
	protected RetError doInBackground(GrowthList... params) {
		list = params[0];
		RetError ret = list.getGrowthList();
		if (ret == RetError.NONE) {
			list.writeGrowth(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
