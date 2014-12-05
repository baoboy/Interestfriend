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
import com.interestfriend.adapter.MyCircleAdapter;
import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.SearchCirclesByCategoryTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.PullDownView;
import com.interestfriend.view.PullDownView.OnPullDownListener;

public class SearchCirclsActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnPullDownListener {
	private int category = 0;
	private ListView mlistView;
	private TextView txt_title;
	private ImageView back;

	private Dialog dialog;

	private CategoryCircleList lists;

	private MyCircleAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();
	private String category_name = "";

	private PullDownView mPullDownView;
	private int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_circls);
		category = getIntent().getIntExtra("category", 0);
		category_name = getIntent().getStringExtra("category_name");
		lists = new CategoryCircleList();
		lists.setCategory(category);
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
		adapter = new MyCircleAdapter(this, listCircles);
		mlistView.setAdapter(adapter);
		txt_title.setText(category_name);

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

		SearchCirclesByCategoryTask task = new SearchCirclesByCategoryTask(page);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				mPullDownView.notifyDidMore();
				if (dialog != null) {
					dialog.dismiss();
				}

				listCircles.addAll(lists.getListCircles());
				if (listCircles.size() == 0) {
					ToastUtil.showToast("还没有圈子哦,赶快创建一个吧", Toast.LENGTH_SHORT);
					return;
				}
				adapter.notifyDataSetChanged();
				if (lists.getListCircles().size() >= 19) {
					mPullDownView.setFooterVisible(true);
					page++;
				} else {
					mPullDownView.setFooterVisible(false);
				}
				lists.getListCircles().clear();
			}
		});
		task.executeParallel(lists);
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
