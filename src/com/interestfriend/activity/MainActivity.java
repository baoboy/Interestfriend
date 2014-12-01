package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.fragment.CircleGroupChatFragment;
import com.interestfriend.fragment.CircleGrowthFragment;
import com.interestfriend.fragment.CircleMemberFragment;
import com.interestfriend.utils.Utils;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	private int currentTabIndex = -1;

	private CircleMemberFragment memberFragment;
	private CircleGroupChatFragment chatFragment;
	private CircleGrowthFragment growthFragment;
	private int unread, unread_growth;
	private TextView unread_msg_number;
	private TextView unread_msg_number_growth;

	private List<RadioButton> buttonLists = new ArrayList<RadioButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplation.addActivity(this);
		unread = getIntent().getIntExtra("unread", 0);
		unread_growth = getIntent().getIntExtra("growth_unread", 0);
		initFragment();
	}

	private void initFragment() {
		unread_msg_number = (TextView) findViewById(R.id.unread_msg_number);
		unread_msg_number_growth = (TextView) findViewById(R.id.unread_msg_number_growth);
		if (unread > 0) {
			unread_msg_number.setVisibility(View.VISIBLE);
			if (unread > 9) {
				unread_msg_number.setBackgroundResource(R.drawable.un_read2);
				if (unread > 100) {
					unread_msg_number.setText("99+");
				} else {
					unread_msg_number.setText(unread + "");
				}
			} else {
				unread_msg_number.setText(unread + "");
				unread_msg_number.setBackgroundResource(R.drawable.un_read_1);
			}
		}
		if (unread_growth > 0) {
			unread_msg_number_growth.setVisibility(View.VISIBLE);
			if (unread_growth > 9) {
				unread_msg_number_growth
						.setBackgroundResource(R.drawable.un_read2);
				if (unread_growth > 100) {
					unread_msg_number_growth.setText("99+");
				} else {
					unread_msg_number_growth.setText(unread_growth + "");
				}
			} else {
				unread_msg_number_growth.setText(unread_growth + "");
				unread_msg_number_growth
						.setBackgroundResource(R.drawable.un_read_1);
			}
		}
		RadioButton btn = (RadioButton) findViewById(R.id.tab_member);
		btn.setOnClickListener(this);
		buttonLists.add(btn);
		btn = (RadioButton) findViewById(R.id.tab_messsage);
		btn.setOnClickListener(this);
		buttonLists.add(btn);
		btn = (RadioButton) findViewById(R.id.tab_growth);
		btn.setOnClickListener(this);
		buttonLists.add(btn);
		memberFragment = new CircleMemberFragment();
		chatFragment = new CircleGroupChatFragment();
		growthFragment = new CircleGrowthFragment();
		fragmentList.add(memberFragment);
		fragmentList.add(chatFragment);
		fragmentList.add(growthFragment);
		showTab(0);

	}

	public void showTab(int tabIndex) {
		if (tabIndex < 0 || tabIndex >= fragmentList.size())
			return;
		if (currentTabIndex == tabIndex)
			return;
		if (currentTabIndex >= 0) {
			fragmentList.get(currentTabIndex).onPause();
		}
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		for (int i = 0; i < fragmentList.size(); i++) {
			Fragment fg = fragmentList.get(i);
			if (i == tabIndex) {
				if (fg.isAdded()) {
					fg.onResume();
				} else {
					ft.add(R.id.realtabcontent, fg);
				}
				ft.show(fg);
			} else {
				ft.hide(fg);
			}

		}
		ft.commit();
		currentTabIndex = tabIndex;
		RadioButton rb = buttonLists.get(tabIndex);
		if (!rb.isChecked())
			rb.setChecked(true);
	}

	public void setVisible(boolean visible) {
		// for (int i = 0; i < rg.getChildCount(); i++) {
		// RadioButton tabItem = (RadioButton) rg.getChildAt(i).findViewById(
		// R.id.tab);
		// tabItem.setEnabled(visible);
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			Utils.rightOut(this);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tab_messsage:
			unread_msg_number.setVisibility(View.GONE);
			break;
		case R.id.tab_growth:
			unread_msg_number_growth.setVisibility(View.GONE);
			break;
		case R.id.back:
			finish();
			Utils.rightOut(this);
			break;
		default:
			break;
		}

		for (int i = 0; i < buttonLists.size(); i++) {
			if (buttonLists.get(i).getId() == v.getId()) {
				showTab(i);
			} else {
				buttonLists.get(i).setChecked(false);
			}
		}
	}
}
