package com.interestfriend.activity;

import java.util.List;

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
import com.interestfriend.data.Circles;
import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.Constants;

import fynn.app.PromptDialog;

/**
 * 解散圈子提醒界面
 * 
 * @author songbinbin
 * 
 */
public class DissolveCircleActivity extends BaseActivity {
	private ImageView back;
	private TextView txt_title;
	private PromptDialog.Builder dialog;
	private String user_id = "";
	private EMConversation conversation;
	private EMMessage lastMessage;
	private TextMessageBody txtBody;
	private int circle_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dissolve_circle);
		user_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance().getConversation(user_id);
		lastMessage = conversation.getLastMessage();
		txtBody = (TextMessageBody) lastMessage.getBody();

		try {
			circle_id = Integer.valueOf(lastMessage
					.getStringAttribute("circle_id"));
			Circles circle = new Circles();
			circle.setCircle_id(circle_id);
			circle.setStatus(Status.DEL);
			circle.write(DBUtils.getDBsa(2));
			Intent intent = new Intent(Constants.DISSOLVE_CIRCLE);
			intent.putExtra("circle_id", circle_id);
			sendBroadcast(intent);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		initView();
		dissolveCircleDialo();
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

	private void dissolveCircleDialo() {
		dialog = new PromptDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage(txtBody.getMessage());
		dialog.setButton1("确定", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();

				finishThisActivity();
			}
		}).show();
	}

	@Override
	protected void onDestroy() {
		// conversation.removeMessage(lastMessage.getMsgId());
		// conversation.resetUnsetMsgCount();
		List<EMMessage> messages = conversation.getAllMessages();
		for (int i = messages.size() - 1; i >= 0; i--) {
			conversation.removeMessage(messages.get(i).getMsgId());
		}
		conversation.resetUnsetMsgCount();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		finishThisActivity();
	}
}
