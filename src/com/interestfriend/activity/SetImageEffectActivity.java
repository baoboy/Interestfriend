package com.interestfriend.activity;

import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.adapter.ImageEffectAdapter;
import com.interestfriend.utils.FileUtils;
import com.interestfriend.utils.GPUImageFilterTools;
import com.interestfriend.utils.GPUImageFilterTools.FilterList;
import com.interestfriend.utils.GPUImageFilterTools.FilterType;
import com.interestfriend.utils.ToastUtil;

public class SetImageEffectActivity extends BaseActivity implements
		OnPictureSavedListener {
	private GPUImageView image;
	private GPUImageFilter mFilter;
	private FilterList filters = new FilterList();

	private Bitmap mBitmap;
	private String image_path = "";

	private GridView mGridView;

	private ImageEffectAdapter adapter;

	private TextView txt_title;
	private ImageView img_back;
	private EditText edit_content;
	private TextView btn_send;

	private String img_save_path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_image_effect);
		image_path = getIntent().getStringExtra("image_path");
		initFilters();
		initView();
		setListener();
		setValue();
	}

	private void initView() {
		btn_send = (TextView) findViewById(R.id.txt_page);
		btn_send.setText("发布");
		edit_content = (EditText) findViewById(R.id.edit_content);
		img_back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("滤镜效果");
		image = (GPUImageView) findViewById(R.id.image);
		mGridView = (GridView) findViewById(R.id.gridView1);
		setGridView();
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				adapter.setSelect_index(position);
				adapter.notifyDataSetChanged();
				switchFilterTo(GPUImageFilterTools.createFilterForType(
						SetImageEffectActivity.this,
						filters.filters.get(position)));
				image.requestRender();
			}
		});
	}

	private void setListener() {
		img_back.setOnClickListener(this);
		btn_send.setOnClickListener(this);
	}

	private void initFilters() {
		filters.addFilter("原图", FilterType.PIXELATION);
		filters.addFilter("经典LOMO", FilterType.CONTRAST);
		filters.addFilter("旧时光", FilterType.MONOCHROME);
		filters.addFilter("唯美", FilterType.TONE_CURVE);
		filters.addFilter("维也纳", FilterType.LOOKUP_AMATORKA);
		filters.addFilter("回忆", FilterType.SEPIA);
		filters.addFilter("黑白", FilterType.GRAYSCALE);
		filters.addFilter("阿宝色", FilterType.HUE);
		filters.addFilter("浓厚", FilterType.GAMMA);
	}

	private void switchFilterTo(final GPUImageFilter filter) {
		if (mFilter == null
				|| (filter != null && !mFilter.getClass().equals(
						filter.getClass()))) {
			mFilter = filter;
			image.setFilter(mFilter);
		}
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
		adapter = new ImageEffectAdapter(this, filters);
		mGridView.setAdapter(adapter);
	}

	private void setValue() {
		mBitmap = BitmapFactory.decodeFile(image_path);
		image.setImage(mBitmap);
	}

	@Override
	public void onPictureSaved(Uri uri) {
		ToastUtil.showToast(img_save_path);
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.txt_page:
			img_save_path = FileUtils.getQuYouGPUImageSavePath()
					+ System.currentTimeMillis() + ".jpg";
			image.saveToPictures("GPUImage", img_save_path, this);
			break;
		default:
			break;
		}
	}
}
