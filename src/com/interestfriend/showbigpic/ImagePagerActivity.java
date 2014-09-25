package com.interestfriend.showbigpic;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.showbigpic.ImageDetailFragment.OnBack;
import com.interestfriend.utils.Constants;
import com.interestfriend.view.HackyViewPager;

public class ImagePagerActivity extends FragmentActivity implements OnBack {
	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private List<String> lists = new ArrayList<String>();
	private ImagePagerAdapter mAdapter;

	private LinearLayout title;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail_pager);
		pagerPosition = getIntent().getIntExtra(Constants.EXTRA_IMAGE_INDEX, 0);
		lists = (List<String>) getIntent().getExtras().getSerializable(
				Constants.EXTRA_IMAGE_URLS);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), lists);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}
		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(Constants.STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
		title = (LinearLayout) findViewById(R.id.layTitle);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(Constants.STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<String> fileList;
		private FragmentManager fm;

		public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
			super(fm);
			this.fileList = fileList;
			this.fm = fm;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			fm.beginTransaction();

		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			ImageDetailFragment fra = ImageDetailFragment.newInstance(url);
			fra.setCallBack(ImagePagerActivity.this);
			return fra;
		}
	}

	@Override
	public void onBackClick() {
		if (title.getVisibility() != View.VISIBLE) {
			title.setVisibility(View.VISIBLE);
			title.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.down_in));

		} else {
			title.setVisibility(View.INVISIBLE);
			title.setAnimation(AnimationUtils
					.loadAnimation(this, R.anim.up_out));

		}
	}

}