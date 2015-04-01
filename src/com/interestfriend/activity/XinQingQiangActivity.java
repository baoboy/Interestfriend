package com.interestfriend.activity;

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
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.ToastUtil;

public class XinQingQiangActivity extends BaseActivity implements
		OnClickListener, SelectOnclick {
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

	private SelectPicPopwindow pop;
	private String imgPath = "";
	private String mTakePicturePath;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_create:
			// startActivity(new Intent(this, CreateXinQingActivity.class));
			// Utils.leftOutRightIn(this);
			pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
			pop.setmSelectOnclick(this);
			pop.show();
			break;

		default:
			break;
		}
	}

	@Override
	public void menu1_select() {
		mTakePicturePath = PhotoUtils.takePicture(this);

	}

	@Override
	public void menu2_select() {
		PhotoUtils.selectPhoto(this);

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
							intentActivity(path);
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
					intentActivity(path);
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					intentActivity(path);
				}
			}
			break;
		}
	}

	private void intentActivity(String image_path) {
		startActivity(new Intent(this, SetImageEffectActivity.class).putExtra(
				"image_path", image_path));
	}
}
