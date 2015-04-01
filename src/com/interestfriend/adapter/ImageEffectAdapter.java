package com.interestfriend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.utils.GPUImageFilterTools.FilterList;
import com.interestfriend.view.RoundAngleImageView;

public class ImageEffectAdapter extends BaseAdapter {
	private Context mContext;
	private FilterList filters = new FilterList();
	private int select_index;

	public ImageEffectAdapter(Context mContext, FilterList filters) {
		this.mContext = mContext;
		this.filters = filters;
	}

	public void setSelect_index(int select_index) {
		this.select_index = select_index;
	}

	@Override
	public int getCount() {
		return filters.names.size();
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
	public View getView(int position, View conventView, ViewGroup arg2) {
		ViewHolder holder;
		if (conventView == null) {
			conventView = LayoutInflater.from(mContext).inflate(
					R.layout.image_effect_item, null);
			holder = new ViewHolder();
			holder.img = (RoundAngleImageView) conventView
					.findViewById(R.id.image);
			holder.txt = (TextView) conventView.findViewById(R.id.txt);
			conventView.setTag(holder);
		} else {
			holder = (ViewHolder) conventView.getTag();
		}
		holder.txt.setText(filters.names.get(position));
		switch (position) {
		case 0:
			holder.img.setImageResource(R.drawable.img_yuantu);
			break;
		case 1:
			holder.img.setImageResource(R.drawable.img_lomo);
			break;
		case 2:
			holder.img.setImageResource(R.drawable.img_jiuishiguagn);
			break;
		case 3:
			holder.img.setImageResource(R.drawable.img_weimei);
			break;
		case 4:
			holder.img.setImageResource(R.drawable.img_weiyena);
			break;
		case 5:
			holder.img.setImageResource(R.drawable.img_huiyi);
			break;
		case 6:
			holder.img.setImageResource(R.drawable.img_heibai);
			break;
		case 7:
			holder.img.setImageResource(R.drawable.img_abaose);
			break;
		case 8:
			holder.img.setImageResource(R.drawable.img_nonghou);
			break;
		default:
			break;
		}
		if (position == select_index) {
			holder.img.setBackgroundResource(R.drawable.image_effect_bg);
			holder.img.setPadding(3, 3, 3, 3);
			holder.txt.setTextColor(mContext.getResources().getColor(
					R.color.white));
		} else {
			holder.img.setBackgroundResource(0);
			holder.img.setPadding(0, 0, 0, 0);
			holder.txt.setTextColor(mContext.getResources().getColor(
					R.color.image_effect_text_color));
		}
		return conventView;
	}

	class ViewHolder {
		RoundAngleImageView img;
		TextView txt;
	}
}
