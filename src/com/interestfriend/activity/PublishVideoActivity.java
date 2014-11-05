package com.interestfriend.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.easemob.util.PathUtil;
import com.easemob.util.TextFormater;
import com.interestfriend.R;
import com.interestfriend.utils.UniversalImageLoadTool;

public class PublishVideoActivity extends BaseActivity implements
		OnClickListener {
	private TextView video_timeLength;
	private TextView video_size;
	private ImageView video_img;
	private RelativeLayout videoClick;
	private ImageView back;
	private TextView txt_title;
	private Button btn_send;
	private EditText edit_content;

	private String video_path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_video);
		getIntentData();
		initView();
		setValue();
	}

	private void getIntentData() {

	}

	private void initView() {
		edit_content = (EditText) findViewById(R.id.txt_content);
		video_img = (ImageView) findViewById(R.id.video_img);
		videoClick = (RelativeLayout) findViewById(R.id.ll_click_area);
		video_size = (TextView) findViewById(R.id.chatting_size_iv);
		video_timeLength = (TextView) findViewById(R.id.chatting_length_iv);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		btn_send = (Button) findViewById(R.id.btnUpload);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		videoClick.setOnClickListener(this);
		btn_send.setOnClickListener(this);

	}

	private void setValue() {
		txt_title.setText("∑¢≤º ”∆µ");
		getVideo();

	}

	private void getVideo() {
		int duration = getIntent().getIntExtra("dur", 0);
		String videoPath = getIntent().getStringExtra("path");
		File file = new File(PathUtil.getInstance().getImagePath(), "thvideo"
				+ System.currentTimeMillis());
		Bitmap bitmap = null;
		FileOutputStream fos = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.app_panel_video_icon);
			}
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}

		}
		video_path = videoPath;
		UniversalImageLoadTool.disPlay("file://" + file.getAbsolutePath(),
				video_img, R.drawable.empty_photo);
		video_timeLength.setText(DateUtils.toTime(duration));
 		video_size.setText(TextFormater.getDataSize(Long
				.valueOf((int) new File(videoPath).length())));
	}

	private void playVideo() {
		Intent intent = new Intent(this, ShowVideoActivity.class);
		intent.putExtra("localpath", video_path);
		intent.putExtra("remotepath", "");
		intent.putExtra("secret", "");
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.ll_click_area:
			playVideo();
			break;
		case R.id.btnUpload:
			Intent intent = getIntent();
			intent.putExtra("content", edit_content.getText().toString());
			setResult(300, getIntent());
			finishThisActivity();
			break;
		default:
			break;
		}
	}
}
