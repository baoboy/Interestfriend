package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.SharedUtils;

public class DissolveCircleTask extends BaseAsyncTask<Circles, Void, RetError> {
	private Circles circle;

	@Override
	protected RetError doInBackground(Circles... params) {
		circle = params[0];
		circle.findCircleGroupID(DBUtils.getDBsa(1));
		RetError ret = circle.dissolveCircle();
		if (ret == RetError.NONE) {
			circle.write(DBUtils.getDBsa(2));
			CircleMember member = new CircleMember();
			member.setCircle_id(circle.getCircle_id());
			member.setUser_id(SharedUtils.getIntUid());
			member.setStatus(com.interestfriend.data.AbstractData.Status.DEL);
			member.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
