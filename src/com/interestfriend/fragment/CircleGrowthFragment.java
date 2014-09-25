package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.activity.PublicshGrowthActivity;
import com.interestfriend.adapter.GrowthAdapter;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetGrowthListTask;
import com.interestfriend.task.UpLoadGrowthTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

@SuppressLint("NewApi")
public class CircleGrowthFragment extends Fragment implements OnClickListener {

	private ImageView img_send;
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
		getGrowthFromServer();
	}

	private void initView() {
		img_send = (ImageView) getView().findViewById(R.id.img_send);
		growth_listView = (ListView) getView().findViewById(
				R.id.growth_listView);
		setListener();
	}

	private void setValue() {
		adapter = new GrowthAdapter(getActivity(), lists);
		growth_listView.setAdapter(adapter);
	}

	private void setListener() {
		img_send.setOnClickListener(this);
	}

	private void getGrowthFromServer() {
		dialog = DialogUtil.createLoadingDialog(getActivity(), "���Ժ�");
		dialog.show();
		GetGrowthListTask task = new GetGrowthListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("��ȡʧ��", Toast.LENGTH_SHORT);
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
		case R.id.img_send:
			startActivityForResult(new Intent(getActivity(),
					PublicshGrowthActivity.class), 200);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != 200 || data == null) {
			return;
		}
		Growth growth = (Growth) data.getSerializableExtra("growth");
		growth.setTag(System.currentTimeMillis() + "");
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
					ToastUtil.showToast("����ʧ��", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("�����ɹ�", Toast.LENGTH_SHORT);
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(growth);
	}
}
