package com.interestfriend.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.interestfriend.R;
import com.interestfriend.utils.Constants;

import fynn.app.PromptDialog;

/**
 * ϵͳ���ѽ���
 * 
 * @author songbinbin
 * 
 */
public class QuYouWainActivity extends BaseActivity {
	private ImageView back;
	private TextView txt_title;
	private PromptDialog.Builder dialog;
	private String user_id = "";
	private EMConversation conversation;
	private EMMessage lastMessage;
	private TextMessageBody txtBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qu_you_wain);
		user_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance().getConversation(user_id);
		lastMessage = conversation.getLastMessage();
		txtBody = (TextMessageBody) lastMessage.getBody();
		initView();
		if (Constants.JOIN_CIRCLE_USER_ID.equals(user_id)) {
			showJoinCircleDialo();
		}
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("ϵͳ��Ϣ");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finishThisActivity();
			}
		});
	}

	private void showJoinCircleDialo() {
		dialog = new PromptDialog.Builder(this);
		dialog.setTitle("����Ȧ������");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage(txtBody.getMessage());
		dialog.setButton1("����", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();

			}
		});
		dialog.setButton2("�ܾ�", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				refusePromptDialog();
			}
		}).show();
	}

	private void refusePromptDialog() {
		dialog = new PromptDialog.Builder(this);
		dialog.setTitle("��ʾ");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage("ȷ��Ҫ�ܾ���");
		dialog.setButton1("ȷ��", new PromptDialog.OnClickListener() {
			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				conversation.removeMessage(lastMessage.getMsgId());
				conversation.resetUnsetMsgCount();
				finishThisActivity();
			}
		});
		dialog.setButton2("ȡ��", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				showJoinCircleDialo();
			}
		}).show();
	}
}
