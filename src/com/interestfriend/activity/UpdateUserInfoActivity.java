package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.UpDateMemberInfoTask;
import com.interestfriend.utils.BroadCast;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

public class UpdateUserInfoActivity extends BaseActivity implements
		OnClickListener {
	private String column = "";
	private String content = "";

	private TextView txt_title;
	private EditText edit_content;
	private Button btn_save;

	private Dialog dialog;

	private CircleMember member;

	private int requestCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_user_info);
		member = (CircleMember) getIntent().getSerializableExtra("member");
		requestCode = getIntent().getIntExtra("requestCode", 0);
		initView();
		setValue();
	}

	private void initView() {
		txt_title = (TextView) findViewById(R.id.title_txt);
		edit_content = (EditText) findViewById(R.id.txt_content);
		btn_save = (Button) findViewById(R.id.btn_save);
		setListener();
	}

	private void setListener() {
		btn_save.setOnClickListener(this);
	}

	private void setValue() {
		column = getIntent().getStringExtra("column");
		content = getIntent().getStringExtra("content");
		edit_content.setText(content);
		edit_content.setSelection(edit_content.getText().toString().length());
		txt_title.setText(column);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			String value = edit_content.getText().toString().trim();
			if ("êÇ³Æ".equals(column)) {
				if (value.length() == 0) {
					ToastUtil.showToast("êÇ³Æ²»ÄÜÎª¿Õ", Toast.LENGTH_SHORT);
					return;
				}
			}
			if (value.equals(content)) {
				finishThisActivity();
			}
			upDate(value);
			break;

		default:
			break;
		}
	}

	private void upDate(final String value) {
		dialog = DialogUtil.createLoadingDialog(this, "ÇëÉÔºò");
		dialog.show();
		UpDateMemberInfoTask task = new UpDateMemberInfoTask(column, value);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("²Ù×÷Ê§°Ü", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("²Ù×÷³É¹¦", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("value", value);
				setResult(requestCode, intent);
				intent.putExtra("member", member);
				intent.setAction(Constants.UPDATE_USER_INFO);
				BroadCast.sendBroadCast(UpdateUserInfoActivity.this, intent);
				finishThisActivity();
			}
		});
		task.execute(member);

	}
}
