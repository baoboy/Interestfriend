package com.interestfriend.findpassword;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.interestfriend.R;

public class FindPasswordCheckVerifyCode extends FindPasswordStep implements
		OnClickListener {
	private Button btn_next;

	public FindPasswordCheckVerifyCode(FindPasswordActivity activity,
			View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		btn_next = (Button) findViewById(R.id.btnNext);
	}

	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
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

}
