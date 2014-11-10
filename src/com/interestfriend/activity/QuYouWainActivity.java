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
 * 系统提醒界面
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
		txt_title.setText("系统消息");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finishThisActivity();
			}
		});
	}

	private void showJoinCircleDialo() {
		dialog = new PromptDialog.Builder(this);
		dialog.setTitle("加入圈子申请");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage(txtBody.getMessage());
		dialog.setButton1("接受", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();

			}
		});
		dialog.setButton2("拒绝", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				refusePromptDialog();
			}
		}).show();
	}

	private void refusePromptDialog() {
		dialog = new PromptDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage("确定要拒绝吗？");
		dialog.setButton1("确定", new PromptDialog.OnClickListener() {
			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				conversation.removeMessage(lastMessage.getMsgId());
				conversation.resetUnsetMsgCount();
				finishThisActivity();
			}
		});
		dialog.setButton2("取消", new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				showJoinCircleDialo();
			}
		}).show();
	}
}
