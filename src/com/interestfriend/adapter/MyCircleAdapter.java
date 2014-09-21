package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.MyCircles;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class MyCircleAdapter extends BaseAdapter {
	private List<MyCircles> list = new ArrayList<MyCircles>();
	private Context mContext;

	public MyCircleAdapter(Context context, List<MyCircles> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = LayoutInflater.from(mContext).inflate(
					R.layout.circle_item, null);
			holder = new ViewHolder();
			holder.img_circle_logo = (RoundAngleImageView) contentView
					.findViewById(R.id.img_circle_logo);
			holder.txt_circle_name = (TextView) contentView
					.findViewById(R.id.txt_circle_name);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.txt_circle_name.setText(list.get(position).getCircle_name());
		UniversalImageLoadTool.disPlay(list.get(position).getCircle_logo(),
				holder.img_circle_logo, R.drawable.picture_default_head);
		return contentView;
	}

	static class ViewHolder {
		private RoundAngleImageView img_circle_logo;
		private TextView txt_circle_name;
	}
}
