package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.XinQing;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class XinQingAdapter extends BaseAdapter {
	private List<XinQing> lists = new ArrayList<XinQing>();

	private Context mContext;

	public XinQingAdapter(List<XinQing> lists, Context context) {
		this.mContext = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.xinqing_list_item, null);
			holder = new ViewHolder();
			holder.txt_context = (TextView) convertView
					.findViewById(R.id.txt_content);
			holder.txt_time = (TextView) convertView
					.findViewById(R.id.txt_time);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_user_name);
			holder.avatar = (RoundAngleImageView) convertView
					.findViewById(R.id.img_avatar);
			holder.img = (ImageView) convertView.findViewById(R.id.img);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UniversalImageLoadTool.disPlay(lists.get(position).getImage_url(),
				holder.img, R.drawable.empty_photo);
		UniversalImageLoadTool.disPlay(lists.get(position)
				.getPublisher_avatar(), holder.avatar, R.drawable.empty_photo);
		holder.txt_context.setText(lists.get(position).getContent());
		holder.txt_name.setText(lists.get(position).getPublisher_name());
		holder.txt_time.setText(lists.get(position).getPublish_time());
		return convertView;
	}

	class ViewHolder {
		RoundAngleImageView avatar;
		TextView txt_context;
		TextView txt_name;
		TextView txt_time;
		ImageView img;

	}
}
