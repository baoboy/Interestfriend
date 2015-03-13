package com.interestfriend.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.interestfriend.R;
import com.interestfriend.adapter.ImageEffectAdapter;
import com.interestfriend.utils.UniversalImageLoadTool;

public class SetImageEffectActivity extends BaseActivity {
	private ImageView image;

	private String image_path = "";

	private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_image_effect);
		image_path = getIntent().getStringExtra("image_path");
		initView();
		setValue();
	}

	private void initView() {
		image = (ImageView) findViewById(R.id.image);
		mGridView = (GridView) findViewById(R.id.gridView1);
		setGridView();
	}

	private void setGridView() {

		int size = 9;
		int length = 100;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		mGridView.setLayoutParams(params); // 重点
		mGridView.setColumnWidth(itemWidth); // 重点
		mGridView.setHorizontalSpacing(5); // 间距
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setNumColumns(size); // 重点
		mGridView.setAdapter(new ImageEffectAdapter(this));
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay("file://" + image_path, image,
				R.drawable.empty_photo);
	};
}
