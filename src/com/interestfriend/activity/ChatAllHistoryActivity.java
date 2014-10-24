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
import com.interestfriend.R;
import com.interestfriend.adapter.ChatAllHistoryAdapter;

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
	 * ˢ��ҳ��
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
		txt_title.setText("�����¼");
		adapter = new ChatAllHistoryAdapter(this, 1,
				loadConversationsWithRecentChat());
		listView.setAdapter(adapter);
	}

	/**
	 * ��ȡ���лỰ
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// ��ȡ���лỰ������İ����
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> conversationList = new ArrayList<EMConversation>();
		// ���˵�messages seizeΪ0��conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0) {
				if (conversation.getIsGroup()) {
					continue;
				}
				conversationList.add(conversation);
			}
		}
		// ����
		sortConversationByLastChatTime(conversationList);
		return conversationList;
	}

	/**
	 * �������һ����Ϣ��ʱ������
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

		// ��������ҳ��
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("userId", username);
		startActivity(intent);

	}
}