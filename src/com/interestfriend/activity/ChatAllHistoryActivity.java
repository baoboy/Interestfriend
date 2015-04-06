package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
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
import com.interestfriend.adapter.ChatAllHistoryAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

public class ChatAllHistoryActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView listView;
	private TextView txt_title;
	private ImageView back;

	private ChatAllHistoryAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_all_history);
		initView();
		setValue();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();

	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		adapter = new ChatAllHistoryAdapter(this, R.layout.row_chat_history,
				loadConversationsWithRecentChat());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		txt_title = (TextView) findViewById(R.id.title_txt);
		listView = (ListView) findViewById(R.id.list);
		back = (ImageView) findViewById(R.id.back);
		setListener();
	}

	private void setListener() {
		listView.setOnItemClickListener(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishThisActivity();
			}
		});
	}

	private void setValue() {
		txt_title.setText("聊天记录");
		adapter = new ChatAllHistoryAdapter(this, 1,
				loadConversationsWithRecentChat());
		listView.setAdapter(adapter);
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> conversationList = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0) {
				if (conversation.getIsGroup()) {
					continue;
				}
				conversationList.add(conversation);
			}
		}
		// 排序
		sortConversationByLastChatTime(conversationList);
		return conversationList;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		EMConversation conversation = adapter.getItem(position);
		String username = conversation.getUserName();
		String circle_name = "";
		Intent intent = null;
		if (Constants.JOIN_CIRCLE_USER_ID.equals(username)) {
			intent = new Intent(this, JoinCircleActivity.class);
		} else if (Constants.RECEIVE_JOIN_CIRCLE_USER_ID.equals(username)) {
			intent = new Intent(this, ReceiveJoinCircleActivity.class);
		} else if (Constants.REFUSE_JON_CIRCLE_USER_ID.equals(username)) {
			intent = new Intent(this, RefuseJoinCircleActivity.class);
		} else if (Constants.DISSOLVE_CIRCLE_USER_ID.equals(username)) {
			intent = new Intent(this, DissolveCircleActivity.class);
		} else if (Constants.PRAISE_USER_ID.equals(username)) {
			intent = new Intent(this, PraiseAndCommentActivity.class);
		} else if (Constants.KICK_OUT_USER_ID.equals(username)) {
			intent = new Intent(this, KickOutActivity.class);
		} else if (Constants.ADD_USER_FRIEND_INVITE.equals(username)) {
			intent = new Intent(this, FriendVertifyListActivity.class);
		} else if (Constants.XINQING_PRAISE_AND_COMMENT_USER_ID
				.equals(username)) {
			intent = new Intent(this, RefushXinQingCommentActivity.class);
		} else {
			intent = new Intent(this, ChatActivity.class);
			EMMessage message = conversation.getLastMessage();
			String user_name = "";
			String user_avatar = "";
			int user_id;
			try {
				user_id = message.getIntAttribute("user_id");
				circle_name = message.getStringAttribute("circle_name");
				if (user_id == SharedUtils.getIntUid()) {
					user_name = message.getStringAttribute("to_user_name");
					user_avatar = message.getStringAttribute("to_user_avatar");
					user_id = message.getIntAttribute("to_user_id");
				} else {
					user_name = message.getStringAttribute("from_user_name");
					user_avatar = message
							.getStringAttribute("from_user_avatar");
				}
				intent.putExtra("user_name", user_name);
				intent.putExtra("user_avatar", user_avatar);
				intent.putExtra("user_id", user_id);

			} catch (EaseMobException e) {
				e.printStackTrace();
				try {
					circle_name = message.getStringAttribute("circle_name");
					user_name = message.getStringAttribute("user_name");
					user_avatar = message.getStringAttribute("user_avatar");
					user_id = message.getIntAttribute("user_id");
					intent.putExtra("user_name", user_name);
					intent.putExtra("user_avatar", user_avatar);
					intent.putExtra("user_id", user_id);
				} catch (EaseMobException e1) {
					e1.printStackTrace();
				}
			}
		}
		MyApplation.setCircle_name(circle_name);
		intent.putExtra("userId", username);
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}
}
