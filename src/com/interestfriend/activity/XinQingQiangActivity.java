package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.XinQingAdapter;
import com.interestfriend.data.XinQing;
import com.interestfriend.data.XinQingList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.task.GetXinQingListTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.view.PullDownView;
import com.interestfriend.view.PullDownView.OnPullDownListener;

public class XinQingQiangActivity extends BaseActivity implements
		OnClickListener, SelectOnclick, OnPullDownListener {
	private ImageView img_create;

	private PullDownView mPullDownView;

	private ListView mListView;

	private XinQingAdapter adapter;

	private List<XinQing> lists = new ArrayList<XinQing>();

	private XinQingList xlist;

	private Dialog dialog;

	private SelectPicPopwindow pop;
	private String imgPath = "";
	private String mTakePicturePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xin_qing_qiang);
		initView();
		setValue();
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		xlist = new XinQingList();
		xlist.setRefushState(1);
		refushServerData();
	}

	private void initView() {
		img_create = (ImageView) findViewById(R.id.img_create);
		mPullDownView = (PullDownView) findViewById(R.id.PullDownlistView);
		mListView = mPullDownView.getListView();
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setCacheColorHint(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		setListener();
	}

	private void setListener() {
		img_create.setOnClickListener(this);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.notifyDidMore();
		mPullDownView.setFooterVisible(false);
	}

	private void setValue() {
		adapter = new XinQingAdapter(lists, this);
		mListView.setAdapter(adapter);
		mPullDownView.addFooterView();
	}

	private void refushServerData() {
		GetXinQingListTask task = new GetXinQingListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				mPullDownView.RefreshComplete();
				mPullDownView.notifyDidMore();
				if (result != RetError.NONE) {
					return;
				}
				lists.clear();
				lists.addAll(xlist.getLists());
				adapter.notifyDataSetChanged();
				if (lists.size() > 19) {
					mPullDownView.setFooterVisible(true);
				} else {
					mPullDownView.setFooterVisible(false);
				}
			}
		});
		task.executeParallel(xlist);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_create:
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

	@Override
	public void onRefresh() {

	}

	@Override
	public void onMore() {

	}
}
