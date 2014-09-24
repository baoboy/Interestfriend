package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.location.ad;
import com.interestfriend.R;
import com.interestfriend.activity.PublicshGrowthActivity;
import com.interestfriend.adapter.GrowthAdapter;
import com.interestfriend.data.Growth;

@SuppressLint("NewApi")
public class CircleGrowthFragment extends Fragment implements OnClickListener {

	private ImageView img_send;
	private ListView growth_listView;

	private GrowthAdapter adapter;

	private List<Growth> lists = new ArrayList<Growth>();

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
		lists.add(0, growth);
		adapter.notifyDataSetChanged();
	}
}
