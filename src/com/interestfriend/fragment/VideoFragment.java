package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.GrowthVideoAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.Video;
import com.interestfriend.data.VideoComment;
import com.interestfriend.data.VideoList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetVideoFromDBTask;
import com.interestfriend.task.GetVideoListTask;
import com.interestfriend.task.UpLoadVideoTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.view.PullDownView;
import com.interestfriend.view.PullDownView.OnPullDownListener;

@SuppressLint("NewApi")
public class VideoFragment extends Fragment implements OnPullDownListener {
	private ListView growth_listView;
	private PullDownView mPullDownView;

	private GrowthVideoAdapter adapter;

	private List<Video> lists = new ArrayList<Video>();

	private Dialog dialog;

	private VideoList list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.circle_growth, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		list = new VideoList(MyApplation.getCircle_id());
		initView();
		setValue();
		registerBoradcastReceiver();
	}

	private void initView() {
		mPullDownView = (PullDownView) getView().findViewById(
				R.id.PullDownlistView);
		growth_listView = mPullDownView.getListView();
		growth_listView.setCacheColorHint(0);
		growth_listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		setListener();
	}

	private void setValue() {
		adapter = new GrowthVideoAdapter(getActivity(), lists);
		growth_listView.setAdapter(adapter);
		mPullDownView.addFooterView();
		getVideoFromDB();
	}

	private void setListener() {
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.notifyDidMore();
		mPullDownView.setFooterVisible(false);

	}

	private void getVideoFromDB() {
		dialog = DialogUtil.createLoadingDialog(getActivity(), "请稍候");
		dialog.show();
		GetVideoFromDBTask task = new GetVideoFromDBTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				lists.addAll(list.getVideos());
				adapter.notifyDataSetChanged();
				if (lists.size() == 0) {
					getVideoFromServer();
				} else {
					if (dialog != null) {
						dialog.dismiss();
					}
					if (lists.size() > 19) {
						mPullDownView.setFooterVisible(true);
					} else {
						mPullDownView.setFooterVisible(false);

					}
				}

			}
		});
		task.executeParallel(list);
	}

	private void sort() {
		Collections.sort(lists, new Comparator<Video>() {
			@Override
			public int compare(Video lhs, Video rhs) {
				return rhs.getTime().compareTo(lhs.getTime());
			}
		});
	}

	private void getVideoFromServer() {

		GetVideoListTask task = new GetVideoListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				mPullDownView.RefreshComplete();
				mPullDownView.notifyDidMore();
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("获取失败", Toast.LENGTH_SHORT);
					return;
				}
				if (list.getVideos().size() == 0) {
					return;
				}
				lists.clear();
				lists.addAll(list.getVideos());
				adapter.notifyDataSetChanged();
				if (lists.size() > 19) {
					mPullDownView.setFooterVisible(true);
				} else {
					mPullDownView.setFooterVisible(false);

				}
			}
		});
		task.executeParallel(list);
	}

	public void refushAdapter(Video video) {
		lists.add(0, video);
		list.getVideos().add(0, video);
		adapter.notifyDataSetChanged();
		upLoadVideo(video);
	}

	private void upLoadVideo(final Video video) {
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
		task.executeParallel(video);
	}

	@Override
	public void onRefresh() {
		if (lists.size() == 0) {
			mPullDownView.RefreshComplete();
			return;
		}
		list.setRefushState(1);
		list.setRefushTime(lists.get(0).getTime());
		getVideoFromServer();
	}

	@Override
	public void onMore() {
		list.setRefushState(2);
		list.setRefushTime(lists.get(lists.size() - 1).getTime());
		getVideoFromServer();
	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.COMMENT_VIDEO);

		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 定义广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.COMMENT_VIDEO)) {
				VideoComment coment = (VideoComment) intent
						.getSerializableExtra("comment");
				int position = intent.getIntExtra("position", -1);
				List<VideoComment> commentsListView = lists.get(position)
						.getCommentsListView();
				if (commentsListView.size() > 1) {
					commentsListView.remove(0);
				}
				commentsListView.add(0, coment);
				lists.get(position).getComments().add(0, coment);
				adapter.notifyDataSetChanged();
			}
		}
	};

	public void onDestroy() {
		super.onDestroy();
	};
}
