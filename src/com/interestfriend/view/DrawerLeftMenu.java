package com.interestfriend.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.interestfriend.activity.FriendVertifyListActivity;
import com.interestfriend.activity.MyUserFriendActivity;
import com.interestfriend.activity.SettingActivity;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.task.UpDateNewVersionTask;
import com.interestfriend.task.UpDateNewVersionTask.UpDateVersion;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;

public class DrawerLeftMenu extends FrameLayout implements OnClickListener {
	private Context mContext;
	private View rootView;
	private ImageView img_avatar;
	private TextView txt_user_name;
 	private TextView txt_message;
	private TextView txt_user_info;
	private TextView txt_setting;
	// private TextView txt_friend_vertify;
	private TextView txt_my_friend;

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
		checkVersion();
		String user_avatar = SharedUtils.getAPPUserAvatar();
		setName(SharedUtils.getAPPUserName());
		UniversalImageLoadTool.disPlay(user_avatar, img_avatar,
				R.drawable.picture_default_head);
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
 		txt_message = (TextView) rootView.findViewById(R.id.txt_message);
		txt_user_info = (TextView) rootView.findViewById(R.id.txt_user_info);
		txt_setting = (TextView) rootView.findViewById(R.id.txt_seting);
		txt_my_friend = (TextView) rootView.findViewById(R.id.txt_my_friend);
 		txt_message.setOnClickListener(this);
		txt_user_info.setOnClickListener(this);
		txt_setting.setOnClickListener(this);
		img_avatar.setOnClickListener(this);
		txt_my_friend.setOnClickListener(this);
		addView(rootView);
	}

	private void setNewVersionPrompt() {
		if (SharedUtils.getNewVersion()) {
			Drawable prompt = getResources().getDrawable(R.drawable.prompt);
			prompt.setBounds(0, 0, prompt.getMinimumWidth(),
					prompt.getMinimumHeight());
			// Drawable setting =
			// getResources().getDrawable(R.drawable.setting);
			// setting.setBounds(0, 0, setting.getMinimumWidth(),
			// setting.getMinimumHeight());
			txt_setting.setCompoundDrawables(null, null, prompt, null);
		}
	}

	public void setMessagePrompt(boolean visible) {
		if (visible) {
			Drawable prompt = getResources().getDrawable(R.drawable.prompt);
			prompt.setBounds(0, 0, prompt.getMinimumWidth(),
					prompt.getMinimumHeight());
			// Drawable message =
			// getResources().getDrawable(R.drawable.message);
			// message.setBounds(0, 0, message.getMinimumWidth(),
			// message.getMinimumHeight());
			txt_message.setCompoundDrawables(null, null, prompt, null);
		} else {
			// Drawable message =
			// getResources().getDrawable(R.drawable.message);
			// message.setBounds(0, 0, message.getMinimumWidth(),
			// message.getMinimumHeight());
			txt_message.setCompoundDrawables(null, null, null, null);
		}
	}

	// public void setFriendVertifyPrompt(boolean visible) {
	// if (visible) {
	// Drawable prompt = getResources().getDrawable(R.drawable.prompt);
	// prompt.setBounds(0, 0, prompt.getMinimumWidth(),
	// prompt.getMinimumHeight());
	// Drawable message = getResources().getDrawable(R.drawable.message);
	// message.setBounds(0, 0, message.getMinimumWidth(),
	// message.getMinimumHeight());
	// txt_friend_vertify
	// .setCompoundDrawables(message, null, prompt, null);
	// } else {
	// Drawable message = getResources().getDrawable(R.drawable.message);
	// message.setBounds(0, 0, message.getMinimumWidth(),
	// message.getMinimumHeight());
	// txt_friend_vertify.setCompoundDrawables(message, null, null, null);
	// }
	// }

	@SuppressLint("NewApi")
	private void checkVersion() {
		if (!Utils.isNetworkAvailable()) {
			return;
		}
		UpDateNewVersionTask task = new UpDateNewVersionTask(mContext, false);
		task.setCallBack(new UpDateVersion() {
			@Override
			public void getNewVersion(int rt, String versionCode, String link,
					String version_info) {
				if (rt == 0) {
					SharedUtils.settingNewVersion(false);
					return;
				}
				SharedUtils.settingNewVersion(true);
				setNewVersionPrompt();
			}
		});
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			task.execute();
		} else {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
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
			intent.putExtra("circle_member", MyApplation.getMemberSelf());
			intent.setClass(mContext, CircleMemberOfSelfInfoActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.txt_seting:
			mContext.startActivity(new Intent(mContext, SettingActivity.class));
			break;
		case R.id.img_avatar:
			List<String> imgUrl = new ArrayList<String>();
			imgUrl.add(SharedUtils.getAPPUserAvatar());
			intent = new Intent(mContext, ImagePagerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
					(Serializable) imgUrl);
			intent.putExtras(bundle);
			intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
			mContext.startActivity(intent);
			break;
  		case R.id.txt_my_friend:
			mContext.startActivity(new Intent(mContext,
					MyUserFriendActivity.class));
			break;
		default:
			break;
		}
		com.interestfriend.utils.Utils.leftOutRightIn(mContext);
	}
}
