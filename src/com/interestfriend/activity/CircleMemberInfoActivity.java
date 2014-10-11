package com.interestfriend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.utils.UniversalImageLoadTool;

public class CircleMemberInfoActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_avatar;
	private TextView txt_register_time;
	private TextView txt_gender;
	private TextView txt_birthday;
	private TextView txt_title;
	private Button btn_chat;

	private CircleMember member;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_member_info);
		member = (CircleMember) getIntent()
				.getSerializableExtra("circle_ember");
		initView();

	}

	private void initView() {
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		txt_birthday = (TextView) findViewById(R.id.txt_birthday);
		txt_gender = (TextView) findViewById(R.id.txt_gender);
		txt_register_time = (TextView) findViewById(R.id.txt_register_time);
		txt_title = (TextView) findViewById(R.id.title_txt);
		btn_chat = (Button) findViewById(R.id.btn_chat);
		setListener();
		setValue();
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(member.getUser_avatar(), img_avatar,
				R.drawable.picture_default_head);
		txt_birthday.setText(member.getUser_birthday());
		txt_gender.setText(member.getUser_gender());
		txt_register_time.setText(member.getUser_register_time());
		txt_title.setText(member.getUser_name());
	}

	private void setListener() {
		btn_chat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chat:
			Intent intent = new Intent();
			intent.putExtra("userId", member.getUser_chat_id());
			intent.putExtra("userName", member.getUser_name());
			intent.setClass(this, ChatActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
