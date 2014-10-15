package com.interestfriend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.interestfriend.R;
import com.interestfriend.register.RegisterActivity;
import com.interestfriend.utils.SharedUtils;

public class WelcomActivity extends BaseActivity implements OnClickListener {
	private Button btn_register;
	private Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
		if (SharedUtils.getIntUid() > 0) {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
		}
		initView();
	}

	private void initView() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		setListener();
	}

	private void setListener() {
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.btn_login:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		default:
			break;
		}
	}

}
