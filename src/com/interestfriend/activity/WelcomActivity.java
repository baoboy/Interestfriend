package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.register.RegisterActivity;

public class WelcomActivity extends BaseActivity implements OnClickListener {
	private Button btn_register;
	private Button btn_login;
	private List<ImageView> views = new ArrayList<ImageView>();
	private ViewPager mViewpager;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
		MyApplation.addActivity(this);

		// if (SharedUtils.getIntUid() > 0) {
		// startActivity(new Intent(this, HomeActivity.class));
		// finish();
		// }
		initView();
	}

	private void initView() {
		mViewpager = (ViewPager) findViewById(R.id.mViewPager);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.splash_image2);
		img.setScaleType(ScaleType.FIT_XY);
		views.add(img);
		img = new ImageView(this);
		img.setImageResource(R.drawable.login_bg);
		img.setScaleType(ScaleType.FIT_XY);
		views.add(img);
		mViewpager.setAdapter(new MyPagerAdapter());
		setListener();
	}

	private void setListener() {
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.btn_login:
			startActivity(new Intent(this, LoginActivity.class));
			break;
		default:
			break;
		}
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1));
			return views.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));

		}
	}
}
