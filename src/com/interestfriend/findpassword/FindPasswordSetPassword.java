package com.interestfriend.findpassword;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.OnEditFocusChangeListener;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.task.UpdateLoginPassword;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.MyEditTextDeleteImg;

public class FindPasswordSetPassword extends FindPasswordStep implements
		OnClickListener, OnTextLengthChange {
	private MyEditTextDeleteImg edit_password;
	private MyEditTextDeleteImg edit_agin_password;
	private Button btn_finish;

	public FindPasswordSetPassword(FindPasswordActivity activity,
			View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_agin_password = (MyEditTextDeleteImg) findViewById(R.id.edit_again_password);
		edit_password = (MyEditTextDeleteImg) findViewById(R.id.edit_password);
		btn_finish = (Button) findViewById(R.id.btn_fiish);
	}

	@Override
	public void setListener() {
		btn_finish.setOnClickListener(this);
		edit_agin_password
				.setOnFocusChangeListener(new OnEditFocusChangeListener(
						edit_agin_password, mContext));
		edit_agin_password.addTextChangedListener(new MyEditTextWatcher(
				edit_agin_password, mContext, this));
		edit_password.addTextChangedListener(new MyEditTextWatcher(
				edit_password, mContext, this));
		edit_password.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_password, mContext));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fiish:
			String passwd = edit_password.getText().toString();
			String paswdAgain = edit_agin_password.getText().toString();
			if (!paswdAgain.equals(passwd)) {
				ToastUtil.showToast("两次输入的密码不一致", Toast.LENGTH_SHORT);
				return;
			}
			upDate(passwd);
			break;

		default:
			break;
		}
	}

	private Dialog dialog;

	private void upDate(String password) {
		dialog = DialogUtil.createLoadingDialog(mActivity, "请稍候");
		dialog.show();
		UpdateLoginPassword task = new UpdateLoginPassword();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("操作失败", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("操作成功", Toast.LENGTH_SHORT);
				mActivity.finish();
				Utils.rightOut(mContext);
			}
		});
		User user = new User();
		user.setUser_password(password);
		user.setUser_cellphone(mActivity.getCell_phone());
		task.execute(user);
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_password.getText().toString().length() != 0
					&& edit_agin_password.getText().toString().length() != 0) {
				btn_finish.setEnabled(true);
				btn_finish.setBackgroundResource(R.drawable.btn_selector);
				return;
			}
		}
		btn_finish.setEnabled(false);
		btn_finish.setBackgroundResource(R.drawable.btn_disenable_bg);
	}
}
