package com.interestfriend.interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.interestfriend.activity.CircleMemberActivity;
import com.interestfriend.activity.CircleMemberOfSelfInfoActivity;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.CircleMember;
import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

public class OnAvatarClick implements OnClickListener {
	private int user_id;
	private Context mContext;

	public OnAvatarClick(int user_id, Context context) {
		this.user_id = user_id;
		this.mContext = context;
	}

	@Override
	public void onClick(View v) {
		if (user_id < 0) {
			return;
		}
		CircleMember member = new CircleMember();
		member.setUser_id(user_id);
		member.read(DBUtils.getDBsa(1));
		Intent intent = new Intent();
		if (member.getUser_id() == SharedUtils.getIntUid()) {
			intent.putExtra("circle_member", MyApplation.getMemberSelf());
			intent.setClass(mContext, CircleMemberOfSelfInfoActivity.class);
		} else {
			intent.putExtra("circle_member", member);
			intent.setClass(mContext, CircleMemberActivity.class);
		}
		mContext.startActivity(intent);
		Utils.leftOutRightIn(mContext);
	}
}
