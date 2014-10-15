package com.interestfriend.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.easemob.util.TextFormater;
import com.interestfriend.R;
import com.interestfriend.activity.ShowVideoActivity;
import com.interestfriend.data.Video;
import com.interestfriend.utils.UniversalImageLoadTool;

public class GrowthVideoAdapter extends BaseAdapter {
	private List<Video> lists;

	private Context mContext;

	private LayoutInflater inflater;

	public GrowthVideoAdapter(Context context, List<Video> lists) {
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
		return 2;
	}

	private View createView(int direct) {
		return direct == 1 ? inflater.inflate(R.layout.growth_video_self_item,
				null) : inflater.inflate(R.layout.growth_video_self_item, null);

	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		int direct = lists.get(position).getDirect();

		if (contentView == null) {
			holder = new ViewHolder();
			contentView = createView(direct);

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

			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		holder.video_timeLength.setText(DateUtils.toTime(lists.get(position)
				.getVideo_duration()));
		holder.video_size.setText(TextFormater.getDataSize(Long.valueOf(lists
				.get(position).getVideo_size())));
		String path = lists.get(position).getVideo_img();
		if (!path.startsWith("http")) {
			path = "file://" + path;
		}
		UniversalImageLoadTool.disPlay(path, holder.iv, R.drawable.empty_photo);
		holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);

		holder.iv.setClickable(true);
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, ShowVideoActivity.class);
				String path = lists.get(position).getVideo_path();
				System.out.println("path::::::::::::::;;" + path);
				if (path.startsWith("http")) {
					intent.putExtra("remotepath", path);
					intent.putExtra("localpath", "");

				} else {
					intent.putExtra("localpath", path);
					intent.putExtra("remotepath", "");

				}
				intent.putExtra("secret", "");
				mContext.startActivity(intent);
			}
		});

		return contentView;
	}

	class ViewHolder {
		ImageView img_avatar;
		TextView txt_time;
		TextView txt_context;
		ImageView playBtn;
		TextView video_timeLength;
		TextView video_size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv;
		ProgressBar pg;

	}

}
