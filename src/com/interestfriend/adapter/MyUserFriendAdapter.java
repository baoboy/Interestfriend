package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.ChatUser;
import com.interestfriend.interfaces.ShowBigAvatariListener;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class MyUserFriendAdapter extends BaseAdapter {
	private List<ChatUser> list = new ArrayList<ChatUser>();
	private Context mContext;

	public MyUserFriendAdapter(Context context, List<ChatUser> list) {
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
					R.layout.circle_member_item, null);
			holder = new ViewHolder();
			holder.img_user_avatar = (RoundAngleImageView) contentView
					.findViewById(R.id.img_avatar);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_user_name);
			holder.txt_user_desc = (TextView) contentView
					.findViewById(R.id.txt_user_desc);
			holder.alpha = (TextView) contentView.findViewById(R.id.alpha);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.txt_user_name.setText(list.get(position).getUser_name());
		holder.txt_user_desc.setText(Html.fromHtml("ю╢вт <font color=#19b5ee>"
				+ list.get(position).getUser_friend_circle() + "</font> х╕вс"));
		UniversalImageLoadTool.disPlay(list.get(position).getUser_avatar(),
				holder.img_user_avatar, R.drawable.default_avatar);
		holder.img_user_avatar.setOnClickListener(new ShowBigAvatariListener(
				mContext, list.get(position).getUser_avatar()));
		return contentView;
	}

	static class ViewHolder {
		private RoundAngleImageView img_user_avatar;
		private TextView txt_user_name;
		private TextView txt_user_desc;
		TextView alpha;

	}
}
