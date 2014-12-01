package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.db.DBUtils;
import com.interestfriend.db.DataBaseHelper;
import com.interestfriend.fragment.FindCircleFragmen;
import com.interestfriend.fragment.MyCircleFragment;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.DrawerLeftMenu;
import com.interestfriend.view.HackyViewPager;

import fynn.app.PromptDialog;

public class HomeActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener, DrawerListener {
	private HackyViewPager mPager;
	private ImagePagerAdapter mAdapter;
	private ImageView imageView;
	private Button btn_tab_neay_circle, btn_tab_my_circle;
	public DrawerLayout drawerLayout;// 侧边栏布局
	private ImageView img_add;
	private ImageView img_back;
	private ImageView img_prompt;

	private FindCircleFragmen nearFragment;
	private MyCircleFragment myCircleFragment;
	private List<Fragment> listFragments = new ArrayList<Fragment>();
	private int screenW;
	private int old;
	private DrawerLeftMenu lfetMenu;

	private NewMessageBroadcastReceiver msgReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		MyApplation.addActivity(this);
		initFragment();
		initView();
		registerReceive();
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());

	}

	@Override
	protected void onResume() {
		updateUnreadLabel();
		EMChatManager.getInstance().activityResumed();
		super.onResume();
	}

	private void initView() {
		img_prompt = (ImageView) findViewById(R.id.img_prompt);
		img_back = (ImageView) findViewById(R.id.back);
		img_back.setOnClickListener(this);
		img_back.setImageResource(R.drawable.menu_nomal);
 		lfetMenu = (DrawerLeftMenu) findViewById(R.id.left_menu);
		btn_tab_my_circle = (Button) findViewById(R.id.btn_tab_my_circle);
		btn_tab_neay_circle = (Button) findViewById(R.id.btn_tab_near_circle);
		img_add = (ImageView) findViewById(R.id.img_add);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(0);
		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		InitImageView();
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

	private void setListener() {
		mPager.setOnPageChangeListener(this);
		img_add.setOnClickListener(this);
		btn_tab_my_circle.setOnClickListener(this);
		btn_tab_neay_circle.setOnClickListener(this);
		drawerLayout.setDrawerListener(this);
	}

	private void initFragment() {
		nearFragment = new FindCircleFragmen();
		myCircleFragment = new MyCircleFragment();
		listFragments.add(myCircleFragment);
		listFragments.add(nearFragment);
	}

	private void registerReceive() {
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.addAction(Constants.UPDATE_USER_AVATAR);
		intentFilter.addAction(Constants.UPDATE_USER_NAME);

		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
	}

	/**
	 * 初始化滑动的view
	 */
	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;// 获取分辨率宽度
		int offset = (screenW / 2) / 2;// 计算偏移量
		imageView
				.setLayoutParams(new LinearLayout.LayoutParams(screenW / 2, 2));
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
		setListener();
	}

	class ImagePagerAdapter extends FragmentStatePagerAdapter {

		private FragmentManager fm;

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			fm.beginTransaction();

		}

		@Override
		public int getCount() {
			return listFragments.size();
		}

		@Override
		public Fragment getItem(int position) {

			return listFragments.get(position);

		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (arg2 != 0) {
			Animation animation = new TranslateAnimation(old / 2, arg2 / 2
					* arg1, 0, 0);// 显然这个比较简洁，只有一行代码。
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			old = arg2;

		}
	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 0) {
			img_add.setVisibility(View.VISIBLE);
		} else {
			img_add.setVisibility(View.GONE);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_add:
			startActivity(new Intent(this, CreateCircleActivity.class));
			Utils.leftOutRightIn(this);
			break;
		case R.id.btn_tab_near_circle:
			mPager.setCurrentItem(1);
			break;
		case R.id.btn_tab_my_circle:
			mPager.setCurrentItem(0);
			break;
		case R.id.back:
			if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
				drawerLayout.closeDrawers();
				return;
			}
			drawerLayout.openDrawer(Gravity.LEFT);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(msgReceiver);

	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.UPDATE_USER_AVATAR)) {
				String avatar = intent.getStringExtra("user_avatar");
				lfetMenu.setAvatar(avatar);
				return;
			}
			if (action.equals(Constants.UPDATE_USER_NAME)) {
				String name = intent.getStringExtra("user_name");
				lfetMenu.setName(name);
				return;
			}
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			if (message.getChatType() == ChatType.Chat) {
				updateUnreadLabel();
			} else {
				if (Constants.DISSOLVE_CIRCLE_USER_ID.equals(message.getFrom())) {
					intent = new Intent(HomeActivity.this,
							DissolveCircleActivity.class);
					intent.putExtra("userId", message.getTo());
					startActivity(intent);
					Utils.leftOutRightIn(HomeActivity.this);
				} else {
					myCircleFragment.refushCircleGroupChatHositiory();
				}
			}
			abortBroadcast();
		}
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			lfetMenu.setMessagePrompt(true);
			img_prompt.setVisibility(View.VISIBLE);
		} else {
			lfetMenu.setMessagePrompt(false);
			img_prompt.setVisibility(View.GONE);

		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		// unreadMsgCountTotal =
		// EMChatManager.getInstance().getUnreadMsgsCount();
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getIsGroup()) {
				continue;
			}
			unreadMsgCountTotal += conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal;
	}

	@Override
	public void onDrawerClosed(View arg0) {
		if (mPager.getCurrentItem() == 0) {
			img_add.setVisibility(View.VISIBLE);
		}
		img_back.setImageResource(R.drawable.menu_nomal);

	}

	@Override
	public void onDrawerOpened(View arg0) {
		img_add.setVisibility(View.GONE);
		img_back.setImageResource(R.drawable.menu_select);
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {

	}

	@Override
	public void onDrawerStateChanged(int arg0) {

	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登陆dialog
				showConflictDialog();
			}
		}

		@Override
		public void onReConnected() {
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	private boolean isConflictDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		MyApplation.logoutHuanXin();

		if (!HomeActivity.this.isFinishing()) {
			PromptDialog.Builder dialog = new PromptDialog.Builder(this);
			dialog.setTitle("下线通知");
			dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
			dialog.setMessage("您的账号已在其他设备登录");
			dialog.setButton1("确定", new PromptDialog.OnClickListener() {

				@Override
				public void onClick(Dialog dialog, int which) {
					dialog.dismiss();
					MyApplation.exit(false);
					SharedUtils.setUid(0 + "");
					DataBaseHelper.setIinstanceNull();
					DBUtils.dbase = null;
					DBUtils.close();
					startActivity(new Intent(HomeActivity.this,
							LoginActivity.class));
				}
			}).show();

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow)
			showConflictDialog();
	}
}
