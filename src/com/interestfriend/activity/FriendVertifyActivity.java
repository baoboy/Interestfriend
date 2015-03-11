package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.data.ChatUser;
import com.interestfriend.data.ChatUserDao;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.InviteMessage;
import com.interestfriend.data.InviteMessgeDao;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.AddFriendTask;
import com.interestfriend.task.RefuseInvitationTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.RoundAngleImageView;

public class FriendVertifyActivity extends BaseActivity implements
		OnClickListener {
	private ImageView back;
	private TextView txt_title;
	private RoundAngleImageView img_avatar;
	private TextView txt_name;
	private TextView txt_desc;
	private Button btn_jujue;
	private Button btn_tongyi;
	private RelativeLayout info_layout;
	private TextView txt_laiyuan;

	private String user_id = "";
	private EMConversation conversation;
	private EMMessage lastMessage;

	private InviteMessage message;

	private Dialog dialog;

	private String reason = "";
	private int user_friend_id;
	private String user_friend_name = "";
	private String user_firend_avatar = "";
	private String from_circle = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_vertify);
		message = (InviteMessage) getIntent().getSerializableExtra("message");
		// getLastMessage();
		initView();
		setValue();
	}

	private void getLastMessage() {
		user_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance().getConversation(user_id);
		lastMessage = conversation.getLastMessage();
		try {
			user_friend_id = Integer.valueOf(lastMessage
					.getIntAttribute("user_friend_id"));
			reason = lastMessage.getStringAttribute("reason");
			user_friend_name = lastMessage
					.getStringAttribute("user_friend_name");
			user_firend_avatar = lastMessage
					.getStringAttribute("user_firend_avatar");
			from_circle = lastMessage.getStringAttribute("from_circle");
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		message = new InviteMessage();
		message.setFrom_circle(from_circle);
		message.setFrom_user_avatar(user_firend_avatar);
		message.setFrom_user_id(user_friend_id);
		message.setFrom_user_name(user_friend_name);
		message.setReason(reason);
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("好友申请");
		img_avatar = (RoundAngleImageView) findViewById(R.id.img_avatar);
		txt_desc = (TextView) findViewById(R.id.txt_user_desc);
		txt_name = (TextView) findViewById(R.id.txt_user_name);
		btn_jujue = (Button) findViewById(R.id.btn_jujue);
		btn_tongyi = (Button) findViewById(R.id.btn_tongyi);
		info_layout = (RelativeLayout) findViewById(R.id.info_layout);
		txt_laiyuan = (TextView) findViewById(R.id.txt_laiyuan);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btn_jujue.setOnClickListener(this);
		btn_tongyi.setOnClickListener(this);
		info_layout.setOnClickListener(this);
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(message.getFrom_user_avatar(),
				img_avatar, R.drawable.default_avatar);
		txt_desc.setText(message.getReason());
		txt_name.setText(message.getFrom_user_name());
		txt_laiyuan.setText(message.getFrom_circle());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.info_layout:
			CircleMember member = new CircleMember();
			member.setUser_id(message.getFrom_user_id());
			member.read(DBUtils.getDBsa(1));
			member.setUser_avatar(message.getFrom_user_avatar());
			Intent intent = new Intent();
			intent.putExtra("circle_member", member);
			intent.setClass(this, CircleMemberActivity.class);
			startActivity(intent);
			Utils.leftOutRightIn(this);
			break;
		case R.id.btn_tongyi:
			acceptInvitation();
			break;
		case R.id.btn_jujue:
			refuseInvitation();
			break;
		default:
			break;
		}
	}

	/**
	 * 同意好友请求
	 * 
	 */
	private void acceptInvitation() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		add();
	}

	private void add() {
		AddFriendTask task = new AddFriendTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("操作成功", Toast.LENGTH_SHORT);
				InviteMessgeDao inviteMessgeDao = new InviteMessgeDao();
				inviteMessgeDao.deleteMessage(message.getFrom_user_chat_id(),
						DBUtils.getDBsa(2));
				ChatUser user = new ChatUser();
				user.setUser_avatar(message.getFrom_user_avatar());
				user.setUser_chat_id(message.getFrom_user_chat_id());
				user.setUser_id(message.getFrom_user_id());
				user.setUser_name(message.getFrom_user_name());
				user.setUser_friend_circle(message.getFrom_circle());
				ChatUserDao dao = new ChatUserDao();
				dao.saveContact(user, DBUtils.getDBsa(2));
				sendBroadcast(new Intent(Constants.ADDED_USER_FRIEND).putExtra(
						"msg_id", getIntent().getStringExtra("msg_id")));
				finishThisActivity();
			};
		});
		task.execute(message);
	}

	private void refuse() {
		RefuseInvitationTask task = new RefuseInvitationTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("操作成功", Toast.LENGTH_SHORT);
				sendBroadcast(new Intent(Constants.REDUED_USER_FRIEND)
						.putExtra("msg_id", getIntent()
								.getStringExtra("msg_id")));
				finishThisActivity();
			};
		});
		task.execute(message);
	}

	private void refuseInvitation() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		refuse();
	}

}
