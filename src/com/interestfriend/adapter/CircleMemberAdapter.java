package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.CircleMember;
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
					.findViewById(R.id.img_circle_logo);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_circle_name);
			holder.alpha = (TextView) contentView.findViewById(R.id.alpha);

			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.txt_user_name.setText(list.get(position).getUser_name());
		UniversalImageLoadTool.disPlay(list.get(position).getUser_avatar(),
				holder.img_user_avatar, R.drawable.picture_default_head);
		showAlpha(position, holder);
		return contentView;
	}

	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#����
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
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // ��д���
		} else {
			return "#";
		}

	}

	private void showAlpha(int position, ViewHolder holder) {
		// ��ǰ��ϵ�˵�sortKey
		String currentStr = getAlpha(list.get(position).getSortkey());
		// ��һ����ϵ�˵�sortKey
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getSortkey()) : " ";
		/**
		 * �ж���ʾ#��A-Z��TextView��������ʾ
		 */
		if (!previewStr.equals(currentStr)) { // ��ǰ��ϵ�˵�sortKey����һ����ϵ�˵�sortKey��ͬ��˵����ǰ��ϵ��������
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
	}

	static class ViewHolder {
		private RoundAngleImageView img_user_avatar;
		private TextView txt_user_name;
		TextView alpha;

	}
}
