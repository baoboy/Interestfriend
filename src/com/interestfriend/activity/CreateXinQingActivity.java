package com.interestfriend.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.interestfriend.R;

public class CreateXinQingActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_camera_switch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_xin_qing);
		initView();
	}

	private void initView() {
		img_camera_switch = (ImageView) findViewById(R.id.rightImg);
		img_camera_switch.setVisibility(View.VISIBLE);
		img_camera_switch
				.setImageResource(R.drawable.scene_photo_button_switch);
		setListener();
	}

	private void setListener() {
		img_camera_switch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

	}
}
