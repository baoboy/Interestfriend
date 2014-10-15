package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import com.interestfriend.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CircleGrowthFragment extends Fragment implements
		RadioGroup.OnCheckedChangeListener {
	private ImageFragment imgFragment;
	private VideoFragment videoFragment;

	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	private int currentTabIndex = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.growth_frament_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initFragment();

	}

	private void initFragment() {
		imgFragment = new ImageFragment();
		videoFragment = new VideoFragment();
		fragmentList.add(imgFragment);
		fragmentList.add(videoFragment);
		RadioGroup rg = (RadioGroup) getActivity().findViewById(R.id.tab_group);
		rg.setOnCheckedChangeListener(this);
		showTab(0, rg);

	}

	@SuppressLint("ResourceAsColor")
	public void showTab(int tabIndex, RadioGroup group) {
		if (tabIndex < 0 || tabIndex >= group.getChildCount())
			return;
		if (currentTabIndex == tabIndex)
			return;
		if (currentTabIndex >= 0) {
			fragmentList.get(currentTabIndex).onPause();
		}
		FragmentTransaction ft = this.getChildFragmentManager()
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
				tabItem.setTextColor(R.color.blue);
				ft.show(fg);
			} else {
				ft.hide(fg);
				tabItem.setTextColor(Color.WHITE);

			}
		}
		ft.commit();
		currentTabIndex = tabIndex;
		if (currentTabIndex == 0) {
			group.setBackgroundResource(R.drawable.tab_bg1_growth);
		} else {
			group.setBackgroundResource(R.drawable.tab_bg_growth);
		}
		RadioButton rb = (RadioButton) group.getChildAt(tabIndex);
		if (!rb.isChecked())
			rb.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
			if (group.getChildAt(i).getId() == checkedId) {
				showTab(i, group);
				break;
			}
		}
	}
}
