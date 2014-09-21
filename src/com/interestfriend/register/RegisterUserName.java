package com.interestfriend.register;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.interestfriend.R;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.view.MyEditTextDeleteImg;

public class RegisterUserName extends RegisterStep implements OnClickListener,
		OnTextLengthChange {
	private MyEditTextDeleteImg edit_user_name;
	private Button btn_next;

	public RegisterUserName(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_user_name = (MyEditTextDeleteImg) findViewById(R.id.edit_user_name);
		btn_next = (Button) findViewById(R.id.btnNext);
		setListener();
	}

	@Override
	public void setListener() {
		edit_user_name.addTextChangedListener(new MyEditTextWatcher(
				edit_user_name, mContext, this));
		btn_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			mActivity.getmRegister().setUser_name(
					edit_user_name.getText().toString());
			mOnNextListener.next();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_user_name.getText().toString().length() != 0) {
				btn_next.setEnabled(true);
				btn_next.setBackgroundResource(R.drawable.button_new);
				return;
			}
		}
		btn_next.setEnabled(false);
		btn_next.setBackgroundResource(R.drawable.button_hui_new);
	}

}
