package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.interestfriend.R;
import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.interfaces.OnEditFocusChangeListener;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.view.MyEditTextDeleteImg;

public class LoginActivity extends BaseActivity implements OnClickListener,
		OnTextLengthChange {
	private MyEditTextDeleteImg edit_telphone;
	private MyEditTextDeleteImg edit_password;
	private Button btn_login;

	private Dialog dialog;

	private int user_id;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ToastUtil.showToast("登录成功", Toast.LENGTH_SHORT);
				startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				finish();
				break;
			case -1:
				ToastUtil.showToast("手机号不存在", Toast.LENGTH_SHORT);
				break;
			case -2:
				ToastUtil.showToast("密码错误", Toast.LENGTH_SHORT);
				break;
			case 2:
				if (dialog != null) {
					mHandler.sendEmptyMessage(2);
					dialog.dismiss();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		edit_password = (MyEditTextDeleteImg) findViewById(R.id.edit_password);
		edit_telphone = (MyEditTextDeleteImg) findViewById(R.id.edit_telphone);
		btn_login = (Button) findViewById(R.id.btn_login);
		setListener();
	}

	private void setListener() {
		edit_telphone.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_telphone, this));
		edit_telphone.addTextChangedListener(new MyEditTextWatcher(
				edit_telphone, this, this));
		edit_password.addTextChangedListener(new MyEditTextWatcher(
				edit_password, this, this));
		edit_password.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_password, this));
		btn_login.setOnClickListener(this);
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_password.getText().toString().length() != 0
					&& edit_telphone.getText().toString().length() != 0) {
				btn_login.setEnabled(true);
				btn_login.setBackgroundResource(R.drawable.button_new);
				return;
			}
		}
		btn_login.setEnabled(false);
		btn_login.setBackgroundResource(R.drawable.button_hui_new);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String user_cellphone = edit_telphone.getText().toString();
			String user_password = edit_password.getText().toString();
			login(user_cellphone, user_password);
			break;

		default:
			break;
		}
	}

	private void login(final String user_cellphone, final String user_password) {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		new Thread() {
			public void run() {
				User user = new User();
				user.setUser_cellphone(user_cellphone);
				user.setUser_password(user_password);
				RetError ret = user.userLogin();
				if (ret == RetError.NONE) {
					user_id = user.getUser_id();
					new Thread() {
						public void run() {
							// loginHuanXin(MD5.Md5_16(user_cellphone),
							// MD5.Md5_16(user_password));
							loginHuanXin(user_cellphone, user_password);
						}
					}.start();
				} else if (ret == RetError.NOT_EXIST_USER) {
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(-1);
				} else if (ret == RetError.WRONG_PASSWORD) {
					mHandler.sendEmptyMessage(2);

					mHandler.sendEmptyMessage(-2);
				}
			}
		}.start();
	}

	private void loginHuanXin(final String username, final String password) {
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(username, password, new EMCallBack() {
			@Override
			public void onSuccess() {
				System.out.println("huanxin success ");
				SharedUtils.setUid(user_id + "");
				// 登陆成功，保存用户名密码
				SharedUtils.setUserName(username);
				// try {
				// // 获取群聊列表(群聊里只有groupid和groupname的简单信息),sdk会把群组存入到内存和db中
				// EMGroupManager.getInstance().getGroupsFromServer();
				// } catch (EaseMobException e) {
				// e.printStackTrace();
				//
				// }
				// List<EMGroup> grouplist = EMGroupManager.getInstance()
				// .getAllGroups();
				// System.out.println("grou::::::::::::quyou--login"
				// + grouplist.toString());
				mHandler.sendEmptyMessage(2);
				mHandler.sendEmptyMessage(1);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, final String message) {

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(),
								"登录失败: " + message, 0).show();

					}
				});
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}
}
