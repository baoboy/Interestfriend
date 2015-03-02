package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
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
import com.interestfriend.contentprovider.MyUserFriendProvider;
import com.interestfriend.data.ChatUser;
import com.interestfriend.data.ChatUserDao;

public class MyUserFriendActivity extends BaseActivity implements
		OnClickListener {
	private AsyncQueryHandler asyncQuery;

	private List<ChatUser> lists = new ArrayList<ChatUser>();

	private ImageView back;
	private TextView txt_title;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_user_friend);
		initView();
		setValue();
		initQuery();
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
			}
		});
	}

	private void setValue() {
		// adapter = new InviteMessageAdapter(this, dao.getLists());
		// mListView.setAdapter(adapter);
	}

	private void initQuery() {
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
				// adapter.notifyDataSetChanged();
				// refushCircleGroupChatHositiory();
				// myCircleList.setLocalCircles(lists);
				// getCircleList();
			} else {

			}
		}
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
