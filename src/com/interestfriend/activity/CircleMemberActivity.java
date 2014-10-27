package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.adapter.MemberCirclesAdapter;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.CirclesList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetMemberCircleListTask;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;

public class CircleMemberActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private ImageView img_avatar;
	private TextView txt_register_time;
	private TextView txt_gender;
	private TextView txt_birthday;
	private TextView txt_title;
	private TextView txt_user_name;
	private TextView txt_declaration;
	private TextView txt_description;
	private ListView circle_listView;
	private Button btn_chat;
	private ImageView back;

	private CircleMember member;

	private CirclesList circleList;

	private List<MyCircles> lists = new ArrayList<MyCircles>();

	private MemberCirclesAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_member);
		member = (CircleMember) getIntent()
				.getSerializableExtra("circle_ember");
		circleList = new CirclesList();
		initView();
		getCircleList();
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
		btn_chat = (Button) findViewById(R.id.btn_chat);
		circle_listView = (ListView) findViewById(R.id.circle_listView);
		back = (ImageView) findViewById(R.id.back);
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
		adapter = new MemberCirclesAdapter(this, lists);
		circle_listView.setAdapter(adapter);
	}

	private void setListener() {
		btn_chat.setOnClickListener(this);
		txt_user_name.setOnClickListener(this);
		circle_listView.setOnItemClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_chat:
			Intent intent = new Intent();
			intent.putExtra("userId", member.getUser_chat_id());
			intent.setClass(this, ChatActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			finishThisActivity();
			break;
		default:
			break;
		}
	}

	private void getCircleList() {
		circleList.setMember_id(member.getUser_id());
		GetMemberCircleListTask task = new GetMemberCircleListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				lists.addAll(circleList.getListCircles());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(circleList);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("circle", lists.get(position));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
		Utils.showSoftInput(this);
	}
}
