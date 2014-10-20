package com.interestfriend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.utils.UniversalImageLoadTool;

public class CircleMemberOfSelfInfoActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_avatar;
	private TextView txt_register_time;
	private TextView txt_gender;
	private TextView txt_birthday;
	private TextView txt_title;
	private TextView txt_user_name;
	private TextView txt_declaration;
	private TextView txt_description;
	private RelativeLayout layout_declaration;
	private RelativeLayout layout_description;
	private RelativeLayout layout_user_name;
 
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
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		txt_declaration = (TextView) findViewById(R.id.txt_declaration);
		txt_description = (TextView) findViewById(R.id.txt_description);
		layout_declaration = (RelativeLayout) findViewById(R.id.layout_declaration);
		layout_description = (RelativeLayout) findViewById(R.id.layout_description);
		layout_user_name = (RelativeLayout) findViewById(R.id.layout_user_name);
 		setListener();
		setValue();
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(member.getUser_avatar(), img_avatar,
				R.drawable.picture_default_head);
		txt_birthday.setText(member.getUser_birthday());
		txt_gender.setText(member.getUser_gender());
		txt_register_time.setText(member.getUser_register_time());
		txt_title.setText("个人资料");
		txt_user_name.setText(member.getUser_name());
		txt_declaration.setText(member.getUser_declaration());
		txt_description.setText(member.getUser_description());
	}

	private void setListener() {
 		layout_declaration.setOnClickListener(this);
		layout_description.setOnClickListener(this);
		txt_user_name.setOnClickListener(this);
		layout_user_name.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		 
 		case R.id.layout_declaration:
			intentUpdateActivity("交友宣言", txt_declaration.getText().toString(),
					300);
			break;
		case R.id.layout_description:
			intentUpdateActivity("个人介绍", txt_description.getText().toString(),
					400);
			break;
		case R.id.layout_user_name:
			intentUpdateActivity("昵称", txt_user_name.getText().toString(), 200);
			break;
		default:
			break;
		}
	}

	private void intentUpdateActivity(String column, String content,
			int requestCode) {
		Intent intent = new Intent();
		intent.putExtra("column", column);
		intent.putExtra("content", content);
		intent.putExtra("requestCode", requestCode);
		intent.putExtra("member", member);
		intent.setClass(this, UpdateUserInfoActivity.class);
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case 200:
			txt_user_name.setText(data.getStringExtra("value"));
			break;
		case 300:
			txt_declaration.setText(data.getStringExtra("value"));
			break;
		case 400:
			txt_description.setText(data.getStringExtra("value"));
			break;
		default:
			break;
		}
	}
}
