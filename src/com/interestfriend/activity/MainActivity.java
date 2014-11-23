package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.fragment.CircleGroupChatFragment;
import com.interestfriend.fragment.CircleGrowthFragment;
import com.interestfriend.fragment.CircleMemberFragment;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.MyRadioButton;

public class MainActivity extends FragmentActivity implements
		RadioGroup.OnCheckedChangeListener {

	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	private int currentTabIndex = -1;

	private CircleMemberFragment memberFragment;
	private CircleGroupChatFragment chatFragment;
	private CircleGrowthFragment growthFragment;
	private RadioGroup rg;
	private int unread;
	private int growth_unread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplation.addActivity(this);
		unread = getIntent().getIntExtra("unread", 0);
		growth_unread = getIntent().getIntExtra("growth_unread", 0);
		initFragment();
	}

	private void initFragment() {
		memberFragment = new CircleMemberFragment();
		chatFragment = new CircleGroupChatFragment();
		growthFragment = new CircleGrowthFragment();
		fragmentList.add(memberFragment);
		fragmentList.add(chatFragment);
		fragmentList.add(growthFragment);
		rg = (RadioGroup) this.findViewById(R.id.tabs_rg);
		rg.setOnCheckedChangeListener(this);
		rg.setEnabled(false);
		showTab(0);

	}

	public void showTab(int tabIndex) {
		if (tabIndex < 0 || tabIndex >= rg.getChildCount())
			return;
		if (currentTabIndex == tabIndex)
			return;
		if (currentTabIndex >= 0) {
			fragmentList.get(currentTabIndex).onPause();
		}
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		for (int i = 0; i < rg.getChildCount(); i++) {
			Fragment fg = fragmentList.get(i);
			MyRadioButton tabItem = (MyRadioButton) rg.getChildAt(i);
			if (i == tabIndex) {
				if (fg.isAdded()) {
					fg.onResume();
				} else {
					ft.add(R.id.realtabcontent, fg);
				}
				ft.show(fg);
				tabItem.setTextColor(Color.rgb(255, 34, 34));
			} else {
				ft.hide(fg);
				tabItem.setTextColor(Color.rgb(108, 79, 34));
			}
			if (i == 1) {
				tabItem.setNum(unread - growth_unread);
				unread = 0;
			} else if (i == 2) {
				tabItem.setNum(growth_unread);
				growth_unread = 0;

			}
		}
		ft.commit();
		currentTabIndex = tabIndex;
		MyRadioButton rb = (MyRadioButton) rg.getChildAt(tabIndex);
		if (!rb.isChecked())
			rb.setChecked(true);
	}

	public void setVisible(boolean visible) {
		for (int i = 0; i < rg.getChildCount(); i++) {
			MyRadioButton tabItem = (MyRadioButton) rg.getChildAt(i);
			tabItem.setEnabled(visible);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		System.out.println("aaaaaaaaaaaaaaaaaaaa");
		for (int i = 0; i < group.getChildCount(); i++) {
			if (group.getChildAt(i).getId() == checkedId) {
				showTab(i);
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			Utils.rightOut(this);
		}
		return super.onKeyDown(keyCode, event);
	}
}
