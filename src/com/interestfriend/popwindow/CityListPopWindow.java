package com.interestfriend.popwindow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
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
import com.interestfriend.data.City;
import com.interestfriend.data.Province;
import com.interestfriend.utils.ProvinceParse;

import fynn.util.Util;

public class CityListPopWindow implements OnClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private View view;
	private ListView listView_province;
	private ListView listView_city;
	private ProvinceParse parse;

	private ProvinceAdapter province_adapter;
	private CityAdapter city_adapter;
	private Province currentProvince;
	private City currentCity;
	private LinearLayout layout_parent;
	private LinearLayout layout_content;

	private SelectCity mCallBack;

	private List<City> cityLists = new ArrayList<City>();

	private int province_index;

	public void setmCallBack(SelectCity mCallBack) {
		this.mCallBack = mCallBack;
	}

	public CityListPopWindow(Context context, View v) {
		this.mContext = context;
		this.v = v;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.city_list_layout, null);
		initView();
		setListener();
		initPopwindow();
		setValue();
	}

	private void initView() {
		layout_content = (LinearLayout) view.findViewById(R.id.layout_conent);
		layout_content.setLayoutParams(new LinearLayout.LayoutParams(Util
				.getScreenWidth(mContext) - 200,
				Util.getScreenWidth(mContext) / 2 + 100));
		layout_parent = (LinearLayout) view.findViewById(R.id.layout_parent);
		layout_parent.getBackground().setAlpha(150);
		listView_province = (ListView) view
				.findViewById(R.id.listView_province);
		listView_city = (ListView) view.findViewById(R.id.listView_city);
	}

	private void setListener() {
		listView_province.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				currentProvince = parse.getProvinces().get(position);
				cityLists = currentProvince.getCities();
				city_adapter = new CityAdapter(cityLists);
				listView_city.setAdapter(city_adapter);
				province_index = position;
				province_adapter.notifyDataSetChanged();
			}
		});
		listView_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				dismiss();
				currentCity = currentProvince.getCities().get(position);
				mCallBack.selectCity(currentProvince.getName(),
						currentProvince.getName_key(), currentCity.getName());
			}
		});
	}

	private void setValue() {
		parse = ProvinceParse.build(mContext, R.raw.province, R.raw.cities);
		currentProvince = parse.getProvinces().get(0);
		cityLists = currentProvince.getCities();
		province_adapter = new ProvinceAdapter(parse.getProvinces());
		listView_province.setAdapter(province_adapter);
		city_adapter = new CityAdapter(cityLists);
		listView_city.setAdapter(city_adapter);

	}

	/**
	 * 初始化popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setAnimationStyle(R.style.AnimBottom);
	}

	/**
	 * popwindow的显示
	 */
	public void show() {
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏
	public void dismiss() {
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.layout_parent:
			dismiss();
			break;
		default:
			break;
		}

	}

	class ProvinceAdapter extends BaseAdapter {
		private List<Province> lists;

		public ProvinceAdapter(List<Province> lists) {
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.city_item, null);
				holder.txt = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txt.setText(lists.get(position).getName());
			if (province_index == position) {
				holder.txt.setTextColor(mContext.getResources().getColor(
						R.color.city_select_color));
			} else {
				holder.txt.setTextColor(mContext.getResources().getColor(
						R.color.city_nomal_color));
			}
			return convertView;
		}

	}

	class CityAdapter extends BaseAdapter {
		private List<City> lists;

		public CityAdapter(List<City> lists) {
			this.lists = lists;
		}

		public void setData(List<City> lists) {
			this.lists = lists;
			notifyDataSetChanged();
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.city_item, null);
				holder.txt = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txt.setText(lists.get(position).getName());
			return convertView;
		}

	}

	class ViewHolder {
		TextView txt;
	}

	public interface SelectCity {
		void selectCity(String province, String province_key, String city);
	}
}
