package com.interestfriend.task;

import com.interestfriend.data.XinQingList;
import com.interestfriend.data.enums.RetError;

public class GetXinQingListTask extends
		BaseAsyncTask<XinQingList, Void, RetError> {
	private XinQingList list;

	@Override
	protected RetError doInBackground(XinQingList... params) {
		list = params[0];
		return list.refush();
	}

}
