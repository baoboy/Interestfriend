package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.ConfirmDialog;
import com.interestfriend.task.JoinCircleTask;
import com.interestfriend.utils.BroadCast;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;

public class CircleInfoActivity extends BaseActivity implements OnClickListener {
	private ImageView img_logo;
	private TextView txt_description;
	private Button btn_join;

	private String imgLogo = "";
	private String description = "";
	private int circle_id = 0;

	private Circles circle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_info);
		getIntentData();
		initView();
		setValue();
	}

	private void getIntentData() {
		// imgLogo = getIntent().getStringExtra("imgLogo");
		// description = getIntent().getStringExtra("description");
		// circle_id = getIntent().getIntExtra("circle_id", 0);
		circle = (Circles) getIntent().getSerializableExtra("circle");
		imgLogo = circle.getCircle_logo();
		circle_id = circle.getCircle_id();
		description = circle.getCircle_description();
	}

	private void initView() {
		img_logo = (ImageView) findViewById(R.id.img_logo);
		txt_description = (TextView) findViewById(R.id.circle_description);
		btn_join = (Button) findViewById(R.id.btn_join);
		setListener();
	}

	private void setListener() {
		btn_join.setOnClickListener(this);
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(imgLogo, img_logo,
				R.drawable.picture_default_head);
		txt_description.setText(description);
	}

	private void joinDialog() {
		Dialog dialog = DialogUtil.confirmDialog(this, "确定要加入该圈子吗？", "确定",
				"取消", new ConfirmDialog() {

					@Override
					public void onOKClick() {
						joinCircle();
					}

					@Override
					public void onCancleClick() {

					}
				});
		dialog.show();
	}

	private void joinCircle() {
		CircleMember member = new CircleMember();
		member.setCircle_id(circle_id);
		member.setGroup_id(circle.getGroup_id());
		member.setUser_id(SharedUtils.getIntUid());
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		JoinCircleTask task = new JoinCircleTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("成功加入", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("circle", circle);
				intent.setAction(Constants.JOIN_CIRCLE);
				BroadCast.sendBroadCast(CircleInfoActivity.this, intent);

				circle.write(DBUtils.getDBsa(2));
			}
		});
		task.execute(member);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_join:
			joinDialog();
			break;

		default:
			break;
		}
	}
}
