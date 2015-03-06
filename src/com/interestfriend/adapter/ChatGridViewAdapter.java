package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;

public class ChatGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private int type = 0;

	public ChatGridViewAdapter(Context context, int type) {
		mContext = context;
		this.type = type;
		initData();
	}

	private void initData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "图片");
		map.put("pic_id", R.drawable.chat_image_normal);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "拍照");
		map.put("pic_id", R.drawable.chat_takepic_normal);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "视频");
		map.put("pic_id", R.drawable.chat_video_normal);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "文件");
		map.put("pic_id", R.drawable.chat_file_normal);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "位置");
		map.put("pic_id", R.drawable.chat_location_noraml);
		list.add(map);
		if (type == 0) {
			map = new HashMap<String, Object>();
			map.put("name", "语音电话");
			map.put("pic_id", R.drawable.chat_voice_call_normal);
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("name", "视频电话");
			map.put("pic_id", R.drawable.chat_video_normal);
			list.add(map);
		}
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.chat_gridview_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img_icon);
			holder.txt_title = (TextView) convertView
					.findViewById(R.id.txt_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setImageResource(Integer.parseInt(list.get(position)
				.get("pic_id").toString()));
		holder.txt_title.setText(list.get(position).get("name").toString());

		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView txt_title;
	}
}
