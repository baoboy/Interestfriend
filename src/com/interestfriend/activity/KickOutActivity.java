package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.data.AbstractData.Status;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Circles;
import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.Constants;

import fynn.app.PromptDialog;

/**
 * 系统提醒界面 踢出圈子
 * 
 * @author songbinbin
 * 
 */
public class KickOutActivity extends BaseActivity {
	private ImageView back;
	private TextView txt_title;
	private PromptDialog.Builder dialog;
	private String user_id = "";
	private EMConversation conversation;
	private EMMessage lastMessage;
	private TextMessageBody txtBody;

	private int circle_id;
	private int kickout_user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qu_you_wain);
		user_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance().getConversation(user_id);
		lastMessage = conversation.getLastMessage();
		txtBody = (TextMessageBody) lastMessage.getBody();
		try {
			kickout_user_id = Integer.valueOf(lastMessage
					.getStringAttribute("kickout_user_id"));
			circle_id = Integer.valueOf(lastMessage
					.getStringAttribute("circle_id"));
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		initView();
		kickOutPrompt();
		Intent intent = new Intent(Constants.DISSOLVE_CIRCLE);
		intent.putExtra("circle_id", circle_id);
		sendBroadcast(intent);
		delDB();
	}

	private void delDB() {
		new Thread() {
			public void run() {
				Circles circle = new Circles();
				circle.setCircle_id(circle_id);
				circle.setStatus(Status.DEL);
				circle.write(DBUtils.getDBsa(2));
				CircleMember member = new CircleMember();
				member.setCircle_id(circle_id);
				member.setUser_id(kickout_user_id);
				member.setStatus(Status.DEL);
				member.write(DBUtils.getDBsa(2));
			}
		}.start();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("系统消息");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finishThisActivity();
			}
		});
	}

	private void kickOutPrompt() {
		dialog = new PromptDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage(txtBody.getMessage());
		dialog.setButton1("确定", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				conversation.removeMessage(lastMessage.getMsgId());
				conversation.resetUnsetMsgCount();
				finishThisActivity();
			}
		}).show();
	}

	@Override
	public void onBackPressed() {
		finishThisActivity();
	}
}
