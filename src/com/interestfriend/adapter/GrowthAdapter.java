package com.interestfriend.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.activity.CommentActivity;
import com.interestfriend.data.Growth;
import com.interestfriend.data.GrowthImage;
import com.interestfriend.data.Praise;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.OnAvatarClick;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.task.CancelPraiseGrowthTask;
import com.interestfriend.task.PraiseGrowthTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.ExpandGridView;

public class GrowthAdapter extends BaseAdapter {
	private List<Growth> lists;

	private Context mContext;

	private LayoutInflater inflater;
	private boolean isTasking = false;

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
		return 2;
	}

	private View createView(int direct) {
		// return direct == SharedUtils.getIntUid() ? inflater.inflate(
		// R.layout.growth_item_self_1, null) : inflater.inflate(
		// R.layout.growth_item, null);
		return inflater.inflate(R.layout.growth_item_self_1, null);

	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		Growth growth = lists.get(position);
		int direct = lists.get(position).getPublisher_id();
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = createView(direct);
			holder.img = (ImageView) contentView.findViewById(R.id.img);
			holder.txt_context = (TextView) contentView
					.findViewById(R.id.txt_content);
			holder.txt_time = (TextView) contentView
					.findViewById(R.id.txt_time);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_user_name);
			holder.img_avatar = (ImageView) contentView
					.findViewById(R.id.img_avatar);
			holder.img_grid_view = (ExpandGridView) contentView
					.findViewById(R.id.imgGridview);
			holder.btn_comment = (TextView) contentView
					.findViewById(R.id.btn_comment);
			holder.mListView = (ListView) contentView
					.findViewById(R.id.listView1);
			holder.line2 = (View) contentView.findViewById(R.id.line2);
			holder.btn_praise = (TextView) contentView
					.findViewById(R.id.btn_prise);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.btn_comment.setOnClickListener(new Onclick(position));
		holder.btn_praise.setOnClickListener(new Onclick(position));
		if (growth.getComments().size() > 0) {
			holder.btn_comment.setText("»Ø¸´(" + growth.getComments().size()
					+ ")");
		} else {
			holder.btn_comment.setText("»Ø¸´");
		}
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_nomal);
		if (growth.isPraise()) {
			drawable = mContext.getResources().getDrawable(
					R.drawable.praise_img_focus);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		holder.btn_praise.setCompoundDrawables(drawable, null, null, null);
		if (growth.getPraise_count() > 0) {
			holder.btn_praise.setText("ÔÞ(" + growth.getPraise_count() + ")");
		} else {
			holder.btn_praise.setText("ÔÞ");
		}
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
			holder.img.setOnClickListener(new Onclick(position));
		} else {
			holder.img.setVisibility(View.GONE);
			holder.img_grid_view.setVisibility(View.GONE);
		}
		holder.img_grid_view.setOnItemClickListener(new GridViewOnItemClick(
				position));
		String content = lists.get(position).getContent();
		if ("".equals(content)) {
			holder.txt_context.setVisibility(View.GONE);
		} else {
			holder.txt_context.setVisibility(View.VISIBLE);
			holder.txt_context.setText(content);

		}
		holder.txt_time.setText(lists.get(position).getPublished());
		UniversalImageLoadTool.disPlay(growth.getPublisher_avatar(),
				holder.img_avatar, R.drawable.default_avatar);
		holder.txt_user_name.setText(growth.getPublisher_name());
		holder.img_avatar.setOnClickListener(new OnAvatarClick(lists.get(
				position).getPublisher_id(), mContext));
		// holder.mListView.setAdapter(new GrowthListCommentAdapter(mContext,
		// lists.get(position).getCommentsListView()));
		// if (growth.getCommentsListView().size() > 0) {
		// holder.line2.setVisibility(View.VISIBLE);
		// } else {
		// holder.line2.setVisibility(View.GONE);
		// }
		return contentView;
	}

	class ViewHolder {
		ImageView img_avatar;
		TextView txt_user_name;
		TextView txt_time;
		TextView txt_context;
		TextView btn_comment;
		ImageView img;
		ExpandGridView img_grid_view;
		ListView mListView;
		View line2;
		TextView btn_praise;
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
				intent.putExtra("growth", lists.get(position));
				intent.putExtra("position", position);
				intent.setClass(mContext, CommentActivity.class);
				mContext.startActivity(intent);
				Utils.leftOutRightIn(mContext);
				break;
			case R.id.btn_prise:
				if (isTasking) {
					return;
				}
				Growth growth = lists.get(position);
				if (!growth.isPraise()) {
					praise(growth, (TextView) v);
				} else {
					cancelPraise(growth, (TextView) v);
				}
				break;
			default:
				intentImagePager(position, 1);
				break;
			}

		}
	}

	private void cancelPraise(final Growth growth, final TextView v) {
		isTasking = true;
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_nomal);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		v.setCompoundDrawables(drawable, null, null, null);
		v.setText("ÔÞ(" + (growth.getPraise_count() - 1) + ")");
		CancelPraiseGrowthTask task = new CancelPraiseGrowthTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					for (Praise pr : growth.getPraises()) {
						if (pr.getUser_id() == SharedUtils.getIntUid()) {
							growth.getPraises().remove(pr);
							break;
						}
					}
				}
			}
		});
		task.executeParallel(growth);
	}

	private void praise(final Growth growth, TextView v) {
		isTasking = true;
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_focus);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		v.setCompoundDrawables(drawable, null, null, null);
		v.setText("ÔÞ(" + (growth.getPraise_count() + 1) + ")");
		PraiseGrowthTask task = new PraiseGrowthTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					Praise pr = new Praise();
					pr.setGrowth_id(growth.getGrowth_id());
					pr.setUser_avatar(SharedUtils.getAPPUserAvatar());
					pr.setUser_id(SharedUtils.getIntUid());
					growth.getPraises().add(pr);

				}

			}
		});
		task.executeParallel(growth);
	}

	class GridViewOnItemClick implements OnItemClickListener {
		int position;

		public GridViewOnItemClick(int position) {
			this.position = position;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int posit,
				long arg3) {
			intentImagePager(position, posit);
		}
	}

	private void intentImagePager(int position, int index) {
		List<String> imgUrl = new ArrayList<String>();
		for (GrowthImage img : lists.get(position).getImages()) {
			imgUrl.add(img.getImg());
		}
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
		mContext.startActivity(intent);
	}
}
