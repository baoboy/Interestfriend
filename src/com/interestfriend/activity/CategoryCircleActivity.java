package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.interestfriend.R;
import com.interestfriend.adapter.CategoryCircleAdapter;
import com.interestfriend.data.CategoryCircle;
import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetCatetoryCircleListTask;
import com.interestfriend.utils.DialogUtil;

public class CategoryCircleActivity extends Activity implements
		OnItemClickListener {
	private ListView listView;

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
		setListener();
	}

	private void setListener() {
		listView.setOnItemClickListener(this);
	}

	private void setValue() {
		adapter = new CategoryCircleAdapter(this, lists);
		listView.setAdapter(adapter);
		getCategory();
	}

	private void getCategory() {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		GetCatetoryCircleListTask task = new GetCatetoryCircleListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				lists.addAll(list.getCateLists());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(list);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
