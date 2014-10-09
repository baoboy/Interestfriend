package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.interestfriend.R;
import com.interestfriend.activity.MainActivity;
import com.interestfriend.adapter.MyCircleAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.contentprovider.MyCirclesProvider;
import com.interestfriend.data.MyCircleList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetMyCircleListTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;

@SuppressLint("NewApi")
public class MyCircleFragment extends Fragment implements OnItemClickListener {
	private AsyncQueryHandler asyncQuery;
	private List<MyCircles> lists = new ArrayList<MyCircles>();

	private MyCircleAdapter adapter;

	private ListView my_circls_listView;

	private MyCircleList myCircleList;

	private Dialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.my_circle_fragment_layouto, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		myCircleList = new MyCircleList();
		initView();
		setValue();
		registerBoradcastReceiver();
	}

	private void initView() {
		my_circls_listView = (ListView) getView().findViewById(
				R.id.my_circles_listview);
		my_circls_listView.setCacheColorHint(0);
		setListener();
	}

	private void setListener() {
		my_circls_listView.setOnItemClickListener(this);
	}

	private void setValue() {
		adapter = new MyCircleAdapter(getActivity(), lists);
		my_circls_listView.setAdapter(adapter);
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		initQuery();
	}

	private void initQuery() {
		String[] projection = { MyCirclesProvider.MyCirclesColumns.CIRCLE_ID,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_NAME,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_DESCRIPTION,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_LOGO,
				MyCirclesProvider.MyCirclesColumns.GROUP_ID, }; // 查询的列
		asyncQuery.startQuery(0, null,
				MyCirclesProvider.MyCirclesColumns.CONTENT_URI, projection,
				null, null, null);
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie,
				final Cursor cursor) {
			if (cursor == null) {
				return;
			}
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					int circle_id = cursor.getInt(0);
					String circle_name = cursor.getString(1);
					String circle_description = cursor.getString(2);
					String circle_logo = cursor.getString(3);
					String group_id = cursor.getString(4);
					MyCircles circles = new MyCircles();
					circles.setCircle_id(circle_id);
					circles.setCircle_description(circle_description);
					circles.setCircle_name(circle_name);
					circles.setCircle_logo(circle_logo);
					circles.setGroup_id(group_id);
					lists.add(circles);
					cursor.moveToNext();
				}
				adapter.notifyDataSetChanged();
			} else {
				dialog = DialogUtil.createLoadingDialog(getActivity(), "请稍候");
				dialog.show();
				getCircleList();
			}
		}
	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.CREATE_CIRCLS);
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 定义广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.CREATE_CIRCLS)) {
				String circle_name = intent.getStringExtra("circle_name");
				String circle_description = intent
						.getStringExtra("circle_description");
				String circle_logo = intent.getStringExtra("circle_logo");
				int circle_id = intent.getIntExtra("circle_id", 0);
				MyCircles circle = new MyCircles();
				circle.setCircle_description(circle_description);
				circle.setCircle_name(circle_name);
				circle.setCircle_logo(circle_logo);
				circle.setCircle_id(circle_id);
				lists.add(circle);
				adapter.notifyDataSetChanged();
			}
		}
	};

	private void getCircleList() {
		GetMyCircleListTask task = new GetMyCircleListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				lists.addAll(myCircleList.getListCircles());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(myCircleList);
	}

	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// startActivity(new Intent(getActivity(), MainActivity.class));
		// Intent intent = new Intent();
		// intent.putExtra("groupId", lists.get(arg2).getGroup_id());
		// intent.putExtra("chatType", 2);
		// intent.setClass(getActivity(), ChatActivity.class);
		// startActivity(intent);
		MyApplation.setCircle_id(lists.get(arg2).getCircle_id());
		Intent intent = new Intent();
		intent.setClass(getActivity(), MainActivity.class);
		startActivity(intent);
	};
}
