package com.interestfriend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.utils.Utils;

/**
 * 关于界面
 * 
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	private ImageView back;
	private TextView titleTxt;
	private TextView version;
	private TextView user_agreement;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_txt);
		titleTxt.setText("关于趣友");
		version = (TextView) findViewById(R.id.version);
		version.setText(getResources().getString(R.string.app_name)
				+ Utils.getVersionName(this) + " For Android");
		user_agreement = (TextView) findViewById(R.id.txt_user_agreement);
		user_agreement.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			Utils.rightOut(this);
			break;
		case R.id.txt_user_agreement:
			startActivity(new Intent(this, UserAgreement.class));
			Utils.leftOutRightIn(this);
			break;
		default:
			break;
		}
	}
}
