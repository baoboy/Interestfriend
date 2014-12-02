package com.interestfriend.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.activity.ChatAllHistoryActivity;
import com.interestfriend.activity.CircleMemberOfSelfInfoActivity;
import com.interestfriend.activity.SettingActivity;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetUserInfoTask;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.UniversalImageLoadTool;

public class DrawerLeftMenu extends FrameLayout implements OnClickListener {
	private Context mContext;
	private View rootView;
	private ImageView img_avatar;
	private TextView txt_user_name;
	private LinearLayout layotu_parent;
	private TextView txt_message;
	private TextView txt_user_info;
	private TextView txt_setting;

	private CircleMember memberSelf = new CircleMember();;

	public DrawerLeftMenu(Context context) {
		super(context);
		mContext = context;
		initView();
		setValue();
	}

	public DrawerLeftMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
		setValue();

	}

	private void setValue() {
		String user_avatar = SharedUtils.getAPPUserAvatar();
		if (!"".equals(user_avatar)) {
			UniversalImageLoadTool.disPlay(user_avatar, img_avatar,
					R.drawable.picture_default_head);
			setName(SharedUtils.getAPPUserName());
			initMember();
			return;
		}
		getUserInfo();
	}

	public void setAvatar(String user_avatar) {
		UniversalImageLoadTool.disPlay(user_avatar, img_avatar,
				R.drawable.picture_default_head);
	}

	public void setName(String name) {
		txt_user_name.setText(name);

	}

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.drawer_left_view_layout, null);
		img_avatar = (ImageView) rootView.findViewById(R.id.img_avatar);
		txt_user_name = (TextView) rootView.findViewById(R.id.txt_user_name);
		txt_user_name.getBackground().setAlpha(120);
		layotu_parent = (LinearLayout) rootView
				.findViewById(R.id.layout_parent);
		txt_message = (TextView) rootView.findViewById(R.id.txt_message);
		txt_user_info = (TextView) rootView.findViewById(R.id.txt_user_info);
		txt_setting = (TextView) rootView.findViewById(R.id.txt_seting);
		layotu_parent.setOnClickListener(this);
		txt_message.setOnClickListener(this);
		txt_user_info.setOnClickListener(this);
		txt_setting.setOnClickListener(this);
		addView(rootView);
	}

	public void setMessagePrompt(boolean visible) {
		if (visible) {
			Drawable prompt = getResources().getDrawable(R.drawable.prompt);
			prompt.setBounds(0, 0, prompt.getMinimumWidth(),
					prompt.getMinimumHeight());
			Drawable message = getResources().getDrawable(
					R.drawable.menu_message);
			message.setBounds(0, 0, message.getMinimumWidth(),
					message.getMinimumHeight());
			txt_message.setCompoundDrawables(message, null, prompt, null);
		} else {
			Drawable message = getResources().getDrawable(
					R.drawable.menu_message);
			message.setBounds(0, 0, message.getMinimumWidth(),
					message.getMinimumHeight());
			txt_message.setCompoundDrawables(message, null, null, null);
		}
	}

	private void getUserInfo() {
		final User user = new User();
		GetUserInfoTask task = new GetUserInfoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (result == RetError.NONE) {
					setAvatar(user.getUser_avatar());
					setName(user.getUser_name());
					initMember();
				}
			}
		});
		task.executeParallel(user);
	}

	private void initMember() {
		memberSelf.setUser_avatar(SharedUtils.getAPPUserAvatar());
		memberSelf.setUser_birthday(SharedUtils.getAPPUserBirthday());
		memberSelf.setUser_declaration(SharedUtils.getAPPUserDeclaration());
		memberSelf.setUser_description(SharedUtils.getAPPUserDescription());
		memberSelf.setUser_gender(SharedUtils.getAPPUserGender());
		memberSelf.setUser_id(SharedUtils.getIntUid());
		memberSelf.setUser_name(SharedUtils.getAPPUserName());
		memberSelf.setUser_register_time(SharedUtils.getAPPUserRegisterTime());
		memberSelf.setSortkey(SharedUtils.getAPPUserSortKey());
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.txt_message:
			mContext.startActivity(new Intent(mContext,
					ChatAllHistoryActivity.class));
			break;
		case R.id.txt_user_info:
			intent = new Intent();
			intent.putExtra("circle_member", memberSelf);
			intent.setClass(mContext, CircleMemberOfSelfInfoActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.txt_seting:
			mContext.startActivity(new Intent(mContext, SettingActivity.class));
			break;
		default:
			break;
		}
		com.interestfriend.utils.Utils.leftOutRightIn(mContext);
	}
}
