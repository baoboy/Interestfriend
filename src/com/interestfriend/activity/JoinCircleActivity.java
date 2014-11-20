package com.interestfriend.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.ReceiveJoinCircleTask;
import com.interestfriend.task.RefuseJoinCircleTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

import fynn.app.PromptDialog;

/**
 * 系统提醒界面
 * 
 * @author songbinbin
 * 
 */
public class JoinCircleActivity extends BaseActivity {
	private ImageView back;
	private TextView txt_title;
	private PromptDialog.Builder dialog;
	private String user_id = "";
	private EMConversation conversation;
	private EMMessage lastMessage;
	private TextMessageBody txtBody;

	private String join_circle_id = "";
	private String request_join_circle_user_id = "";
	private String group_id = "";
	private String huanxin_userName = "";
	private String join_circle_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qu_you_wain);
		user_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance().getConversation(user_id);
		lastMessage = conversation.getLastMessage();
		txtBody = (TextMessageBody) lastMessage.getBody();
		try {
			join_circle_id = lastMessage.getStringAttribute("join_circle_id");
			request_join_circle_user_id = lastMessage
					.getStringAttribute("request_join_circle_user_id");
			group_id = lastMessage.getStringAttribute("group_id");
			huanxin_userName = lastMessage
					.getStringAttribute("huanxin_userName");
			join_circle_name = lastMessage
					.getStringAttribute("join_circle_name");

		} catch (EaseMobException e) {
			e.printStackTrace();
		}
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

	private void joinCircle() {
		CircleMember member = new CircleMember();
		member.setCircle_id(Integer.valueOf(join_circle_id));
		member.setGroup_id(group_id);
		member.setUser_id(Integer.valueOf(request_join_circle_user_id));
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		ReceiveJoinCircleTask task = new ReceiveJoinCircleTask(
				huanxin_userName, join_circle_name);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				conversation.removeMessage(lastMessage.getMsgId());
				conversation.resetUnsetMsgCount();
				ToastUtil.showToast("操作成功", Toast.LENGTH_SHORT);
				// Intent intent = new Intent(
				// Constants.RECEIVE_JOIN_CIRCLE_REQUEST);
				// intent.putExtra("circle_id",
				// Integer.valueOf(join_circle_id));
				// sendBroadcast(intent);
				finishThisActivity();
			}
		});
		task.executeParallel(member);
	}

	private void refuseJoinCircle() {
		CircleMember member = new CircleMember();
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		RefuseJoinCircleTask task = new RefuseJoinCircleTask(huanxin_userName,
				join_circle_name);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				conversation.removeMessage(lastMessage.getMsgId());
				conversation.resetUnsetMsgCount();
				ToastUtil.showToast("操作成功", Toast.LENGTH_SHORT);
				finishThisActivity();
			}
		});
		task.executeParallel(member);
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
				joinCircle();
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
				refuseJoinCircle();
				// conversation.removeMessage(lastMessage.getMsgId());
				// conversation.resetUnsetMsgCount();
				// finishThisActivity();
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

	@Override
	public void onBackPressed() {
		finishThisActivity();
	}
}
