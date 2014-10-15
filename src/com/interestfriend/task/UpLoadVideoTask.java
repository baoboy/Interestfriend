package com.interestfriend.task;

import com.interestfriend.data.Video;
import com.interestfriend.data.enums.RetError;

public class UpLoadVideoTask extends BaseAsyncTask<Video, Void, RetError> {
	private Video video;

	@Override
	protected RetError doInBackground(Video... params) {
		video = params[0];
		RetError ret = video.upLoadVideo();
		return ret;
	}
}
