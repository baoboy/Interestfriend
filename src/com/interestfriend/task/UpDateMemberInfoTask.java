package com.interestfriend.task;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.CircleMemberState;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.PinYinUtils;

public class UpDateMemberInfoTask extends
		BaseAsyncTask<CircleMember, Void, RetError> {
	private CircleMember member;
	private String clounm = "";
	private String value = "";

	public UpDateMemberInfoTask(String clounm, String value) {
		this.clounm = clounm;
		this.value = value;
	}

	@Override
	protected RetError doInBackground(CircleMember... params) {
		member = params[0];
		RetError ret = member.upDateUserInfo(clounm, value);
		if (ret == RetError.NONE) {
			if ("Í«≥∆".equals(clounm)) {
				member.setUser_name(value);
			} else if ("Ωª”—–˚—‘".equals(clounm)) {
				member.setUser_declaration(value);
			} else if ("∏ˆ»ÀΩÈ…‹".equals(clounm)) {
				member.setUser_description(value);
			}
			member.setState(CircleMemberState.UPDATE);
			member.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
