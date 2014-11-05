package com.interestfriend.findpassword;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.interestfriend.R;
import com.interestfriend.activity.BaseActivity;
import com.interestfriend.findpassword.FindPasswordStep.onNextListener;

public class FindPasswordActivity extends BaseActivity implements
		onNextListener {
	private TextView txt_title;
	private ViewFlipper mVfFlipper;

	private int mCurrentStepIndex = 1;

	private FindPasswordStep step;
	private FindPasswordGetVerifyCode getCode;
	private FindPasswordCheckVerifyCode checkCode;
	private FindPasswordSetPassword setPassword;

	private String cell_phone = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		initView();
	}

	private void initView() {
		mVfFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		mVfFlipper.setDisplayedChild(0);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("’“ªÿ√‹¬Î");
		step = initStep();
		setLitener();
	}

	private FindPasswordStep initStep() {
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

	private void setLitener() {
		step.setmOnNextListener(this);
	}

	public String getCell_phone() {
		return cell_phone;
	}

	public void setCell_phone(String cell_phone) {
		this.cell_phone = cell_phone;
	}

}
