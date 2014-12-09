package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.task.UpdateUserAvatarTask;
import com.interestfriend.utils.BroadCast;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.PinYinUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.DampView;

public class CircleMemberOfSelfInfoActivity extends BaseActivity implements
		OnClickListener, SelectOnclick {
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
	private ImageView back;

	private CircleMember member;

	private SelectPicPopwindow pop;

	private String mTakePicturePath = "";
	private String imgPath = "";

	private Dialog dialog;
	private DampView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_member_info);
		member = (CircleMember) getIntent().getSerializableExtra(
				"circle_member");
		initView();
	}

	private void initView() {
		view = (DampView) findViewById(R.id.scrollView1);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		view.setImageView(img_avatar);
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
	}

	private void setListener() {
		layout_declaration.setOnClickListener(this);
		layout_description.setOnClickListener(this);
		txt_user_name.setOnClickListener(this);
		layout_user_name.setOnClickListener(this);
		img_avatar.setOnClickListener(this);
		back.setOnClickListener(this);

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
		case R.id.img_avatar:
			pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
			pop.setmSelectOnclick(this);
			pop.show();
			break;
		case R.id.back:
			finishThisActivity();
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
		Utils.leftOutRightIn(this);
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
			Intent intent = new Intent();
			intent.putExtra("user_name", data.getStringExtra("value"));
			intent.setAction(Constants.UPDATE_USER_NAME);
			BroadCast
					.sendBroadCast(CircleMemberOfSelfInfoActivity.this, intent);
			SharedUtils.setAPPUserName(data.getStringExtra("value"));
			SharedUtils.setAPPUserSortKey(PinYinUtil.converterToFirstSpell(data
					.getStringExtra("value")));
			break;
		case 300:
			txt_declaration.setText(data.getStringExtra("value"));
			SharedUtils.setAPPUserDeclaration(data.getStringExtra("value"));
			break;
		case 400:
			txt_description.setText(data.getStringExtra("value"));
			SharedUtils.setAPPUserDescription(data.getStringExtra("value"));
			break;
		case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:

			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				if (!FileUtils.isSdcardExist()) {
					ToastUtil.showToast("SD卡不可用,请检查", Toast.LENGTH_SHORT);
					return;
				}
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						Bitmap bitmap = BitmapFactory.decodeFile(path);
						if (PhotoUtils.bitmapIsLarge(bitmap)) {
							PhotoUtils.cropPhoto(this, this, path);
						} else {
							setAvatar(bitmap, path);
						}
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:

			if (resultCode == RESULT_OK) {

				String path = mTakePicturePath;
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(this, this, path);
				} else {
					setAvatar(bitmap, path);

				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						setAvatar(bitmap, path);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	private void setAvatar(Bitmap bitmap, String path) {
		if (bitmap != null) {
			img_avatar.setImageBitmap(bitmap);
			imgPath = path;
			upDateAvatar();
		}
	}

	private void upDateAvatar() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		member.setUser_avatar(imgPath);
		UpdateUserAvatarTask task = new UpdateUserAvatarTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("上传成功", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("member", member);
				intent.setAction(Constants.UPDATE_USER_INFO);
				BroadCast.sendBroadCast(CircleMemberOfSelfInfoActivity.this,
						intent);
				intent = new Intent();
				intent.putExtra("user_avatar", member.getUser_avatar());
				intent.setAction(Constants.UPDATE_USER_AVATAR);
				BroadCast.sendBroadCast(CircleMemberOfSelfInfoActivity.this,
						intent);
				SharedUtils.setAPPUserAvatar(member.getUser_avatar());
			}
		});
		task.executeParallel(member);
	}

	@Override
	public void menu1_select() {
		mTakePicturePath = PhotoUtils.takePicture(this);

	}

	@Override
	public void menu2_select() {
		PhotoUtils.selectPhoto(this);

	}

}
