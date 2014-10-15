package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
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
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Video;
import com.interestfriend.data.VideoList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetVideoListTask;
import com.interestfriend.task.UpLoadVideoTask;
import com.interestfriend.utils.ToastUtil;

@SuppressLint("NewApi")
public class VideoFragment extends Fragment {
	private ListView growth_listView;

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
		list = new VideoList();
		list.setCid(MyApplation.getCircle_id());
		initView();
		setValue();
		// getVideoFromServer();
	}

	private void initView() {
		growth_listView = (ListView) getView().findViewById(
				R.id.growth_listView);
		setListener();
	}

	private void setValue() {
		adapter = new GrowthVideoAdapter(getActivity(), lists);
		growth_listView.setAdapter(adapter);
		initQuery();
	}

	private void setListener() {
	}

	private void initQuery() {
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		String[] projection = { MyCircleVideoProvider.MyCircleVideoColumns.CID,
				MyCircleVideoProvider.MyCircleVideoColumns.PUBLISHER_ID,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_DURATION,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_ID,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_IMG,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_PATH,
				MyCircleVideoProvider.MyCircleVideoColumns.VIDEO_SIZE, }; // 查询的列
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
					lists.add(video);
					cursor.moveToNext();
				}
				adapter.notifyDataSetChanged();
			} else {
				// dialog = DialogUtil.createLoadingDialog(getActivity(),
				// "请稍候");
				// dialog.show();
				getVideoFromServer();
			}
		}
	}

	private void getVideoFromServer() {

		GetVideoListTask task = new GetVideoListTask();
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
				lists.addAll(list.getVideos());
				adapter.notifyDataSetChanged();
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
}
