package com.interestfriend.popwindow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.CategoryCircle;

/**
 * 选择圈子类别
 * 
 * @author songbinbin
 * 
 */
public class SelectCircleCategoryPopWindow implements OnClickListener,
		OnItemClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private View view;
	private OnSelectKey callback;
	private ListView listview;
	private MyAdapter adapter;
	private LinearLayout bg;

	private List<CategoryCircle> lists = new ArrayList<CategoryCircle>();

	public SelectCircleCategoryPopWindow(Context context, View v) {
		this.mContext = context;
		this.v = v;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.select_circle_categoty_layout, null);
		initData();
		adapter = new MyAdapter();
		initView();
		initPopwindow();
	}

	private void initData() {
		CategoryCircle c = new CategoryCircle();
		c.setCode(1);
		c.setName("官方圈子");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(2);
		c.setName("游戏世界");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(3);
		c.setName("卡通动漫");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(4);
		c.setName("明星名人");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(5);
		c.setName("电影电视");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(6);
		c.setName("生活情感");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(7);
		c.setName("体育运动");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(8);
		c.setName("文学艺术");
		lists.add(c);
		c = new CategoryCircle();
		c.setCode(9);
		c.setName("旅游摄影");
		lists.add(c);
	}

	private void initView() {
		bg = (LinearLayout) view.findViewById(R.id.layParent);
		bg.setOnClickListener(this);
		listview = (ListView) view.findViewById(R.id.listView1);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);
	}

	/**
	 * 初始化popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 这个是为了点击�?返回Back”也能使其消失，并且并不会影响你的背景（很神奇的�?
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * popwindow的显�?
	 */
	public void show() {
		popupWindow
				.setAnimationStyle(R.style.select_circle_cagegory_popwindow_anim);
		popupWindow.showAtLocation(v, 0, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状�?
		popupWindow.update();
	}

	// 隐藏
	public void dismiss() {
		popupWindow.dismiss();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.select_circle_category_listview_item, null);
				holder = new ViewHolder();
				holder.laybg = (LinearLayout) convertView
						.findViewById(R.id.laybg);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(lists.get(position).getName());
			return convertView;
		}
	}

	class ViewHolder {
		TextView text;
		LinearLayout laybg;
	}

	public void setCallBack(OnSelectKey callback) {
		this.callback = callback;
	}

	public interface OnSelectKey {
		void getSelectKey(CategoryCircle category);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layParent:
			dismiss();
			break;

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		callback.getSelectKey(lists.get(arg2));
		dismiss();
	}

}
