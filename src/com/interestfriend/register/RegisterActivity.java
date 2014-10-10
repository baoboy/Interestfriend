package com.interestfriend.register;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.interestfriend.R;
import com.interestfriend.data.User;
import com.interestfriend.register.RegisterStep.onNextListener;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.ToastUtil;

public class RegisterActivity extends Activity implements onNextListener {
	private ViewFlipper mVfFlipper;

	private RegisterStep reStep;
	private RegisterUserName reUserName;
	private RegisterBasicInfo reBasicInfo;
	private RegisterPhone rePhone;
	private RegisterSetPassword reSetPasswd;
	private User mRegister;

	private int mCurrentStepIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_activity);
		mRegister = new User();
		initView();
	}

	private void initView() {
		mVfFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		mVfFlipper.setDisplayedChild(0);
		reStep = initStep();
		setListener();
	}

	private void setListener() {
		reStep.setmOnNextListener(this);
	}

	protected User getmRegister() {
		return mRegister;
	}

	public void setmRegister(User mRegister) {
		this.mRegister = mRegister;
	}

	private RegisterStep initStep() {
		switch (mCurrentStepIndex) {
		case 1:
			if (reUserName == null) {
				reUserName = new RegisterUserName(this,
						mVfFlipper.getChildAt(0));
			}
			return reUserName;
		case 2:
			if (reBasicInfo == null) {
				reBasicInfo = new RegisterBasicInfo(this,
						mVfFlipper.getChildAt(1));
			}
			return reBasicInfo;
		case 3:
			if (rePhone == null) {
				rePhone = new RegisterPhone(this, mVfFlipper.getChildAt(2));
			}
			return rePhone;
		case 4:
			if (reSetPasswd == null) {
				reSetPasswd = new RegisterSetPassword(this,
						mVfFlipper.getChildAt(3));
			}
			return reSetPasswd;
		default:
			break;
		}
		return null;
	}

	@Override
	public void next() {
		mCurrentStepIndex++;
		reStep = initStep();
		reStep.setmOnNextListener(this);
		mVfFlipper.showNext();
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
							reBasicInfo.setUserPhoto(bitmap, path);
						}
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:

			if (resultCode == RESULT_OK) {

				String path = reBasicInfo.getTakePicturePath();
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(this, this, path);
				} else {
					reBasicInfo.setUserPhoto(bitmap, path);
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						reBasicInfo.setUserPhoto(bitmap, path);
					}
				}
			}
			break;
		}
	}
}
