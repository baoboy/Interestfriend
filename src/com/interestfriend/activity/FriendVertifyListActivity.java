package com.interestfriend.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.adapter.InviteMessageAdapter1;
import com.interestfriend.data.InviteMessage;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.Utils;

public class FriendVertifyListActivity extends BaseActivity implements
		OnClickListener {
	private ImageView back;
	private TextView txt_title;
	private ListView mListView;

	// private Dialog dialog;

	// private InviteMessgeDao dao = new InviteMessgeDao();

	private InviteMessageAdapter1 adapter;
	private String user_chat_id = "";

	private EMConversation conversation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_vertify_list);
		user_chat_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance()
				.getConversation(user_chat_id);
		initView();
		setValue();
		registerBoradcastReceiver();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		mListView = (ListView) findViewById(R.id.listview);
		txt_title.setText("∫√”—…Í«Î");
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				EMMessage emmessage = conversation.getMessage(position);
				String reason = "";
				int user_friend_id = 0;
				String user_friend_name = "";
				String user_firend_avatar = "";
				String from_circle = "";
				String user_friend_chat_id = "";
				try {
					user_friend_id = Integer.valueOf(emmessage
							.getIntAttribute("user_friend_id"));
					reason = emmessage.getStringAttribute("reason");
					user_friend_name = emmessage
							.getStringAttribute("user_friend_name");
					user_firend_avatar = emmessage
							.getStringAttribute("user_firend_avatar");
					from_circle = emmessage.getStringAttribute("from_circle");
					user_friend_chat_id = emmessage
							.getStringAttribute("user_friend_chat_id");
				} catch (EaseMobException e) {
					e.printStackTrace();
					System.out.println("e:::::::::::::::;;;" + e.toString());
				}
				InviteMessage message = new InviteMessage();
				message.setFrom_circle(from_circle);
				message.setFrom_user_avatar(user_firend_avatar);
				message.setFrom_user_id(user_friend_id);
				message.setFrom_user_name(user_friend_name);
				message.setReason(reason);
				message.setFrom_user_chat_id(user_friend_chat_id);
				startActivity(new Intent(FriendVertifyListActivity.this,
						FriendVertifyActivity.class).putExtra("message",
						message).putExtra("msg_id", emmessage.getMsgId()));
				Utils.leftOutRightIn(FriendVertifyListActivity.this);
			}
		});
	}

	private void setValue() {
		// adapter = new InviteMessageAdapter(this, dao.getLists());
		adapter = new InviteMessageAdapter1(this, conversation);
		mListView.setAdapter(adapter);
	}

	// private void getInviteMessage() {
	// dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
	// dialog.show();
	// GetInviteMessageTask task = new GetInviteMessageTask();
	// task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
	// @Override
	// public void taskFinish(RetError result) {
	// if (dialog != null) {
	// dialog.dismiss();
	// }
	// adapter.notifyDataSetChanged();
	// }
	// });
	// task.execute(dao);
	// }

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

	/**
	 * ◊¢≤·∏√π„≤•
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.ADDED_USER_FRIEND);
		myIntentFilter.addAction(Constants.REDUED_USER_FRIEND);
		// ◊¢≤·π„≤•
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * ∂®“Âπ„≤•
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.ADDED_USER_FRIEND)
					|| action.equals(Constants.REDUED_USER_FRIEND)) {
				String msg_id = intent.getStringExtra("msg_id");
				System.out.println("msg::::::::::::::" + msg_id);
				conversation.removeMessage(msg_id);
				adapter.notifyDataSetChanged();
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	};

}
