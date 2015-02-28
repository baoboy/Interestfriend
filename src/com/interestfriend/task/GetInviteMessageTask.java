package com.interestfriend.task;

import com.interestfriend.data.InviteMessgeDao;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetInviteMessageTask extends
		BaseAsyncTask<InviteMessgeDao, Void, RetError> {
	private InviteMessgeDao dao;

	@Override
	protected RetError doInBackground(InviteMessgeDao... params) {
		dao = params[0];
		dao.getMessagesList(DBUtils.getDBsa(2));
		return RetError.NONE;
	}

}
