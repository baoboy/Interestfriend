package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.Hashtable;
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

import com.baidu.location.ad;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.interestfriend.R;
import com.interestfriend.activity.MainActivity;
import com.interestfriend.adapter.MyCircleAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.contentprovider.MyCirclesProvider;
import com.interestfriend.data.Circles;
import com.interestfriend.data.CirclesList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetCircleListTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;

@SuppressLint("NewApi")
public class MyCircleFragment extends Fragment implements OnItemClickListener {
	private AsyncQueryHandler asyncQuery;
	private List<MyCircles> lists = new ArrayList<MyCircles>();

	private MyCircleAdapter adapter;

	private ListView my_circls_listView;

	private CirclesList myCircleList;

	private Dialog dialog;

	private boolean hidden;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.my_circle_fragment_layouto, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		myCircleList = new CirclesList();
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
		initQuery();
	}

	private void initQuery() {
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
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
				refushCircleGroupChatHositiory();
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
		myIntentFilter.addAction(Constants.JOIN_CIRCLE);

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
			} else if (action.equals(Constants.JOIN_CIRCLE)) {
				MyCircles circle = (MyCircles) intent
						.getSerializableExtra("circle");
				lists.add(circle);
				adapter.notifyDataSetChanged();
			}
		}
	};

	public void refushCircleGroupChatHositiory() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		for (EMConversation conversation : conversations.values()) {

			if (!conversation.getIsGroup()) {
				continue;
			}
			EMMessage m = conversation.getLastMessage();
			setUnread(m.getTo(), conversation.getUnreadMsgCount());
			System.out.println("count::::::::::::"
					+ conversation.getUnreadMsgCount() + "    " + m.getTo());
		}
		adapter.notifyDataSetChanged();
	}

	private void setUnread(String groupId, int unread) {
		for (MyCircles c : lists) {
			if (c.getGroup_id().equals(groupId)) {
				c.setUnread(unread);
				break;
			}
		}
	}

	private void getCircleList() {
		GetCircleListTask task = new GetCircleListTask();
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

		MyApplation.setCircle_id(lists.get(arg2).getCircle_id());
		MyApplation.setCircle_group_id(lists.get(arg2).getGroup_id());
		MyApplation.setCircle_name(lists.get(arg2).getCircle_name());
		Intent intent = new Intent();
		intent.setClass(getActivity(), MainActivity.class);
		startActivity(intent);
	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refushCircleGroupChatHositiory();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			if (lists.size() > 0) {
				refushCircleGroupChatHositiory();
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			if (lists.size() > 0) {
				refushCircleGroupChatHositiory();
			}
		} else {
			// 相当于Fragment的onPause
		}
	}
}
