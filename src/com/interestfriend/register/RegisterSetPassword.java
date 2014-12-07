package com.interestfriend.register;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.activity.LoginActivity;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.interfaces.OnEditFocusChangeListener;
import com.interestfriend.task.UserRegisterTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.MyEditTextDeleteImg;

public class RegisterSetPassword extends RegisterStep implements
		OnClickListener, OnTextLengthChange {
	private MyEditTextDeleteImg edit_password;
	private MyEditTextDeleteImg edit_agagin_passwrod;
	private Button btn_finish;
	private Dialog dialog;

	public RegisterSetPassword(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);

	}

	@Override
	public void initView() {
		edit_password = (MyEditTextDeleteImg) findViewById(R.id.edit_password);
		edit_agagin_passwrod = (MyEditTextDeleteImg) findViewById(R.id.edit_again_password);
		btn_finish = (Button) findViewById(R.id.btn_fiish);
	}

	@Override
	public void setListener() {
		btn_finish.setOnClickListener(this);
		edit_agagin_passwrod
				.setOnFocusChangeListener(new OnEditFocusChangeListener(
						edit_agagin_passwrod, mContext));
		edit_agagin_passwrod.addTextChangedListener(new MyEditTextWatcher(
				edit_agagin_passwrod, mContext, this));
		edit_password.addTextChangedListener(new MyEditTextWatcher(
				edit_password, mContext, this));
		edit_password.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_password, mContext));
	}

	@Override
	public void onClick(View v) {
		String passwd = edit_password.getText().toString();
		String paswdAgain = edit_agagin_passwrod.getText().toString();
		if (!paswdAgain.equals(passwd)) {
			ToastUtil.showToast("两次输入的密码不一致", Toast.LENGTH_SHORT);
			return;
		}
		mActivity.getmRegister().setUser_password(passwd);
		dialog = DialogUtil.createLoadingDialog(mContext, "请稍候");
		dialog.show();
		userRegister();
	}

	private void userRegister() {
		UserRegisterTask taks = new UserRegisterTask();
		taks.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("注册成功", Toast.LENGTH_SHORT);
				mContext.startActivity(new Intent(mContext, LoginActivity.class));
				Utils.rightOut(mActivity);
			}
		});
		taks.executeParallel(mActivity.getmRegister());
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_password.getText().toString().length() != 0
					&& edit_agagin_passwrod.getText().toString().length() != 0) {
				btn_finish.setEnabled(true);
				btn_finish.setBackgroundResource(R.drawable.btn_selector);
				return;
			}
		}
		btn_finish.setEnabled(false);
		btn_finish.setBackgroundResource(R.drawable.btn_disenable_bg);
	}
}
