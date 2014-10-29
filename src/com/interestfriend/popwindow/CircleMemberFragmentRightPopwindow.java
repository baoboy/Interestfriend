package com.interestfriend.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.interestfriend.R;

public class CircleMemberFragmentRightPopwindow implements OnClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private LinearLayout layout_parent;
	private View view;
	private TextView txt_quit_circle;
	private TextView txt_circle_info;
	private OnMenuClick mCallBack;

	public OnMenuClick getmCallBack() {
		return mCallBack;
	}

	public void setmCallBack(OnMenuClick mCallBack) {
		this.mCallBack = mCallBack;
	}

	public CircleMemberFragmentRightPopwindow(Context context, View v) {
		this.mContext = context;
		this.v = v;

		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(
				R.layout.circlememberfragment_right_popwindow_layout, null);
		initView();
		initPopwindow();

	}

	private void initView() {
		layout_parent = (LinearLayout) view.findViewById(R.id.parent);
		layout_parent.setOnClickListener(this);
		txt_quit_circle = (TextView) view.findViewById(R.id.txt_quit_circle);
		txt_circle_info = (TextView) view.findViewById(R.id.txt_circle_info);
		txt_quit_circle.setOnClickListener(this);
		txt_circle_info.setOnClickListener(this);

	}

	/**
	 * 初始化popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * popwindow的显示
	 */
	public void show() {
		// popupWindow.showAtLocation(v, Gravity.CENTER
		// | Gravity.CENTER_HORIZONTAL, 0, 0);
		popupWindow.showAsDropDown(v, 0, 5);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏
	public void dismiss() {
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		dismiss();
		mCallBack.onClick(v.getId());

	}

	public interface OnMenuClick {
		void onClick(int id);
	}
}
