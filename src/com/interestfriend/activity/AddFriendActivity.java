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

import com.easemob.chat.EMContactManager;
import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
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
		new Thread(new Runnable() {
			public void run() {

				try {
					// 写死了个reason，实际应该让用户手动填入
					EMContactManager.getInstance().addContact(
							add_user_chat_id,
							edit_content.getText().toString() + ","
									+ SharedUtils.getAPPUserName() + ","
									+ SharedUtils.getAPPUserAvatar() + ","
									+ SharedUtils.getUid() + ","
									+ MyApplation.getCircle_name() + ",");
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							ToastUtil.showToast("发送请求成功,等待对方验证",
									Toast.LENGTH_SHORT);
							finishThisActivity();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							ToastUtil.showToast("请求添加好友失败", Toast.LENGTH_SHORT);
						}
					});
				}
			}
		}).start();
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
