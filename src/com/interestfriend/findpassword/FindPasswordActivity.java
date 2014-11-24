package com.interestfriend.findpassword;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.interestfriend.R;
import com.interestfriend.activity.BaseActivity;
import com.interestfriend.findpassword.FindPasswordStep.onNextListener;

public class FindPasswordActivity extends BaseActivity implements
		onNextListener, OnClickListener {
	private TextView txt_title;
	private TextView txt_page;
	private ImageView back;
	private ViewFlipper mVfFlipper;

	private int mCurrentStepIndex = 1;

	private FindPasswordStep step;
	private FindPasswordGetVerifyCode getCode;
	private FindPasswordCheckVerifyCode checkCode;
	private FindPasswordSetPassword setPassword;

	private String cell_phone = "";

	public Dialog dialog;

	private int second = 60;// 用于重新获取验证码时间倒计时

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				second--;
				if (second < 0) {
					second = 60;
					removeCallbacksAndMessages(null);
					return;
				}
				checkCode.setText(second);
				this.sendEmptyMessageDelayed(0, 1000);
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		mVfFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		mVfFlipper.setDisplayedChild(0);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("找回密码");
		txt_page = (TextView) findViewById(R.id.txt_page);
		step = initStep();
		setLitener();
	}

	private FindPasswordStep initStep() {
		txt_page.setText(mCurrentStepIndex + "/3");
		switch (mCurrentStepIndex) {
		case 1:
			if (getCode == null) {
				getCode = new FindPasswordGetVerifyCode(this,
						mVfFlipper.getChildAt(0));
			}
			return getCode;
		case 2:
			if (checkCode == null) {
				checkCode = new FindPasswordCheckVerifyCode(this,
						mVfFlipper.getChildAt(1));
			}
			mHandler.sendEmptyMessage(0);
			return checkCode;
		case 3:
			if (setPassword == null) {
				setPassword = new FindPasswordSetPassword(this,
						mVfFlipper.getChildAt(2));
			}
			return setPassword;
		default:
			break;
		}
		return null;
	}

	@Override
	public void next() {
		mCurrentStepIndex++;
		step = initStep();
		step.setmOnNextListener(this);
		mVfFlipper.showNext();
	}

	private void pre() {
		mCurrentStepIndex--;
		step = initStep();
		step.setmOnNextListener(this);
		mVfFlipper.showPrevious();
	}

	private void setLitener() {
		step.setmOnNextListener(this);
		back.setOnClickListener(this);
	}

	public String getCell_phone() {
		return cell_phone;
	}

	public void setCell_phone(String cell_phone) {
		this.cell_phone = cell_phone;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return false;
	}

	private void back() {
		if (mCurrentStepIndex == 1) {
			finishThisActivity();
		} else {
			mHandler.removeCallbacksAndMessages(null);
			second = 60;
			pre();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			back();
			break;

		default:
			break;
		}
	}

	protected void postHandler() {
		second = 60;
		mHandler.sendEmptyMessage(0);
	}

	protected void removeHandlerMessage() {
		mHandler.removeCallbacksAndMessages(null);
		second = 60;
		checkCode.setEnable();
	}
}
