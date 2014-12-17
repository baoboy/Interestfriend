package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.CategoryCircle;
import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.SelectCircleCategoryPopWindow;
import com.interestfriend.popwindow.SelectCircleCategoryPopWindow.OnSelectKey;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.task.CreateCircleTask;
import com.interestfriend.utils.BroadCast;
import com.interestfriend.utils.CategoryCircleUtils;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DateUtils;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

public class CreateCircleActivity extends BaseActivity implements
		OnClickListener, SelectOnclick, TextWatcher {
	private ImageView img_circle_avatar;
	private EditText edit_circle_name;
	private EditText edit_circle_description;
	private Button btn_create;
	private ImageView img_back;
	private TextView txt_title;
	private TextView txt_category;
	private RelativeLayout layout_category;

	private String imgPath = "";
	private String mTakePicturePath;
	private int category_code;

	private SelectPicPopwindow pop;

	private Circles circle;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_circle);
		initView();
		circle = new Circles();
	}

	private void initView() {
		img_circle_avatar = (ImageView) findViewById(R.id.img_circle_avatar);
		edit_circle_description = (EditText) findViewById(R.id.edit_circle_description);
		edit_circle_name = (EditText) findViewById(R.id.edit_circle_name);
		btn_create = (Button) findViewById(R.id.btn_create);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("创建圈子");
		img_back = (ImageView) findViewById(R.id.back);
		txt_category = (TextView) findViewById(R.id.txt_category);
		layout_category = (RelativeLayout) findViewById(R.id.layout_category);
		layout_category.setOnClickListener(this);
		setListener();
	}

	private void setListener() {
		img_circle_avatar.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		img_back.setOnClickListener(this);
		edit_circle_description.addTextChangedListener(this);
		edit_circle_name.addTextChangedListener(this);
	}

	private void setAvatar(Bitmap bitmap, String path) {
		if (bitmap != null) {
			img_circle_avatar.setImageBitmap(PhotoUtils.toRoundCorner(bitmap,
					30));
			imgPath = path;
			setBtnVisible();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
			if (data == null) {
				return;
			}
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
		}
	}

	private void createCircle() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		CreateCircleTask task = new CreateCircleTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result == RetError.NONE) {
					ToastUtil.showToast("圈子创建成功", Toast.LENGTH_SHORT);
					Intent intent = new Intent(Constants.CREATE_CIRCLS);
					intent.putExtra("circle_name", circle.getCircle_name());
					intent.putExtra("circle_logo", circle.getCircle_logo());
					intent.putExtra("circle_id", circle.getCircle_id());
					intent.putExtra("circle_creator", SharedUtils.getIntUid());
					intent.putExtra("circle_description",
							circle.getCircle_description());
					intent.putExtra("group_id", circle.getGroup_id());
					intent.putExtra("circle_create_name",
							SharedUtils.getAPPUserName());
					intent.putExtra("circle_create_time",
							DateUtils.getRegisterTime());
					intent.putExtra("circle_category",
							circle.getCircle_category());
					intent.putExtra("circle_category_name",
							circle.getCircle_category_name());
					BroadCast.sendBroadCast(CreateCircleActivity.this, intent);
					finish();
					Utils.rightOut(CreateCircleActivity.this);
				}
			}
		});
		task.executeParallel(circle);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_circle_avatar:
			pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
			pop.setmSelectOnclick(this);
			pop.show();
			break;
		case R.id.btn_create:
			String circleDesc = edit_circle_description.getText().toString()
					.replace(" ", "");
			String circleName = edit_circle_name.getText().toString()
					.replace(" ", "");
			if (circleName.length() == 0) {
				ToastUtil.showToast("圈子名称不能为空", Toast.LENGTH_SHORT);
				return;
			}
			if (circleDesc.length() == 0) {
				ToastUtil.showToast("圈子描述不能为空", Toast.LENGTH_SHORT);
				return;
			}
			circle.setCircle_description(circleDesc);
			circle.setCircle_name(circleName);
			circle.setCircle_logo(imgPath);
			circle.setCircle_category(category_code);
			circle.setCircle_category_name(CategoryCircleUtils
					.getCateGoryNameByCode(category_code));
			circle.setCircle_creator_name(SharedUtils.getAPPUserName());
			circle.setCircle_create_time(DateUtils.getCircleCreateTime());
			circle.setCreator_id(SharedUtils.getIntUid());
			createCircle();
			break;
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.layout_category:
			SelectCircleCategoryPopWindow pop = new SelectCircleCategoryPopWindow(
					this, v);
			pop.setCallBack(new OnSelectKey() {

				@Override
				public void getSelectKey(CategoryCircle category) {
					category_code = category.getCode();
					txt_category.setText(category.getName());
					setBtnVisible();
				}
			});
			pop.show();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishThisActivity();
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void menu1_select() {
		mTakePicturePath = PhotoUtils.takePicture(this);

	}

	@Override
	public void menu2_select() {
		PhotoUtils.selectPhoto(this);

	}

	private void setBtnVisible() {
		String circleName = edit_circle_name.getText().toString();
		String circleDes = edit_circle_description.getText().toString();
		String circleCategory = txt_category.getText().toString();
		if (!"".equals(imgPath) && circleCategory.length() != 0
				&& circleDes.length() != 0 && circleName.length() != 0) {
			btn_create.setEnabled(true);
			btn_create.setBackgroundResource(R.drawable.btn_selector);
		} else {
			btn_create.setEnabled(false);
			btn_create.setBackgroundResource(R.drawable.btn_disenable_bg);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		setBtnVisible();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}
}
