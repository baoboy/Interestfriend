package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.fragment.CircleGroupChatFragment;
import com.interestfriend.fragment.CircleGrowthFragment;
import com.interestfriend.fragment.ImageFragment;
import com.interestfriend.fragment.CircleMemberFragment;

public class MainActivity extends FragmentActivity implements
		RadioGroup.OnCheckedChangeListener {

	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	private int currentTabIndex = -1;
	private int circle_id = 0;

	private CircleMemberFragment memberFragment;
	private CircleGroupChatFragment chatFragment;
	private CircleGrowthFragment growthFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplation.addActivity(this);
		circle_id = getIntent().getIntExtra("circle_id", 0);
		initFragment();
	}

	private void initFragment() {
		memberFragment = new CircleMemberFragment();
		chatFragment = new CircleGroupChatFragment();
		growthFragment = new CircleGrowthFragment();
		fragmentList.add(memberFragment);
		fragmentList.add(chatFragment);
		fragmentList.add(growthFragment);
		RadioGroup rg = (RadioGroup) this.findViewById(R.id.tabs_rg);
		rg.setOnCheckedChangeListener(this);
		showTab(0);

	}

	public void showTab(int tabIndex) {
		RadioGroup group = (RadioGroup) this.findViewById(R.id.tabs_rg);
		if (tabIndex < 0 || tabIndex >= group.getChildCount())
			return;
		if (currentTabIndex == tabIndex)
			return;
		if (currentTabIndex >= 0) {
			fragmentList.get(currentTabIndex).onPause();
		}
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		for (int i = 0; i < group.getChildCount(); i++) {
			Fragment fg = fragmentList.get(i);
			RadioButton tabItem = (RadioButton) group.getChildAt(i);
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
		}
		ft.commit();
		currentTabIndex = tabIndex;
		RadioButton rb = (RadioButton) group.getChildAt(tabIndex);
		if (!rb.isChecked())
			rb.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
			if (group.getChildAt(i).getId() == checkedId) {
				showTab(i);
				break;
			}
		}
	}

}
