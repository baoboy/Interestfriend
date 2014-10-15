package com.interestfriend.task;

import com.interestfriend.data.VideoList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetVideoListTask extends BaseAsyncTask<VideoList, Void, RetError> {
	private VideoList list;

	@Override
	protected RetError doInBackground(VideoList... params) {
		list = params[0];
		RetError ret = list.getGrowthList();
		if (ret == RetError.NONE) {
			list.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
