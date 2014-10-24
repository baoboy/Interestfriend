package com.interestfriend.fragment;

import java.util.ArrayList;
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
import com.interestfriend.adapter.GrowthAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.Comment;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetGrowthFormDBTask;
import com.interestfriend.task.GetGrowthListTask;
import com.interestfriend.task.UpLoadGrowthTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.view.PullDownView;
import com.interestfriend.view.PullDownView.OnPullDownListener;

@SuppressLint("NewApi")
public class ImageFragment extends Fragment implements OnPullDownListener {
	private PullDownView mPullDownView;

	private ListView growth_listView;

	private GrowthAdapter adapter;

	private List<Growth> lists = new ArrayList<Growth>();

	private GrowthList glist;

	private Dialog dialog;

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
		glist = new GrowthList(MyApplation.getCircle_id());
		getGrowthFromDB();
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
		adapter = new GrowthAdapter(getActivity(), lists);
		growth_listView.setAdapter(adapter);
		mPullDownView.addFooterView();

	}

	private void setListener() {
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.notifyDidMore();
		mPullDownView.setFooterVisible(false);

	}

	private void getGrowthFromDB() {
		dialog = DialogUtil.createLoadingDialog(getActivity(), "请稍候");
		dialog.show();
		GetGrowthFormDBTask task = new GetGrowthFormDBTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				lists.addAll(glist.getGrowths());
				adapter.notifyDataSetChanged();
				if (lists.size() == 0) {
					getGrowthFromServer();
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
		task.execute(glist);
	}

	private void getGrowthFromServer() {

		GetGrowthListTask task = new GetGrowthListTask();
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

				lists.clear();
				lists.addAll(glist.getGrowths());
				adapter.notifyDataSetChanged();
				if (lists.size() > 19) {
					mPullDownView.setFooterVisible(true);
				} else {
					mPullDownView.setFooterVisible(false);
				}
			}
		});
		task.execute(glist);
	}

	public void refushAdapter(Growth growth) {
		lists.add(0, growth);
		glist.getGrowths().add(0, growth);
		adapter.notifyDataSetChanged();
		growth_listView.setSelection(0);
		upLoadGrowth(growth);
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
	public void onRefresh() {
		if (lists.size() == 0) {
			mPullDownView.RefreshComplete();
			return;
		}
		glist.setRefushState(1);
		glist.setRefushTime(lists.get(0).getPublished());
		getGrowthFromServer();
	}

	@Override
	public void onMore() {
		glist.setRefushState(2);
		glist.setRefushTime(lists.get(lists.size() - 1).getPublished());
		getGrowthFromServer();
	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.COMMENT_GROWTH);

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
			if (action.equals(Constants.COMMENT_GROWTH)) {
				Comment coment = (Comment) intent
						.getSerializableExtra("commeent");
				int position = intent.getIntExtra("position", -1);
				List<Comment> commentsListView = lists.get(position)
						.getCommentsListView();
				if (commentsListView.size() > 0) {
					commentsListView.remove(0);
				}
				commentsListView.add(0, coment);

			}
		}
	};

	public void onDestroy() {
		super.onDestroy();
	};
}
