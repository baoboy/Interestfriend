package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.NearCirclesAdapter;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.NearCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetNearCirclesTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

public class NearCirclesActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView mlistView;
	private TextView txt_title;

	private Dialog dialog;

	private NearCirclesAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();

	private NearCircleList list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_circles);
		list = new NearCircleList();
		initView();
		setValue();
		getCircleList();
	}

	private void initView() {
		mlistView = (ListView) findViewById(R.id.listview);
		mlistView.setCacheColorHint(0);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
	}

	private void setListener() {
		mlistView.setOnItemClickListener(this);
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
		intent.putExtra("circle", listCircles.get(position));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
	}

	private void getCircleList() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		GetNearCirclesTask task = new GetNearCirclesTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("获取失败", Toast.LENGTH_SHORT);
					return;
				}
				listCircles.addAll(list.getListCircles());
				adapter.notifyDataSetChanged();
				if (listCircles.size() == 0) {
					ToastUtil.showToast("附近还没有圈子呢,赶快创建一个吧", Toast.LENGTH_SHORT);
				}

			};
		});
		task.execute(list);
	}

}
