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
			txt_user_name.setText(SharedUtils.getAPPUserName());
			return;
		}
		getUserInfo();
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
		layotu_parent.setOnClickListener(this);
		txt_message.setOnClickListener(this);
		addView(rootView);
	}

	public void setMessagePrompt(boolean visible) {
		if (visible) {
			Drawable prompt = getResources().getDrawable(R.drawable.prompt);
			prompt.setBounds(0, 0, prompt.getMinimumWidth(),
					prompt.getMinimumHeight());
			txt_message.setCompoundDrawables(null, null, prompt, null);
		} else {
			txt_message.setCompoundDrawables(null, null, null, null);
		}
	}

	private void getUserInfo() {
		final User user = new User();
		GetUserInfoTask task = new GetUserInfoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (result == RetError.NONE) {
					UniversalImageLoadTool.disPlay(user.getUser_avatar(),
							img_avatar, R.drawable.picture_default_head);
				}
			}
		});
		task.execute(user);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_message:
			mContext.startActivity(new Intent(mContext,
					ChatAllHistoryActivity.class));
			break;

		default:
			break;
		}
	}
}
