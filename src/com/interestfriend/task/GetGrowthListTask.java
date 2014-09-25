package com.interestfriend.task;

import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;

public class GetGrowthListTask extends
		BaseAsyncTask<GrowthList, Void, RetError> {
	private GrowthList list;

	@Override
	protected RetError doInBackground(GrowthList... params) {
		list = params[0];
		RetError ret = list.getGrowthList();
		return ret;
	}

}
