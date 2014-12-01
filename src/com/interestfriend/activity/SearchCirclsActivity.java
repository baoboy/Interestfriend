package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
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

public class SearchCirclsActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	private int category = 0;

	private ListView mlistView;
	private TextView txt_title;
	private ImageView back;

	private Dialog dialog;

	private CategoryCircleList lists;

	private MyCircleAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();
	private String category_name = "";

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
		getCircleList();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		mlistView = (ListView) findViewById(R.id.listview);
		mlistView.setCacheColorHint(0);
		txt_title = (TextView) findViewById(R.id.title_txt);

		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		mlistView.setOnItemClickListener(this);
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
		intent.putExtra("circle", listCircles.get(position));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}

	private void getCircleList() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		SearchCirclesByCategoryTask task = new SearchCirclesByCategoryTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (lists.getListCircles().size() == 0) {
					ToastUtil.showToast("还没有圈子哦,赶快创建一个吧", Toast.LENGTH_SHORT);
					return;
				}
				listCircles.addAll(lists.getListCircles());
				adapter.notifyDataSetChanged();
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
}
