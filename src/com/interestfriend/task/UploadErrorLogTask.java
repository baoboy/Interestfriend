package com.interestfriend.task;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.utils.HttpUrlHelper;
import com.interestfriend.utils.Utils;

public class UploadErrorLogTask extends BaseAsyncTask<File, Void, RetError> {
	private File file;

	@Override
	protected RetError doInBackground(File... params) {
		file = params[0];
		Map<String, Object> map = new HashMap<String, Object>();
		String result = HttpUrlHelper.postDataWithFile(
				HttpUrlHelper.DEFAULT_HOST + "ErrorLog", map, file);
		Utils.print("err:::::::::::::result" + result);
 		return RetError.NONE;
	}
}
