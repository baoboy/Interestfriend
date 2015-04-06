package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.activity.XinQingCommentActivity;
import com.interestfriend.data.XinQing;
import com.interestfriend.data.XinQingPraise;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.CancelPraiseXinQingTask;
import com.interestfriend.task.PraiseXinQingTask;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.RoundAngleImageView;

public class XinQingAdapter extends BaseAdapter {
	private List<XinQing> lists = new ArrayList<XinQing>();

	private Context mContext;
	private boolean isTasking = false;

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
			holder.btn_praise = (TextView) convertView
					.findViewById(R.id.btn_prise);
			holder.btn_comment = (TextView) convertView
					.findViewById(R.id.btn_comment);
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
		if (lists.get(position).getComments().size() > 0) {
			holder.btn_comment.setText("»Ø¸´("
					+ lists.get(position).getComments().size() + ")");
		} else {
			holder.btn_comment.setText("»Ø¸´");
		}
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_nomal);
		if (lists.get(position).isPraise()) {
			drawable = mContext.getResources().getDrawable(
					R.drawable.praise_img_focus);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		holder.btn_praise.setCompoundDrawables(drawable, null, null, null);
		if (lists.get(position).getPraise_count() > 0) {
			holder.btn_praise.setText("ÔÞ("
					+ lists.get(position).getPraise_count() + ")");
		} else {
			holder.btn_praise.setText("ÔÞ");
		}
		holder.btn_comment.setOnClickListener(new Onclick(position));
		holder.btn_praise.setOnClickListener(new Onclick(position));
		return convertView;
	}

	class ViewHolder {
		RoundAngleImageView avatar;
		TextView txt_context;
		TextView txt_name;
		TextView txt_time;
		ImageView img;
		TextView btn_praise;
		TextView btn_comment;

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
				intent.putExtra("xinqing", lists.get(position));
				intent.putExtra("position", position);
				intent.setClass(mContext, XinQingCommentActivity.class);
				mContext.startActivity(intent);
				Utils.leftOutRightIn(mContext);
				break;
			case R.id.btn_prise:
				if (isTasking) {
					return;
				}
				XinQing xinqing = lists.get(position);
				if (!xinqing.isPraise()) {
					praise(xinqing, (TextView) v);
				} else {
					cancelPraise(xinqing, (TextView) v);
				}
				break;
			default:
				break;
			}

		}
	}

	private void cancelPraise(final XinQing xinqing, final TextView v) {
		isTasking = true;
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_nomal);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		v.setCompoundDrawables(drawable, null, null, null);
		int count = xinqing.getPraise_count() - 1;
		if (count > 0) {
			v.setText("ÔÞ(" + (xinqing.getPraise_count() - 1) + ")");
		} else {
			v.setText("ÔÞ ");

		}
		CancelPraiseXinQingTask task = new CancelPraiseXinQingTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					for (XinQingPraise pr : xinqing.getPraises()) {
						if (pr.getUser_id() == SharedUtils.getIntUid()) {
							xinqing.getPraises().remove(pr);
							break;
						}
					}
				}
			}
		});
		task.executeParallel(xinqing);
	}

	private void praise(final XinQing xinqing, TextView v) {
		isTasking = true;
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_focus);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		v.setCompoundDrawables(drawable, null, null, null);
		v.setText("ÔÞ(" + (xinqing.getPraise_count() + 1) + ")");
		PraiseXinQingTask task = new PraiseXinQingTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					XinQingPraise pr = new XinQingPraise();
					pr.setXinqing_id(xinqing.getXinqing_id());
					pr.setUser_avatar(SharedUtils.getAPPUserAvatar());
					pr.setUser_id(SharedUtils.getIntUid());
					xinqing.getPraises().add(pr);

				}

			}
		});
		task.executeParallel(xinqing);
	}

}
