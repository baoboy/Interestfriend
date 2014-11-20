package com.interestfriend.findpassword;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.interestfriend.R;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.interfaces.OnEditFocusChangeListener;
import com.interestfriend.view.MyEditTextDeleteImg;

public class FindPasswordCheckVerifyCode extends FindPasswordStep implements
		OnClickListener, OnTextLengthChange {
	private Button btn_next;
	private MyEditTextDeleteImg edit_code;

	public FindPasswordCheckVerifyCode(FindPasswordActivity activity,
			View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		btn_next = (Button) findViewById(R.id.btnNext);
		edit_code = (MyEditTextDeleteImg) findViewById(R.id.edit_verify_code);
	}

	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
		edit_code.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_code, mContext));
		edit_code.addTextChangedListener(new MyEditTextWatcher(edit_code,
				mContext, this));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			mOnNextListener.next();
			break;

		default:
			break;
		}
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_code.getText().toString().length() != 0) {
				btn_next.setEnabled(true);
				btn_next.setBackgroundResource(R.drawable.btn_selector);
				return;
			}
		}
		btn_next.setEnabled(false);
		btn_next.setBackgroundResource(R.drawable.btn_disenable_bg);
	}
}
