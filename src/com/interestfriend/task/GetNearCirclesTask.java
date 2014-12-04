package com.interestfriend.task;

import com.interestfriend.data.NearCircleList;
import com.interestfriend.data.enums.RetError;

public class GetNearCirclesTask extends
		BaseAsyncTask<NearCircleList, Void, RetError> {
	private NearCircleList list;
	private int page;
	private double longitude;
	private double latitude;

	public GetNearCirclesTask(double longitude, double latitude, int page) {
		this.page = page;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected RetError doInBackground(NearCircleList... params) {
		list = params[0];
		RetError ret = list
				.searchCirclesByLongitudey(longitude, latitude, page);
		return ret;
	}
}
