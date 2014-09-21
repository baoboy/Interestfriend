package com.interestfriend.register;

import android.app.Dialog;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.Register;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.task.VerifyCellPhoneTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.view.MyEditTextDeleteImg;

public class RegisterPhone extends RegisterStep implements OnClickListener,
		OnTextLengthChange {
	private MyEditTextDeleteImg edit_telephone;
	private Button btn_next;
	private Dialog dialog;

	public RegisterPhone(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);

	}

	@Override
	public void initView() {
		edit_telephone = (MyEditTextDeleteImg) findViewById(R.id.edit_telephone);
		btn_next = (Button) findViewById(R.id.btnNext);
	}

	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
		edit_telephone.addTextChangedListener(new MyEditTextWatcher(
				edit_telephone, mContext, this));
	}

	private void verifyCellPhone() {
		Register re = new Register();
		re.setUser_cellphone(edit_telephone.getText().toString());
		VerifyCellPhoneTask task = new VerifyCellPhoneTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				dialog.dismiss();
				if (result != RetError.NONE) {
					ToastUtil.showToast("手机号已经注册", Toast.LENGTH_SHORT);
					return;
				}
				mActivity.getmRegister().setUser_cellphone(
						edit_telephone.getText().toString());

				mOnNextListener.next();
			}
		});
		task.execute(re);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			dialog = DialogUtil.createLoadingDialog(mContext, "请稍候");
			dialog.show();
			verifyCellPhone();
			break;

		default:
			break;
		}

	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_telephone.getText().toString().length() != 0) {
				btn_next.setEnabled(true);
				btn_next.setBackgroundResource(R.drawable.button_new);
				return;
			}
		}
		btn_next.setEnabled(false);
		btn_next.setBackgroundResource(R.drawable.button_hui_new);
	}
	//
	// if (s.toString().length() > 0) {
	// char[] chars = s.toString().toCharArray();
	// StringBuffer buffer = new StringBuffer();
	// for (int i = 0; i < chars.length; i++) {
	// if (i % 4 == 2) {
	// buffer.append(chars[i] + "  ");
	// } else {
	// buffer.append(chars[i]);
	// }
	// }
	// edit_telephone.setText(buffer.toString());
	// }
}
