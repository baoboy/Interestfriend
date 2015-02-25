package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.ShowBigAvatariListener;
import com.interestfriend.task.SearchCirclesByCategoryTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.PullDownView.OnPullDownListener;
import com.interestfriend.view.RoundAngleImageView;

public class SearchCirclsActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnPullDownListener {
	private int category = 0;
	private ListView mlistView;
	private TextView txt_title;
	private ImageView back;

	private Dialog dialog;

	private CategoryCircleList lists;

	private MyCircleAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();
	private String category_name = "";

	// private PullDownView mPullDownView;
	private int page = 1;

	private View view_foot;

	// private LinearLayout layout_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_circls);
		category = getIntent().getIntExtra("category", 0);
		category_name = getIntent().getStringExtra("category_name");
		lists = new CategoryCircleList();
		lists.setCategory(category);
		initView();
		setValue();
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		getCircleList();
	}

	private void initView() {
		view_foot = LayoutInflater.from(this).inflate(R.layout.pulldown_footer,
				null);
		view_foot.setVisibility(View.GONE);
		mlistView = (ListView) findViewById(R.id.listview);
		mlistView.addFooterView(view_foot);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
		mlistView.setLayoutAnimation(getLayoutAni());
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
		mlistView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						if (listCircles.size() < 10) {
							return;
						}
						view_foot.setVisibility(View.VISIBLE);
						getCircleList();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void setValue() {
		txt_title.setText(category_name);

	}

	class MyCircleAdapter extends BaseAdapter {
		private List<MyCircles> list = new ArrayList<MyCircles>();
		private Context mContext;

		public MyCircleAdapter(Context context, List<MyCircles> list) {
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
						R.layout.circle_item_delete, null);
				holder = new ViewHolder();
				holder.img_circle_logo = (RoundAngleImageView) contentView
						.findViewById(R.id.img_circle_logo);
				holder.txt_circle_name = (TextView) contentView
						.findViewById(R.id.txt_circle_name);
				holder.txt_unread = (TextView) contentView
						.findViewById(R.id.unread_msg_number);
				holder.txt_circle_desc = (TextView) contentView
						.findViewById(R.id.txt_circle_desc);
				holder.txt_circle_member_num = (TextView) contentView
						.findViewById(R.id.circle_member_num);
				contentView.setTag(holder);
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			holder.txt_circle_name.setText(list.get(position).getCircle_name());
			holder.txt_circle_desc.setText(list.get(position)
					.getCircle_description());
			holder.txt_circle_member_num.setText(list.get(position)
					.getCircle_member_num() + "");
			UniversalImageLoadTool.disPlay(list.get(position).getCircle_logo(),
					holder.img_circle_logo, R.drawable.default_avatar);
			holder.img_circle_logo
					.setOnClickListener(new ShowBigAvatariListener(
							SearchCirclsActivity.this, list.get(position)
									.getCircle_logo()));
			int num = list.get(position).getUnread()
					+ list.get(position).getGrowth_unread();
			if (num > 0) {
				if (num > 9) {
					holder.txt_unread
							.setBackgroundResource(R.drawable.un_read2);
					if (num > 100) {
						holder.txt_unread.setText("99+");
					} else {
						holder.txt_unread.setText(num + "");
					}
				} else {
					holder.txt_unread.setText(num + "");
					holder.txt_unread
							.setBackgroundResource(R.drawable.un_read_1);
				}
				holder.txt_unread.setVisibility(View.VISIBLE);

			} else {
				holder.txt_unread.setVisibility(View.GONE);
			}
			return contentView;
		}

		class ViewHolder {
			private RoundAngleImageView img_circle_logo;
			private TextView txt_circle_name;
			private TextView txt_unread;
			private TextView txt_circle_desc;
			private TextView txt_circle_member_num;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("circle", listCircles.get(position));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}

	private void getCircleList() {

		SearchCirclesByCategoryTask task = new SearchCirclesByCategoryTask(page);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				// mPullDownView.notifyDidMore();
				if (dialog != null) {
					dialog.dismiss();
				}
				view_foot.setVisibility(View.GONE);
				if (result != RetError.NONE) {
					return;
				}
				listCircles.addAll(lists.getListCircles());
				if (listCircles.size() == 0) {
					ToastUtil.showToast("还没有圈子哦,赶快创建一个吧", Toast.LENGTH_SHORT);
					return;
				}
				if (adapter == null) {
					adapter = new MyCircleAdapter(SearchCirclsActivity.this,
							listCircles);
					mlistView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				if (lists.getListCircles().size() > 0) {
					// mPullDownView.setFooterVisible(true);
					page++;
				} else {
					// mPullDownView.setFooterVisible(false);
				}
				lists.getListCircles().clear();
			}
		});
		task.executeParallel(lists);
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
