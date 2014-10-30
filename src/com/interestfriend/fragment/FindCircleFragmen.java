package com.interestfriend.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.activity.CategoryCircleActivity;
import com.interestfriend.activity.NearCirclesActivity;
import com.interestfriend.data.NearCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetNearCirclesTask;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

@SuppressLint("NewApi")
public class FindCircleFragmen extends Fragment implements OnClickListener {

	private LinearLayout category_item;
	private LinearLayout near_item;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.near_circle_fragment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		category_item = (LinearLayout) getView().findViewById(
				R.id.category_item);
		near_item = (LinearLayout) getView().findViewById(R.id.near_item);
		setListener();
	}

	private void setListener() {
		category_item.setOnClickListener(this);
		near_item.setOnClickListener(this);
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.category_item:
			startActivity(new Intent(getActivity(),
					CategoryCircleActivity.class));
			Utils.leftOutRightIn(getActivity());

			break;
		case R.id.near_item:
			startActivity(new Intent(getActivity(), NearCirclesActivity.class));
			Utils.leftOutRightIn(getActivity());
			break;
		default:
			break;
		}
	}
}
