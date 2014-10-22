package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
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
import com.interestfriend.contentprovider.MyCircleVideoProvider;
import com.interestfriend.data.Video;
import com.interestfriend.data.VideoList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetVideoListTask;
import com.interestfriend.task.UpLoadVideoTask;
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

	private AsyncQueryHandler asyncQuery;

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
		// getVideoFromServer();
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
		initQuery();
	}

	private void setListener() {
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.notifyDidMore();
		mPullDownView.setFooterVisible(false);

	}

	private void initQuery() {
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		String[] projection = { MyCircleVideoProvider.MyCircleVideoColumns.CID,
				MyCircleVideoProvider.MyCircleVideoColumns.PUBLISHER_ID,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_DURATION,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_ID,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_IMG,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_PATH,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_SIZE,
				MyCircleVideoProvider.MyCircleVideoColumns.TIME }; // 查询的列
		asyncQuery.startQuery(0, null,
				MyCircleVideoProvider.MyCircleVideoColumns.CONTENT_URI,
				projection, MyCircleVideoProvider.MyCircleVideoColumns.CID
						+ "=?",
				new String[] { MyApplation.getCircle_id() + "" }, null);
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie,
				final Cursor cursor) {
			if (cursor == null) {
				return;
			}
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					Video video = new Video();
					video.setCid(cursor.getInt(0));
					video.setPublisher_id(cursor.getInt(1));
					video.setVideo_duration(cursor.getInt(2));
					video.setVideo_id(cursor.getInt(3));
					video.setVideo_img(cursor.getString(4));
					video.setVideo_path(cursor.getString(5));
					video.setVideo_size(cursor.getInt(6));
					video.setTime(cursor.getString(7));
					lists.add(video);
					cursor.moveToNext();
				}
				sort();
				adapter.notifyDataSetChanged();
				if (lists.size() > 19) {
					mPullDownView.setFooterVisible(true);
				} else {
					mPullDownView.setFooterVisible(false);

				}
			} else {
				// dialog = DialogUtil.createLoadingDialog(getActivity(),
				// "请稍候");
				// dialog.show();
				getVideoFromServer();
			}
		}
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
		task.execute(list);
	}

	public void refushAdapter(Video video) {
		lists.add(0, video);
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
		task.execute(video);
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
}
