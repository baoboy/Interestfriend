package com.interestfriend.task;

import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class CreateCircleTask extends BaseAsyncTask<Circles, Void, RetError> {
	private Circles circle;

	@Override
	protected RetError doInBackground(Circles... params) {
		circle = params[0];
		RetError ret = circle.createNewCircle();
		if (ret == RetError.NONE) {
			circle.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
