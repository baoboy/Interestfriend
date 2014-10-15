package com.interestfriend.task;

import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.RetError;

public class UpLoadVideoTask extends BaseAsyncTask<Growth, Void, RetError> {
	private Growth growth;

	@Override
	protected RetError doInBackground(Growth... params) {
		growth = params[0];
		RetError ret = growth.upLoadVideo();
		return ret;
	}
}
