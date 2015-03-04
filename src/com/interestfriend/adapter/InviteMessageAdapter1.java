package com.interestfriend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.interfaces.ShowBigAvatariListener;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class InviteMessageAdapter1 extends BaseAdapter {
	private Context mContext;

	private EMConversation conversation;

	public InviteMessageAdapter1(Context context, EMConversation conversation) {
		this.mContext = context;
		this.conversation = conversation;
	}

	@Override
	public int getCount() {
		return conversation.getMsgCount();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public EMMessage getItem(int position) {
		return conversation.getMessage(position);
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
		EMMessage message = getItem(position);
		String reason = "";
		String user_friend_name = "";
		String user_firend_avatar = "";
		try {
			reason = message.getStringAttribute("reason");
			user_friend_name = message.getStringAttribute("user_friend_name");
			user_firend_avatar = message
					.getStringAttribute("user_firend_avatar");
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		holder.txt_user_name.setText(user_friend_name);
		holder.txt_user_desc.setText("验证信息:" + reason);

		UniversalImageLoadTool.disPlay(user_firend_avatar,
				holder.img_user_avatar, R.drawable.default_avatar);
		holder.img_user_avatar.setOnClickListener(new ShowBigAvatariListener(
				mContext, user_firend_avatar));
		return contentView;
	}

	static class ViewHolder {
		private RoundAngleImageView img_user_avatar;
		private TextView txt_user_name;
		private TextView txt_user_desc;
		TextView alpha;

	}
}
