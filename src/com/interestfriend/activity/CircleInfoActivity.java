package com.interestfriend.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.ConfirmDialog;
import com.interestfriend.popwindow.RightMenuPopwindow;
import com.interestfriend.popwindow.RightMenuPopwindow.OnlistOnclick;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.task.JoinCircleTask;
import com.interestfriend.task.UpdateCircleLogoTask;
import com.interestfriend.utils.BroadCast;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.DampView;

import fynn.app.PromptDialog;

public class CircleInfoActivity extends BaseActivity implements
		OnClickListener, SelectOnclick {
	private ImageView img_logo;
	private TextView txt_description;
	private TextView txt_title;
	private ImageView back;
	private RelativeLayout layout_circle_creator;
	private TextView txt_circle_create_time;
	private TextView txt_citcle_creator_name;
	private TextView txt_circle_category;
	private TextView txt_circle_name;
	private RelativeLayout layout_desc;
	private ImageView img_desc_arrow;

	private String imgLogo = "";
	private String description = "";
	private int circle_id = 0;

	private Circles circle;

	private boolean isLocalCircle = false;
	private RightMenuPopwindow pop;

	private RelativeLayout title_layout;
	private ImageView right_image;

	private DampView view;

	private SelectPicPopwindow pic_pop;
	private String mTakePicturePath = "";
	private String imgPath = "";

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_info);
		getIntentData();
		initView();
		setValue();
	}

	private void getIntentData() {
		isLocalCircle = getIntent().getBooleanExtra("isLocalCircle", false);
		circle = (Circles) getIntent().getSerializableExtra("circle");
		imgLogo = circle.getCircle_logo();
		circle_id = circle.getCircle_id();
		description = circle.getCircle_description();

	}

	private void initView() {
		txt_circle_name = (TextView) findViewById(R.id.txt_circle_name);
		view = (DampView) findViewById(R.id.scrollView1);
		title_layout = (RelativeLayout) findViewById(R.id.title);
		img_desc_arrow = (ImageView) findViewById(R.id.img_arrow_desc);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		view.setImageView(img_logo);
		txt_description = (TextView) findViewById(R.id.circle_description);
		txt_title = (TextView) findViewById(R.id.title_txt);
		back = (ImageView) findViewById(R.id.back);
		layout_circle_creator = (RelativeLayout) findViewById(R.id.layout_circle_creator);
		txt_circle_category = (TextView) findViewById(R.id.txt_circle_category);
		txt_citcle_creator_name = (TextView) findViewById(R.id.txt_circle_creator);
		txt_circle_create_time = (TextView) findViewById(R.id.txt_circle_create_time);
		if (!isLocalCircle) {
			if (circle.findCircleByID(DBUtils.getDBsa(1)) == 0) {
				right_image = (ImageView) findViewById(R.id.rightImg);
				right_image.setVisibility(View.VISIBLE);
				right_image.setImageResource(R.drawable.right_menu);
				right_image.setOnClickListener(this);
			}

		}
		if (circle.getCreator_id() == SharedUtils.getIntUid()) {
			layout_desc = (RelativeLayout) findViewById(R.id.layout_circle_desc);
			layout_desc.setBackgroundResource(R.drawable.left_menu);
			img_desc_arrow.setVisibility(View.VISIBLE);
			layout_desc.setOnClickListener(this);
			img_logo.setOnClickListener(this);

		}
		setListener();

	}

	private void setListener() {
		back.setOnClickListener(this);
		if (circle.getCreator_id() > 0) {
			layout_circle_creator.setOnClickListener(this);
		} else {
			txt_citcle_creator_name
					.setCompoundDrawables(null, null, null, null);
		}
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(imgLogo, img_logo,
				R.drawable.picture_default_head);
		txt_description.setText(description);
		txt_title.setText("圈子信息");
		txt_circle_category.setText(circle.getCircle_category_name());
		txt_circle_create_time.setText(circle.getCircle_create_time());
		txt_citcle_creator_name.setText(circle.getCircle_creator_name());
		txt_circle_name.setText(circle.getCircle_name());
	}

	private void setCircleLogo(Bitmap bitmap, String path) {
		if (bitmap != null) {
			img_logo.setImageBitmap(bitmap);
			imgPath = path;
			upDateLogo();
		}
	}

	private void upDateLogo() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		circle.setCircle_logo(imgPath);
		UpdateCircleLogoTask task = new UpdateCircleLogoTask();
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
				intent.putExtra("circle_id", circle.getCircle_id());
				intent.putExtra("circle_logo", circle.getCircle_logo());
				intent.setAction(Constants.UPDATE_CIRCLE_LOGO);
				BroadCast.sendBroadCast(CircleInfoActivity.this, intent);

			}
		});
		task.executeParallel(circle);
	}

	private void showLogo() {
		List<String> imgUrl = new ArrayList<String>();
		imgUrl.add(imgLogo);
		Intent intent = new Intent(this, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
		startActivity(intent);
	}

	private void joinDialog() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(this,
				"确定要加入该圈子吗？", "确定", "取消", new ConfirmDialog() {

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
		member.setUser_chat_id(SharedUtils.getAPPUserChatID());
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		JoinCircleTask task = new JoinCircleTask(circle.getCreator_id(),
				circle.getCircle_name());
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				if (circle_id < 0) {
					sendBroadcast(new Intent(Constants.RECEIVE_JOIN_CIRCLE));
					ToastUtil.showToast("操作成功！", Toast.LENGTH_SHORT);

				} else {
					ToastUtil.showToast("已发送申请，请等待确认！", Toast.LENGTH_SHORT);
				}
				finishThisActivity();
			}
		});
		task.executeParallel(member);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back:
			finishThisActivity();
			break;

		case R.id.layout_circle_creator:
			intentMemberInfoActivity();
			break;
		case R.id.layout_circle_desc:
			Intent intent = new Intent();
			intent.putExtra("circle", circle);
			intent.setClass(this, UpdateCircleDiscriptionActivity.class);
			startActivityForResult(intent, 300);
			Utils.leftOutRightIn(this);
			break;
		case R.id.rightImg:
			pop = new RightMenuPopwindow(this, title_layout,
					new String[] { "加入圈子" });
			pop.setOnlistOnclick(new OnlistOnclick() {
				@Override
				public void onclick(int position) {
					switch (position) {
					case 0:
						joinDialog();
						break;
					default:
						break;
					}
				}
			});
			pop.show();
			break;
		case R.id.img_logo:
			pic_pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
			pic_pop.setmSelectOnclick(this);
			pic_pop.show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (requestCode == 300) {
			String description = data.getStringExtra("circle_description");
			txt_description.setText(description);
			circle.setCircle_description(description);
		}
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_ALBUM) {

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
							setCircleLogo(bitmap, path);
						}
					}
				}
			}
		}

		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CAMERA) {

			if (resultCode == RESULT_OK) {

				String path = mTakePicturePath;
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(this, this, path);
				} else {
					setCircleLogo(bitmap, path);

				}
			}
		}
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CROP) {
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						setCircleLogo(bitmap, path);
					}
				}
			}
		}
	}

	private void intentMemberInfoActivity() {
		CircleMember member = new CircleMember();
		member.setUser_id(circle.getCreator_id());
		member.read(DBUtils.getDBsa(1));
		Intent intent = new Intent();
		if (circle.getCreator_id() == SharedUtils.getIntUid()) {
			intent.putExtra("circle_member", MyApplation.getMemberSelf());
			intent.setClass(this, CircleMemberOfSelfInfoActivity.class);
		} else {
			intent.putExtra("circle_member", member);
			intent.setClass(this, CircleMemberActivity.class);
		}
		startActivity(intent);
		Utils.leftOutRightIn(this);
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
