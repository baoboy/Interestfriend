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
import com.interestfriend.adapter.CategoryCircleAdapter;
import com.interestfriend.data.CategoryCircle;
import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetCatetoryCircleListTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

public class CategoryCircleActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView listView;
	private TextView txt_title;

	private List<CategoryCircle> lists = new ArrayList<CategoryCircle>();

	private CategoryCircleAdapter adapter;

	private Dialog dialog;

	private CategoryCircleList list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_circle);
		list = new CategoryCircleList();
		initView();
		setValue();

	}

	private void initView() {
		listView = (ListView) findViewById(R.id.Listview);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
	}

	private void setListener() {
		listView.setOnItemClickListener(this);
	}

	private void setValue() {
		txt_title.setText("圈子分类");
		adapter = new CategoryCircleAdapter(this, lists);
		listView.setAdapter(adapter);
		getCategory();
	}

	private void getCategory() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		GetCatetoryCircleListTask task = new GetCatetoryCircleListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (list.getCateLists().size() == 0) {
					ToastUtil.showToast("还没有圈子哦,赶快创建一个吧", Toast.LENGTH_SHORT);
					return;
				}
				lists.addAll(list.getCateLists());
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(list);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("category", lists.get(position).getCode());
		intent.setClass(this, SearchCirclsActivity.class);
		intent.putExtra("category_name", lists.get(position).getName());
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
