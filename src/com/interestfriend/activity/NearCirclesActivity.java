package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.NearCircleList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.ShowBigAvatariListener;
import com.interestfriend.task.GetNearCirclesTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.PullDownView;
import com.interestfriend.view.PullDownView.OnPullDownListener;
import com.interestfriend.view.RoundAngleImageView;

public class NearCirclesActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnPullDownListener {

	private ListView mlistView;
	private TextView txt_title;
	private ImageView back;

	private Dialog dialog;

	private NearCirclesAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();

	private NearCircleList list;

	private PullDownView mPullDownView;
	private int page = 1;
	private double longitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_circles);
		list = new NearCircleList();
		longitude = MyApplation.getnLontitude();
		latitude = MyApplation.getnLatitude();
		initView();
		setValue();
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		getCircleList();
	}

	private void initView() {
		mPullDownView = (PullDownView) findViewById(R.id.PullDownlistView);
		mlistView = mPullDownView.getListView();
		mlistView.setVerticalScrollBarEnabled(false);
		mlistView.setCacheColorHint(0);
		mlistView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mlistView.setLayoutAnimation(getLayoutAni());
		mPullDownView.setShowRefresh(false);
		mPullDownView.addFooterView();
		mPullDownView.setFooterVisible(false);
		mPullDownView.notifyDidMore();
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
	}

	private LayoutAnimationController getLayoutAni() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.list_anim_slide_right);
		LayoutAnimationController laController = new LayoutAnimationController(
				animation);
		laController.setOrder(LayoutAnimationController.ORDER_NORMAL);
		return laController;
	}

	private void setListener() {
		back.setOnClickListener(this);
		mlistView.setOnItemClickListener(this);
		mPullDownView.setOnPullDownListener(this);
	}

	private void setValue() {
		txt_title.setText("附近圈子");

	}

	class NearCirclesAdapter extends BaseAdapter {
		private List<MyCircles> list = new ArrayList<MyCircles>();
		private Context mContext;

		public NearCirclesAdapter(Context context, List<MyCircles> list) {
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
						R.layout.near_circles_item, null);
				holder = new ViewHolder();
				holder.img_circle_logo = (RoundAngleImageView) contentView
						.findViewById(R.id.img_circle_logo);
				holder.txt_circle_name = (TextView) contentView
						.findViewById(R.id.txt_circle_name);
				holder.txt_diatance = (TextView) contentView
						.findViewById(R.id.txt_distance);
				holder.txt_circle_member_num = (TextView) contentView
						.findViewById(R.id.circle_member_num);
				contentView.setTag(holder);
			} else {
				holder = (ViewHolder) contentView.getTag();
			}

			holder.txt_circle_member_num.setText(list.get(position)
					.getCircle_member_num() + "");
			holder.txt_circle_name.setText(list.get(position).getCircle_name());
			UniversalImageLoadTool.disPlay(list.get(position).getCircle_logo(),
					holder.img_circle_logo, R.drawable.default_avatar);
			holder.img_circle_logo
					.setOnClickListener(new ShowBigAvatariListener(
							NearCirclesActivity.this, list.get(position)
									.getCircle_logo()));
			int distance = list.get(position).getDistance();
			if (distance < 1000) {
				holder.txt_diatance.setText(distance + " 米以内");
			} else {
				holder.txt_diatance.setText((distance / 1000) * 2 + " 公里以内");

			}
			return contentView;
		}

		class ViewHolder {
			private TextView txt_circle_member_num;
			private RoundAngleImageView img_circle_logo;
			private TextView txt_circle_name;
			private TextView txt_diatance;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("circle", listCircles.get(position - 1));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}

	private void getCircleList() {
		GetNearCirclesTask task = new GetNearCirclesTask(longitude, latitude,
				page);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			public void taskFinish(RetError result) {
				mPullDownView.notifyDidMore();
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				listCircles.addAll(list.getListCircles());
				if (adapter == null) {
					adapter = new NearCirclesAdapter(NearCirclesActivity.this,
							listCircles);
					mlistView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				if (list.getListCircles().size() >= 19) {
					mPullDownView.setFooterVisible(true);
					page++;
				} else {
					mPullDownView.setFooterVisible(false);
				}
				adapter.notifyDataSetChanged();
				if (listCircles.size() == 0) {
					ToastUtil.showToast("附近还没有圈子呢,赶快创建一个吧", Toast.LENGTH_SHORT);
				}
				list.getListCircles().clear();
			};
		});
		task.executeParallel(list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onMore() {
		getCircleList();
	}
}
