package com.interestfriend.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.CategoryCircle;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class CategoryCircleAdapter extends BaseAdapter {
	private List<CategoryCircle> list = new ArrayList<CategoryCircle>();
	private Context mContext;

	public CategoryCircleAdapter(Context context, List<CategoryCircle> list) {
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
					R.layout.circle_category_item, null);
			holder = new ViewHolder();
			holder.img_circle_logo = (RoundAngleImageView) contentView
					.findViewById(R.id.img_circle_logo);
			holder.txt_circle_name = (TextView) contentView
					.findViewById(R.id.txt_circle_name);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.txt_circle_name.setText(list.get(position).getName());
		UniversalImageLoadTool.disPlay(list.get(position).getLogo(),
				holder.img_circle_logo, R.drawable.default_avatar);
		holder.img_circle_logo.setOnClickListener(new AvatarClick(position));
		return contentView;
	}

	static class ViewHolder {
		private RoundAngleImageView img_circle_logo;
		private TextView txt_circle_name;
	}

	class AvatarClick implements OnClickListener {
		int position;

		public AvatarClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			List<String> imgUrl = new ArrayList<String>();
			imgUrl.add(list.get(position).getLogo());
			Intent intent = new Intent(mContext, ImagePagerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
					(Serializable) imgUrl);
			intent.putExtras(bundle);
			intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
			mContext.startActivity(intent);
		}

	}
}
