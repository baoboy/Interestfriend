package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.activity.CircleMemberInfoActivity;
import com.interestfriend.adapter.CircleMemberAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.CircleMemberList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetCircleMemberTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

@SuppressLint("NewApi")
public class CircleMemberFragment extends Fragment implements
		OnItemClickListener {
	private int circle_id = 0;

	private CircleMemberList list;

	private Dialog dialog;

	private List<CircleMember> cirlceMemberLists = new ArrayList<CircleMember>();

	private CircleMemberAdapter adapter;

	private ListView circle_member_listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.circle_member_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		circle_id = MyApplation.getCircle_id();
		list = new CircleMemberList();
		list.setCid(circle_id);
		initView();
		setValue();
		getCircleMemberFormServer();
	}

	private void initView() {
		circle_member_listView = (ListView) getView().findViewById(
				R.id.circle_member_listview);
		setListener();
	}

	private void setListener() {
		circle_member_listView.setOnItemClickListener(this);
	}

	private void setValue() {
		adapter = new CircleMemberAdapter(getActivity(), cirlceMemberLists);
		circle_member_listView.setAdapter(adapter);
	}

	private void getCircleMemberFormServer() {
		dialog = DialogUtil.createLoadingDialog(getActivity(), "«Î…‘∫Ú");
		GetCircleMemberTask task = new GetCircleMemberTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {

			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("ªÒ»° ß∞‹", Toast.LENGTH_LONG);
					return;
				}
				cirlceMemberLists.addAll(list.getCircleMemberLists());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(list);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("circle_ember", cirlceMemberLists.get(position));
		intent.setClass(getActivity(), CircleMemberInfoActivity.class);
		startActivity(intent);
	}
}
