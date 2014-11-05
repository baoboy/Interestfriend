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
import com.interestfriend.task.GetFindPasswordVerifyCodeTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.view.MyEditTextDeleteImg;

public class FindPasswordGetVerifyCode extends FindPasswordStep implements
		OnClickListener {
	private MyEditTextDeleteImg edit_phone;
	private Button btn_next;

	private Dialog dialog;

	public FindPasswordGetVerifyCode(FindPasswordActivity activity,
			View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_phone = (MyEditTextDeleteImg) findViewById(R.id.edit_telephone);
		btn_next = (Button) findViewById(R.id.btnNext);
	}

	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
	}

	private void getVerifyCode() {
		User re = new User();
		re.setUser_cellphone(edit_phone.getText().toString());
		GetFindPasswordVerifyCodeTask task = new GetFindPasswordVerifyCodeTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				dialog.dismiss();
				if (result != RetError.NONE) {
					ToastUtil.showToast("ÊÖ»úºÅ»¹Î´×¢²á£¬¸Ï¿ì×¢²á°É", Toast.LENGTH_SHORT);
					return;
				}
				// mActivity.getmRegister().setUser_cellphone(
				// edit_telephone.getText().toString());

				mOnNextListener.next();
				mActivity.setCell_phone(edit_phone.getText().toString());
			}
		});
		task.execute(re);
	}

	@Override
	public void onClick(View v) {
		dialog = DialogUtil.createLoadingDialog(mContext, "ÇëÉÔºò");
		dialog.show();
		getVerifyCode();
	}

}
