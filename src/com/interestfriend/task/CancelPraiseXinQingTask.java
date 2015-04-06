package com.interestfriend.task;

import com.interestfriend.data.XinQing;
import com.interestfriend.data.enums.RetError;

public class CancelPraiseXinQingTask extends
		BaseAsyncTask<XinQing, Void, RetError> {
	private XinQing xinqing;

	@Override
	protected RetError doInBackground(XinQing... params) {
		xinqing = params[0];
		RetError ret = xinqing.cancelpraiseGrowth();
		if (ret == RetError.NONE) {
			xinqing.setPraise(!xinqing.isPraise());
		}
		return ret;
	}

}
