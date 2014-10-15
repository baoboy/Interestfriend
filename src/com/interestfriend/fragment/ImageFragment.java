package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.GrowthAdapter;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.SelectPicPopwindow;
import com.interestfriend.task.GetGrowthFormDBTask;
import com.interestfriend.task.GetGrowthListTask;
import com.interestfriend.task.UpLoadGrowthTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

@SuppressLint("NewApi")
public class ImageFragment extends Fragment {

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
		glist = new GrowthList();
		getGrowthFromDB();
		// getGrowthFromServer();
	}

	private void initView() {
		growth_listView = (ListView) getView().findViewById(
				R.id.growth_listView);
		setListener();
	}

	private void setValue() {
		adapter = new GrowthAdapter(getActivity(), lists);
		growth_listView.setAdapter(adapter);
	}

	private void setListener() {
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

	public void refushAdapter(Growth growth) {
		lists.add(0, growth);
		adapter.notifyDataSetChanged();
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
}
