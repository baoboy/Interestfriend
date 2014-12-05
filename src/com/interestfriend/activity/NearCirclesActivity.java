package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.NearCirclesAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.NearCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetNearCirclesTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.PullDownView;
import com.interestfriend.view.PullDownView.OnPullDownListener;

public class NearCirclesActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnPullDownListener {

	private ListView mlistView;
	private TextView txt_title;
	private ImageView back;

	private Dialog dialog;

	private NearCirclesAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();

	private NearCircleList list;

	private PullDownView mPullDownView;
	private int page = 1;
	private double longitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_circles);
		list = new NearCircleList();
		longitude = MyApplation.getnLontitude();
		latitude = MyApplation.getnLatitude();
		initView();
		setValue();
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		getCircleList();
	}

	private void initView() {
		mPullDownView = (PullDownView) findViewById(R.id.PullDownlistView);
		mlistView = mPullDownView.getListView();
		mlistView.setVerticalScrollBarEnabled(false);
		mlistView.setCacheColorHint(0);
		mlistView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mPullDownView.setShowRefresh(false);
		mPullDownView.addFooterView();
		mPullDownView.setFooterVisible(false);
		mPullDownView.notifyDidMore();
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		mlistView.setOnItemClickListener(this);
		mPullDownView.setOnPullDownListener(this);
	}

	private void setValue() {
		adapter = new NearCirclesAdapter(this, listCircles);
		mlistView.setAdapter(adapter);
		txt_title.setText("附近圈子");

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("circle", listCircles.get(position - 1));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}

	private void getCircleList() {
		GetNearCirclesTask task = new GetNearCirclesTask(longitude, latitude,
				page);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			public void taskFinish(RetError result) {
				mPullDownView.notifyDidMore();
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				listCircles.addAll(list.getListCircles());
				if (list.getListCircles().size() >= 19) {
					mPullDownView.setFooterVisible(true);
					page++;
				} else {
					mPullDownView.setFooterVisible(false);
				}
				adapter.notifyDataSetChanged();
				if (listCircles.size() == 0) {
					ToastUtil.showToast("附近还没有圈子呢,赶快创建一个吧", Toast.LENGTH_SHORT);
				}
				list.getListCircles().clear();
			};
		});
		task.executeParallel(list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onMore() {
		getCircleList();
	}
}
