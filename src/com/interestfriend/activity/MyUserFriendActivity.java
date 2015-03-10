package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.adapter.MyUserFriendAdapter;
import com.interestfriend.contentprovider.MyUserFriendProvider;
import com.interestfriend.data.ChatUser;
import com.interestfriend.data.ChatUserDao;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.UserFriendList;
import com.interestfriend.data.AbstractData.Status;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetUserFriendTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.Utils;

public class MyUserFriendActivity extends BaseActivity implements
		OnClickListener {
	private AsyncQueryHandler asyncQuery;

	private List<ChatUser> lists = new ArrayList<ChatUser>();

	private MyUserFriendAdapter adapter;

	private ImageView back;
	private TextView txt_title;
	private ListView mListView;

	private UserFriendList list = new UserFriendList();

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_user_friend);
		initView();
		setValue();
		initQuery();
		registerBoradcastReceiver();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		mListView = (ListView) findViewById(R.id.listview);
		txt_title.setText("我的好友");
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				CircleMember member = new CircleMember();
				member.setUser_id(lists.get(position).getUser_id());
				member.read(DBUtils.getDBsa(1));
				Intent intent = new Intent();
				intent.putExtra("circle_member", member);
				intent.putExtra("from", -1);
				intent.setClass(MyUserFriendActivity.this,
						CircleMemberActivity.class);
				startActivity(intent);
				Utils.leftOutRightIn(MyUserFriendActivity.this);
			}
		});
	}

	private void setValue() {
		adapter = new MyUserFriendAdapter(this, lists);
		mListView.setAdapter(adapter);

	}

	private void initQuery() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		String[] projection = { ChatUserDao.COLUMN_USER_AVATAR,
				ChatUserDao.COLUMN_USER_CHAT_ID,
				ChatUserDao.COLUMN_USER_FROM_CIRCLE,
				ChatUserDao.COLUMN_USER_ID, ChatUserDao.COLUMN_USER_NAME, }; // 查询的列
		asyncQuery.startQuery(0, null,
				MyUserFriendProvider.MyUserFriendlumns.CONTENT_URI, projection,
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
				dialog.dismiss();
				return;
			}
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					String user_avatar = cursor.getString(0);
					String user_chat_id = cursor.getString(1);
					String user_from_circle = cursor.getString(2);
					int user_id = cursor.getInt(3);
					String user_name = cursor.getString(4);
					ChatUser user = new ChatUser();
					user.setUser_avatar(user_avatar);
					user.setUser_chat_id(user_chat_id);
					user.setUser_friend_circle(user_from_circle);
					user.setUser_id(user_id); 
					user.setUser_name(user_name);
					lists.add(user);
					cursor.moveToNext();
				}
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			} else {
				getUserFriend();
			}
		}
	}

	private void getUserFriend() {
		GetUserFriendTask task = new GetUserFriendTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				lists.clear();
				lists.addAll(list.getLists());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(list);
	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.DEL_USER_FRIEND);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 定义广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.DEL_USER_FRIEND)) {
				int user_id = intent.getIntExtra("user_id", 0);
				del(user_id);
				ChatUserDao dao = new ChatUserDao();
				dao.deleteContact(user_id);
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	};

	private void del(int user_id) {
		for (ChatUser user : lists) {
			if (user.getUser_id() == user_id) {
				lists.remove(user);
				break;
			}
		}
		adapter.notifyDataSetChanged();
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
