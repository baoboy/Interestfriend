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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.easemob.util.TextFormater;
import com.interestfriend.R;
import com.interestfriend.activity.ShowVideoActivity;
import com.interestfriend.activity.VideoCommentActivity;
import com.interestfriend.data.Video;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;

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
		return direct == SharedUtils.getIntUid() ? inflater.inflate(
				R.layout.growth_video_self_item, null) : inflater.inflate(
				R.layout.growth_video_item, null);

	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		int direct = lists.get(position).getPublisher_id();
		Video video = lists.get(position);

		if (contentView == null) {
			holder = new ViewHolder();
			contentView = createView(direct);

			holder.video_size = (TextView) contentView
					.findViewById(R.id.chatting_size_iv);
			holder.video_timeLength = (TextView) contentView
					.findViewById(R.id.chatting_length_iv);
			holder.txt_context = (TextView) contentView
					.findViewById(R.id.txt_content);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_user_name);
			holder.img_avatar = (ImageView) contentView
					.findViewById(R.id.img_avatar);
			holder.txt_time = (TextView) contentView
					.findViewById(R.id.txt_time);
			holder.btn_comment = (TextView) contentView
					.findViewById(R.id.btn_comment);
			holder.mListView = (ListView) contentView
					.findViewById(R.id.listView1);
			holder.video_img = (ImageView) contentView
					.findViewById(R.id.video_img);
			holder.videoClick = (RelativeLayout) contentView
					.findViewById(R.id.ll_click_area);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.btn_comment.setOnClickListener(new Onclick(position));
		if (video.getComments().size() > 0) {
			holder.btn_comment
					.setText("»Ø¸´(" + video.getComments().size() + ")");
		} else {
			holder.btn_comment.setText("»Ø¸´");
		}
		UniversalImageLoadTool.disPlay(video.getPublisher_avatar(),
				holder.img_avatar, R.drawable.default_avatar);
		String content = lists.get(position).getVideo_txt_content();
		if ("".equals(content)) {
			holder.txt_context.setVisibility(View.GONE);
		} else {
			holder.txt_context.setVisibility(View.VISIBLE);
			holder.txt_context.setText(content);
		}
		holder.txt_user_name.setText(video.getPublisher_name());
		holder.video_timeLength.setText(DateUtils.toTime(lists.get(position)
				.getVideo_duration()));
		holder.video_size.setText(TextFormater.getDataSize(Long.valueOf(lists
				.get(position).getVideo_size())));
		holder.txt_time.setText(lists.get(position).getTime());
		String path = lists.get(position).getVideo_img();
		if (!path.startsWith("http")) {
			path = "file://" + path;
		}
		UniversalImageLoadTool.disPlay(path, holder.video_img,
				R.drawable.empty_photo);

		holder.videoClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, ShowVideoActivity.class);
				String path = lists.get(position).getVideo_path();
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
		holder.mListView.setAdapter(new VideoListCommentAdapter(mContext, lists
				.get(position).getCommentsListView()));
		return contentView;
	}

	class ViewHolder {
		ImageView img_avatar;
		TextView txt_time;
		TextView txt_user_name;
		TextView txt_context;
		TextView video_timeLength;
		TextView video_size;
		ProgressBar pg;
		TextView btn_comment;
		ListView mListView;
		ImageView video_img;
		RelativeLayout videoClick;
	}

	class Onclick implements OnClickListener {
		int position;

		public Onclick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_comment:
				Intent intent = new Intent();
				intent.putExtra("video", lists.get(position));
				intent.putExtra("position", position);
				intent.setClass(mContext, VideoCommentActivity.class);
				mContext.startActivity(intent);
				Utils.leftOutRightIn(mContext);
				break;
			default:
				break;
			}

		}

	}
}
