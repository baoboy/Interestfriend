package com.interestfriend.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.InviteMessage;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.AddUserFriendInviteTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class AddFriendActivity extends BaseActivity implements OnClickListener {
	private ImageView back;
	private TextView txt_title;
	private RoundAngleImageView img_avatar;
	private TextView txt_name;
	private EditText edit_content;
	private Button btn_add;

	private String user_name = "";
	private String user_avatar = "";
	private String add_user_chat_id = "";
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		user_avatar = getIntent().getStringExtra("add_user_avatar");
		user_name = getIntent().getStringExtra("add_user_name");
		add_user_chat_id = getIntent().getStringExtra("add_user_chat_id");
		initView();
		setValue();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("加好友");
		img_avatar = (RoundAngleImageView) findViewById(R.id.img_avatar);
		txt_name = (TextView) findViewById(R.id.txt_user_name);
		edit_content = (EditText) findViewById(R.id.edit_content);
		btn_add = (Button) findViewById(R.id.btn_add);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btn_add.setOnClickListener(this);
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(user_avatar, img_avatar,
				R.drawable.default_avatar);
		txt_name.setText(user_name);
	}

	private void addFriend() {
		AddUserFriendInviteTask task = new AddUserFriendInviteTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
					if (result != RetError.NONE) {
						return;
					}
					ToastUtil.showToast("发送请求成功,等待对方验证", Toast.LENGTH_SHORT);
					finishThisActivity();
				}
			};
		});
		InviteMessage message = new InviteMessage();
		message.setReason(edit_content.getText().toString());
		message.setFrom_user_chat_id(add_user_chat_id);
		task.executeParallel(message);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btn_add:
			dialog = DialogUtil.createLoadingDialog(this, "请稍候");
			dialog.show();
			addFriend();
			break;
		default:
			break;
		}
	}
}
