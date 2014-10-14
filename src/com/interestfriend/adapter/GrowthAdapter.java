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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.activity.ShowVideoActivity;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.ExpandGridView;

public class GrowthAdapter extends BaseAdapter {
	private List<Growth> lists;

	private Context mContext;

	private LayoutInflater inflater;

	public GrowthAdapter(Context context, List<Growth> lists) {
		this.mContext = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
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
	public int getViewTypeCount() {
		return 4;
	}

	private View createView(int type, int direct) {
		switch (type) {
		case 1:
			System.out.println("type:::::::::::::;===" + type + direct);

			return direct == 1 ? inflater.inflate(R.layout.growth_item_self,
					null) : inflater.inflate(R.layout.growth_item, null);
		case 2:
			System.out.println("type:::::::::::::;" + type + direct);
			return direct == 1 ? inflater.inflate(
					R.layout.growth_video_self_item, null) : inflater.inflate(
					R.layout.growth_video_self_item, null);
		default:
			break;
		}
		return null;

	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		int type = lists.get(position).getType();
		int direct = lists.get(position).getDirect();

		// if (contentView == null) {
		holder = new ViewHolder();
		contentView = createView(type, direct);
		if (type == 1) {
			holder.img = (ImageView) contentView.findViewById(R.id.img);
			holder.txt_context = (TextView) contentView
					.findViewById(R.id.txt_content);
			holder.txt_time = (TextView) contentView
					.findViewById(R.id.txt_time);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_user_name);
			holder.img_grid_view = (ExpandGridView) contentView
					.findViewById(R.id.imgGridview);
			System.out.println("type::::::::::::::;;------" + type);
		}
		if (type == 2) {
			System.out.println("type::::::::::::::;;" + type);
			holder.video_size = (TextView) contentView
					.findViewById(R.id.chatting_size_iv);
			holder.video_timeLength = (TextView) contentView
					.findViewById(R.id.chatting_length_iv);
			holder.playBtn = (ImageView) contentView
					.findViewById(R.id.chatting_status_btn);
			holder.container_status_btn = (LinearLayout) contentView
					.findViewById(R.id.container_status_btn);
			holder.iv = ((ImageView) contentView
					.findViewById(R.id.chatting_content_iv));
		}
		contentView.setTag(holder);
		// } else {
		// holder = (ViewHolder) contentView.getTag();
		// }
		if (type == 1) {
			int imageSize = lists.get(position).getImages().size();
			if (imageSize > 1) {
				if (imageSize > 2) {
					holder.img_grid_view.setNumColumns(3);
				} else {
					holder.img_grid_view.setNumColumns(2);
				}
				holder.img_grid_view.setAdapter(new GrowthImgAdapter(mContext,
						lists.get(position).getImages()));
				holder.img.setVisibility(View.GONE);
				holder.img_grid_view.setVisibility(View.VISIBLE);
			} else if (imageSize == 1) {
				String path = lists.get(position).getImages().get(0).getImg();
				if (!path.startsWith("http")) {
					path = "file://" + path;
				}
				UniversalImageLoadTool.disPlay(path, holder.img,
						R.drawable.empty_photo);
				holder.img.setVisibility(View.VISIBLE);
				holder.img_grid_view.setVisibility(View.GONE);
			} else {
				holder.img.setVisibility(View.GONE);
				holder.img_grid_view.setVisibility(View.GONE);
			}
			holder.img_grid_view
					.setOnItemClickListener(new GridViewOnItemClick(position));
			holder.txt_context.setText(lists.get(position).getContent());
			holder.txt_time.setText(lists.get(position).getPublished());
		} else {
			holder.video_size.setText(lists.get(position).getVideo_size());
			holder.video_timeLength
					.setText(lists.get(position).getVideo_time());
			String path = lists.get(position).getVideo_img();
			if (!path.startsWith("http")) {
				path = "file://" + path;
			}
			UniversalImageLoadTool.disPlay(path, holder.iv,
					R.drawable.empty_photo);
			holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);

			holder.iv.setClickable(true);
			holder.iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext,
							ShowVideoActivity.class);
					intent.putExtra("localpath", lists.get(position)
							.getVideo_path());
					intent.putExtra("secret", "");
					intent.putExtra("remotepath", "");
					mContext.startActivity(intent);
				}
			});

		}
		return contentView;
	}

	class ViewHolder {
		ImageView img_avatar;
		TextView txt_user_name;
		TextView txt_time;
		TextView txt_context;
		ImageView img;
		ExpandGridView img_grid_view;
		ImageView playBtn;
		TextView video_timeLength;
		TextView video_size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv;

	}

	class GridViewOnItemClick implements OnItemClickListener {
		int position;

		public GridViewOnItemClick(int position) {
			this.position = position;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int posit,
				long arg3) {
			List<String> imgUrl = new ArrayList<String>();
			for (GrowthImage img : lists.get(position).getImages()) {
				imgUrl.add(img.getImg());
			}
			Intent intent = new Intent(mContext, ImagePagerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
					(Serializable) imgUrl);
			intent.putExtras(bundle);
			intent.putExtra(Constants.EXTRA_IMAGE_INDEX, posit);
			mContext.startActivity(intent);
		}
	}
}
