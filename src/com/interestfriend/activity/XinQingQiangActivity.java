package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.XinQingAdapter;
import com.interestfriend.data.XinQing;
import com.interestfriend.data.XinQingComment;
import com.interestfriend.data.XinQingList;
import com.interestfriend.data.XinQingPraise;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.task.GetXinQingListTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.PhotoUtils;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
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
	private String mTakePicturePath;

	private ImageView back;
	private TextView txt_title;

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
		registerBoradcastReceiver();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("心情墙");
		img_create = (ImageView) findViewById(R.id.img_create);
		mPullDownView = (PullDownView) findViewById(R.id.PullDownlistView);
		mListView = mPullDownView.getListView();
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setCacheColorHint(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		img_create.setOnClickListener(this);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.notifyDidMore();
		mPullDownView.setFooterVisible(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("xinqing", lists.get(position - 1));
				intent.putExtra("position", position - 1);
				intent.setClass(XinQingQiangActivity.this,
						XinQingCommentActivity.class);
				startActivity(intent);
				Utils.leftOutRightIn(XinQingQiangActivity.this);
			}
		});

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
		case R.id.back:
			finishThisActivity();
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
		if (lists.size() == 0) {
			mPullDownView.RefreshComplete();
			return;
		}
		xlist.setRefushState(1);
		xlist.setRefushTime(lists.get(0).getPublish_time());
		refushServerData();
	}

	@Override
	public void onMore() {
		xlist.setRefushState(2);
		xlist.setRefushTime(lists.get((lists.size() - 1)).getPublish_time());
		refushServerData();

	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.COMMENT_XINQING);
		myIntentFilter.addAction(Constants.XINQING_COMMENT_PRAISE);
		myIntentFilter.addAction(Constants.XINQINGCOMMENT_CANCEL_PRAISE);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 定义广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.COMMENT_XINQING)) {
				XinQingComment coment = (XinQingComment) intent
						.getSerializableExtra("comment");
				int position = intent.getIntExtra("position", -1);
				lists.get(position).getComments().add(0, coment);
				adapter.notifyDataSetChanged();
			} else if (action.equals(Constants.XINQING_COMMENT_PRAISE)) {
				int xinqing_id = intent.getIntExtra("xinqing_id", 0);
				for (XinQing xinqing : lists) {
					if (xinqing.getXinqing_id() == xinqing_id) {
						XinQingPraise pr = new XinQingPraise();
						pr.setXinqing_id(xinqing.getXinqing_id());
						pr.setUser_avatar(SharedUtils.getAPPUserAvatar());
						pr.setUser_id(SharedUtils.getIntUid());
						xinqing.getPraises().add(pr);
						xinqing.setPraise_count(xinqing.getPraise_count() + 1);
						xinqing.setPraise(!xinqing.isPraise());
						adapter.notifyDataSetChanged();
						break;
					}
				}

			} else if (action.equals(Constants.XINQINGCOMMENT_CANCEL_PRAISE)) {
				int xinqing_id = intent.getIntExtra("xinqing_id", 0);
				for (XinQing xinqing : lists) {
					if (xinqing.getXinqing_id() == xinqing_id) {
						for (XinQingPraise pr : xinqing.getPraises()) {
							if (pr.getUser_id() == SharedUtils.getIntUid()) {
								xinqing.getPraises().remove(pr);
								break;
							}
						}
						xinqing.setPraise_count(xinqing.getPraise_count() - 1);
						xinqing.setPraise(!xinqing.isPraise());
						adapter.notifyDataSetChanged();
						break;
					}
				}

			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);

	}

}
