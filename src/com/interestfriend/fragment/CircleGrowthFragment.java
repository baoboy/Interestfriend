package com.interestfriend.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.interestfriend.R;
import com.interestfriend.activity.ChatActivity;
import com.interestfriend.activity.ImageGridActivity;
import com.interestfriend.activity.PublicshGrowthActivity;
import com.interestfriend.activity.PublishVideoActivity;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.Growth;
import com.interestfriend.data.Video;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.popwindow.SelectPicPopwindow.SelectOnclick;
import com.interestfriend.utils.DateUtils;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

public class CircleGrowthFragment extends Fragment implements
		RadioGroup.OnCheckedChangeListener, OnClickListener, SelectOnclick {
	private ImageFragment imgFragment;
	private VideoFragment videoFragment;

	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	private int currentTabIndex = -1;

	private ImageView img_send;

	private SelectPicPopwindow pop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.growth_frament_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initFragment();
		initView();
	}

	private void initView() {
		img_send = (ImageView) getView().findViewById(R.id.rightImg);
		setListener();
	}

	private void setListener() {
		img_send.setOnClickListener(this);
	}

	private void initFragment() {
		imgFragment = new ImageFragment();
		videoFragment = new VideoFragment();
		fragmentList.add(imgFragment);
		fragmentList.add(videoFragment);
		RadioGroup rg = (RadioGroup) getActivity().findViewById(R.id.tab_group);
		rg.setOnCheckedChangeListener(this);
		showTab(0, rg);

	}

	@SuppressLint("ResourceAsColor")
	public void showTab(int tabIndex, RadioGroup group) {
		if (tabIndex < 0 || tabIndex >= group.getChildCount())
			return;
		if (currentTabIndex == tabIndex)
			return;
		if (currentTabIndex >= 0) {
			fragmentList.get(currentTabIndex).onPause();
		}
		FragmentTransaction ft = this.getChildFragmentManager()
				.beginTransaction();
		for (int i = 0; i < group.getChildCount(); i++) {
			Fragment fg = fragmentList.get(i);
			RadioButton tabItem = (RadioButton) group.getChildAt(i);
			if (i == tabIndex) {
				if (fg.isAdded()) {
					fg.onResume();
				} else {
					ft.add(R.id.realtabcontent, fg);
				}
				tabItem.setTextColor(R.color.blue);
				ft.show(fg);
			} else {
				ft.hide(fg);
				tabItem.setTextColor(Color.WHITE);

			}
		}
		ft.commit();
		currentTabIndex = tabIndex;
		if (currentTabIndex == 0) {
			group.setBackgroundResource(R.drawable.tab_bg1_growth);
		} else {
			group.setBackgroundResource(R.drawable.tab_bg_growth);
		}
		RadioButton rb = (RadioButton) group.getChildAt(tabIndex);
		if (!rb.isChecked())
			rb.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
			if (group.getChildAt(i).getId() == checkedId) {
				showTab(i, group);
				break;
			}
		}
	}

	@Override
	public void menu1_select() {
		startActivityForResult(new Intent(getActivity(),
				PublicshGrowthActivity.class), 200);
		Utils.leftOutRightIn(getActivity());
	}

	@Override
	public void menu2_select() {
		Intent intent = new Intent(getActivity(), ImageGridActivity.class);
		startActivityForResult(intent, ChatActivity.REQUEST_CODE_SELECT_VIDEO);
		Utils.leftOutRightIn(getActivity());
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
			growth.setPublisher_id(SharedUtils.getIntUid());
			growth.setCid(MyApplation.getCircle_id());
			growth.setPublished(DateUtils.getGrowthShowTime());
			growth.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
			growth.setPublisher_name(SharedUtils.getAPPUserName());
			imgFragment.refushAdapter(growth);
		} else if (requestCode == ChatActivity.REQUEST_CODE_SELECT_VIDEO) {
			Intent intent = data;
			data.setClass(getActivity(), PublishVideoActivity.class);
			startActivityForResult(intent, 300);
			Utils.leftOutRightIn(getActivity());

		} else if (requestCode == 300) {
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
			Video video = new Video();
			video.setCid(MyApplation.getCircle_id());
			video.setDirect(1);
			video.setVideo_img(file.getAbsolutePath());
			video.setVideo_duration(duration / 1000);
			video.setVideo_duration(duration);
			video.setVideo_size((int) new File(videoPath).length());
			video.setVideo_path(videoPath);
			video.setPublisher_id(SharedUtils.getIntUid());
			video.setCid(MyApplation.getCircle_id());
			video.setTime(DateUtils.getGrowthShowTime());
			video.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
			video.setPublisher_name(SharedUtils.getAPPUserName());
			videoFragment.refushAdapter(video);
		}
	}

}
