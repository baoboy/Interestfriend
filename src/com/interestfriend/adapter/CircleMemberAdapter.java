package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
import com.interestfriend.interfaces.ShowBigAvatariListener;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class CircleMemberAdapter extends BaseAdapter {
	private List<CircleMember> list = new ArrayList<CircleMember>();
	private Context mContext;

	public CircleMemberAdapter(Context context, List<CircleMember> list) {
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
		if ("女".equals(list.get(position).getUser_gender())) {
			// holder.txt_user_name.setText(Html
			// .fromHtml("<font color=#FF9a9a>[MM] </font>"
			// + list.get(position).getUser_name()));
			holder.txt_user_name.setText(Html
					.fromHtml("<font color=#FF9a9a>[MM] "
							+ list.get(position).getUser_name() + "</font>"));
		} else {
			// holder.txt_user_name.setText(Html
			// .fromHtml("<font color=#19b5ee>[GG] </font>"
			// + list.get(position).getUser_name()));
			holder.txt_user_name.setText(Html
					.fromHtml("<font color=#19b5ee>[GG] "
							+ list.get(position).getUser_name() + "</font>"));
		}
		holder.txt_user_desc.setText(list.get(position).getUser_declaration());
		String decl = list.get(position).getUser_declaration();
		String desc = list.get(position).getUser_description();
		if (!"".equals(decl)) {
			holder.txt_user_desc.setText(decl);
		} else if (!"".equals(desc)) {
			holder.txt_user_desc.setText(desc);
		} else {
			holder.txt_user_desc.setText("这家伙很懒，什么都没留下");
		}
		UniversalImageLoadTool.disPlay(list.get(position).getUser_avatar(),
				holder.img_user_avatar, R.drawable.default_avatar);
		holder.img_user_avatar.setOnClickListener(new ShowBigAvatariListener(
				mContext, list.get(position).getUser_avatar()));
 		 showAlpha(position, holder);
 		return contentView;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 大写输出
		} else {
			return "#";
		}

	}

	private void showAlpha(int position, ViewHolder holder) {
		// 当前联系人的sortKey
		String currentStr = getAlpha(list.get(position).getSortkey());
		// 上一个联系人的sortKey
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getSortkey()) : " ";
		/**
		 * 判断显示#、A-Z的TextView隐藏与显示
		 */
		if (!previewStr.equals(currentStr)) { // 当前联系人的sortKey与上一个联系人的sortKey不同，说明当前联系人是新组
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			if (position == 1) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);

			} else {
				holder.alpha.setVisibility(View.GONE);
			}
		}
		if (position == 0) {
			holder.alpha.setVisibility(View.GONE);
		}
	}

	static class ViewHolder {
		private RoundAngleImageView img_user_avatar;
		private TextView txt_user_name;
		private TextView txt_user_desc;

		TextView alpha;

	}
}
