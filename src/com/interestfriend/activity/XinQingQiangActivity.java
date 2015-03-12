package com.interestfriend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.interestfriend.R;
import com.interestfriend.utils.Utils;

public class XinQingQiangActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_create;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xin_qing_qiang);
		initView();
	}

	private void initView() {
		img_create = (ImageView) findViewById(R.id.img_create);
		setListener();
	}

	private void setListener() {
		img_create.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_create:
			startActivity(new Intent(this, CreateXinQingActivity.class));
			Utils.leftOutRightIn(this);
			break;

		default:
			break;
		}
	}
}
