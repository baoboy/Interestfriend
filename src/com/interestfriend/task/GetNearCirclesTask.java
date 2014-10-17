package com.interestfriend.task;

import com.interestfriend.data.NearCircleList;
import com.interestfriend.data.enums.RetError;

public class GetNearCirclesTask extends
		BaseAsyncTask<NearCircleList, Void, RetError> {
	private NearCircleList list;

	@Override
	protected RetError doInBackground(NearCircleList... params) {
		list = params[0];
		RetError ret = list.searchCirclesByLongitudey();
		return ret;
	}

}
