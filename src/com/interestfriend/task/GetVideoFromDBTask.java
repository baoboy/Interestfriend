package com.interestfriend.task;

import com.interestfriend.data.VideoList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetVideoFromDBTask extends
		BaseAsyncTask<VideoList, Void, RetError> {
	private VideoList list;

	@Override
	protected RetError doInBackground(VideoList... params) {
		list = params[0];
		list.read(DBUtils.getDBsa(1));
		return RetError.NONE;
	}

}
