package com.interestfriend.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.interestfriend.R;
import com.interestfriend.activity.ChatActivity;
import com.interestfriend.activity.ImageGridActivity;
import com.interestfriend.activity.PublicshGrowthActivity;
import com.interestfriend.adapter.GrowthAdapter;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.task.GetGrowthFormDBTask;
import com.interestfriend.task.GetGrowthListTask;
import com.interestfriend.task.UpLoadGrowthTask;
import com.interestfriend.task.UpLoadVideoTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

@SuppressLint("NewApi")
public class ImageFragment extends Fragment implements OnClickListener,
		SelectOnclick {

	// private ImageView img_send;
	private ListView growth_listView;

	private GrowthAdapter adapter;

	private List<Growth> lists = new ArrayList<Growth>();

	private GrowthList glist;

	private Dialog dialog;

	private SelectPicPopwindow pop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.circle_growth, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setValue();
		glist = new GrowthList();
		getGrowthFromDB();
		// getGrowthFromServer();
	}

	private void initView() {
		// img_send = (ImageView) getView().findViewById(R.id.rightImg);
		// img_send.setVisibility(View.VISIBLE);
		// img_send.setImageResource(R.drawable.growth_send_tie_main_img);
		growth_listView = (ListView) getView().findViewById(
				R.id.growth_listView);
		setListener();
	}

	private void setValue() {
		adapter = new GrowthAdapter(getActivity(), lists);
		growth_listView.setAdapter(adapter);
	}

	private void setListener() {
		// img_send.setOnClickListener(this);
	}

	private void getGrowthFromDB() {
		dialog = DialogUtil.createLoadingDialog(getActivity(), "请稍候");
		dialog.show();
		GetGrowthFormDBTask task = new GetGrowthFormDBTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				lists.addAll(glist.getGrowths());
				if (lists.size() == 0) {
					getGrowthFromServer();
				} else {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				adapter.notifyDataSetChanged();

			}
		});
		task.execute(glist);
	}

	private void getGrowthFromServer() {

		GetGrowthListTask task = new GetGrowthListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("获取失败", Toast.LENGTH_SHORT);
					return;
				}
				lists.addAll(glist.getGrowths());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(glist);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rightImg:
			pop = new SelectPicPopwindow(getActivity(), v, "发布图片", "发布视频");
			pop.setmSelectOnclick(this);
			pop.show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (requestCode == 200) {
			Growth growth = (Growth) data.getSerializableExtra("growth");
			growth.setTag(System.currentTimeMillis() + "");
			lists.add(0, growth);
			adapter.notifyDataSetChanged();
			upLoadGrowth(growth);
		} else if (requestCode == ChatActivity.REQUEST_CODE_SELECT_VIDEO) {
			int duration = data.getIntExtra("dur", 0);
			String videoPath = data.getStringExtra("path");
			File file = new File(PathUtil.getInstance().getImagePath(),
					"thvideo" + System.currentTimeMillis());
			Bitmap bitmap = null;
			FileOutputStream fos = null;
			try {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
				if (bitmap == null) {
					EMLog.d("chatactivity",
							"problem load video thumbnail bitmap,use default icon");
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.app_panel_video_icon);
				}
				fos = new FileOutputStream(file);

				bitmap.compress(CompressFormat.JPEG, 100, fos);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fos = null;
				}
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}

			}
			Growth growth = new Growth();
			growth.setDirect(1);
			growth.setType(2);
			growth.setTag(System.currentTimeMillis() + "");
			growth.setVideo_img(file.getAbsolutePath());
			growth.setVideo_duration(duration / 1000);
			growth.setVideo_duration(duration);
			growth.setVideo_size((int) new File(videoPath).length());
			growth.setVideo_path(videoPath);
			lists.add(0, growth);
			adapter.notifyDataSetChanged();
			upLoadVideo(growth);
		}
	}

	private void upLoadVideo(final Growth growth) {
		UpLoadVideoTask task = new UpLoadVideoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (result != RetError.NONE) {
					ToastUtil.showToast("发布失败", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("发布成功", Toast.LENGTH_SHORT);
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(growth);
	}

	private void upLoadGrowth(final Growth growth) {
		UpLoadGrowthTask task = new UpLoadGrowthTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (result != RetError.NONE) {
					ToastUtil.showToast("发布失败", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("发布成功", Toast.LENGTH_SHORT);
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(growth);
	}

	@Override
	public void menu1_select() {
		startActivityForResult(new Intent(getActivity(),
				PublicshGrowthActivity.class), 200);
	}

	@Override
	public void menu2_select() {
		Intent intent = new Intent(getActivity(), ImageGridActivity.class);
		startActivityForResult(intent, ChatActivity.REQUEST_CODE_SELECT_VIDEO);
	}
}
