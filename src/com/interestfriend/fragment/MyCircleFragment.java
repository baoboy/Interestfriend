package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
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

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.activity.MainActivity;
import com.interestfriend.adapter.MyCircleAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.contentprovider.MyCirclesProvider;
import com.interestfriend.data.AbstractData.Status;
import com.interestfriend.data.CirclesList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetCircleListTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

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
				MyCirclesProvider.MyCirclesColumns.GROUP_ID,
				MyCirclesProvider.MyCirclesColumns.CREATOR_ID,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_CATEGORY,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_CREATE_TIME,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_CREATOR_NAME,
				MyCirclesProvider.MyCirclesColumns.CIRCLE_MEMBER_NUM }; // 查询的列
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
					int creator_id = cursor.getInt(5);
					int circle_membet_num = cursor.getInt(9);
					MyCircles circles = new MyCircles();
					circles.setCircle_id(circle_id);
					circles.setCircle_description(circle_description);
					circles.setCircle_name(circle_name);
					circles.setCircle_logo(circle_logo);
					circles.setGroup_id(group_id);
					circles.setCreator_id(creator_id);
					circles.setCircle_member_num(circle_membet_num);
					lists.add(circles);
					cursor.moveToNext();
				}
				adapter.notifyDataSetChanged();
				refushCircleGroupChatHositiory();
				myCircleList.setLocalCircles(lists);
				getCircleList();
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
		myIntentFilter.addAction(Constants.RECEIVE_JOIN_CIRCLE);
		myIntentFilter.addAction(Constants.QUIT_CIRCLE);
		myIntentFilter.addAction(Constants.DISSOLVE_CIRCLE);
		myIntentFilter.addAction(Constants.UPDATE_CIRCLE_MEMBER_NUM);
		myIntentFilter.addAction(Constants.REMOVE_CIRCLE_MEMBER_COUNT);

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
				int circle_creator = intent.getIntExtra("circle_creator", 0);
				MyCircles circle = new MyCircles();
				circle.setCircle_description(circle_description);
				circle.setCircle_name(circle_name);
				circle.setCircle_logo(circle_logo);
				circle.setCreator_id(circle_creator);
				circle.setCircle_id(circle_id);
				lists.add(circle);
				adapter.notifyDataSetChanged();
			} else if (action.equals(Constants.QUIT_CIRCLE)) {
				int circle_id = intent.getIntExtra("circle_id", 0);
				for (MyCircles circle : lists) {
					if (circle.getCircle_id() == circle_id) {
						lists.remove(circle);
						adapter.notifyDataSetChanged();
						return;
					}
				}
			} else if (action.equals(Constants.RECEIVE_JOIN_CIRCLE)) {
				getCircleList();
			} else if (action.equals(Constants.DISSOLVE_CIRCLE)) {
				int circle_id = intent.getIntExtra("circle_id", 0);
				for (MyCircles c : lists) {
					if (c.getCircle_id() == circle_id) {
						lists.remove(c);
						adapter.notifyDataSetChanged();
						break;
					}
				}
			} else if (action.equals(Constants.UPDATE_CIRCLE_MEMBER_NUM)) {
				int circle_id = intent.getIntExtra("circle_id", 0);
				int num = intent.getIntExtra("num", 0);
				for (MyCircles c : lists) {
					if (c.getCircle_id() == circle_id) {
						c.setCircle_member_num(num);
						c.setStatus(Status.UPDATE);
						c.write(DBUtils.getDBsa(2));
						adapter.notifyDataSetChanged();
						break;
					}
				}
			} else if (action.equals(Constants.REMOVE_CIRCLE_MEMBER_COUNT)) {
				int circle_id = intent.getIntExtra("circle_id", 0);
				for (MyCircles c : lists) {
					if (c.getCircle_id() == circle_id) {
						c.setCircle_member_num(c.getCircle_member_num() - 1);
						c.setStatus(Status.UPDATE);
						c.write(DBUtils.getDBsa(2));
						adapter.notifyDataSetChanged();
						break;
					}
				}
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
			int growth_unread = 0;
			int self_publish = 0;
			Iterator<EMMessage> messageLists = conversation.getAllMessages()
					.iterator();
			while (messageLists.hasNext()) {
				EMMessage message = messageLists.next();
				if (message.getFrom().equals(Constants.GROWTH_USER_ID)) {
					int publicsher_id = 0;
					try {
						publicsher_id = Integer.valueOf(message
								.getStringAttribute("publisher_id"));
					} catch (EaseMobException e) {
						e.printStackTrace();
					}
					if (publicsher_id == SharedUtils.getIntUid()) {
						self_publish++;
						Utils.print("pulish:::::::::::===");
					} else {
						growth_unread++;
						Utils.print("pulish:::::::::::===---");

					}
					message.setUnread(true);
					conversation.removeMessage(message.getMsgId());
				}
			}
			setUnread(conversation.getUserName(),
					conversation.getUnreadMsgCount(), growth_unread,
					self_publish);
		}
		adapter.notifyDataSetChanged();
	}

	private void setUnread(String groupId, int unread, int growth_unread,
			int self_unread) {
		Utils.print("pulish:::::::::::===+++++" + unread + "      "
				+ self_unread + "   " + growth_unread);
		for (MyCircles c : lists) {
			if (c.getGroup_id().equals(groupId)) {
				c.setUnread(unread - (self_unread + growth_unread));
				c.setGrowth_unread(growth_unread);
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
		task.executeParallel(myCircleList);
	}

	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MyCircles circle = lists.get(arg2);
		MyApplation.setCircle_id(circle.getCircle_id());
		MyApplation.setCircle_group_id(circle.getGroup_id());
		MyApplation.setCircle_name(circle.getCircle_name());
		Intent intent = new Intent();
		intent.putExtra("unread", circle.getUnread());
		intent.putExtra("growth_unread", circle.getGrowth_unread());
		intent.setClass(getActivity(), MainActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(getActivity());
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(circle.getGroup_id());
		// 把此会话的未读数置为0
		conversation.resetUnsetMsgCount();
	};

	// @Override
	// public void onHiddenChanged(boolean hidden) {
	// super.onHiddenChanged(hidden);
	// this.hidden = hidden;
	// if (!hidden) {
	// refushCircleGroupChatHositiory();
	// }
	// }

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			if (lists.size() > 0) {
				refushCircleGroupChatHositiory();
			}
		}
	}

	// @Override
	// public void setUserVisibleHint(boolean isVisibleToUser) {
	// super.setUserVisibleHint(isVisibleToUser);
	// if (isVisibleToUser) {
	// // 相当于Fragment的onResume
	// if (lists.size() > 0) {
	// refushCircleGroupChatHositiory();
	// }
	// } else {
	// // 相当于Fragment的onPause
	// }
	// }
}
