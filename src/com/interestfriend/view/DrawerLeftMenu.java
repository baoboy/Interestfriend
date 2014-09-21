package com.interestfriend.view;

import com.interestfriend.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class DrawerLeftMenu extends FrameLayout {
	private Context mContext;
	private View rootView;

	public DrawerLeftMenu(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public DrawerLeftMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.drawer_left_view_layout, null);
		addView(rootView);
	}
}
