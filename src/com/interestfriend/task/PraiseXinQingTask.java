package com.interestfriend.task;

import com.interestfriend.data.XinQing;
import com.interestfriend.data.enums.RetError;

public class PraiseXinQingTask extends BaseAsyncTask<XinQing, Void, RetError> {
	private XinQing xinqing;

	@Override
	protected RetError doInBackground(XinQing... params) {
		xinqing = params[0];
		RetError ret = xinqing.praiseXinQing();
		return ret;
	}

}
