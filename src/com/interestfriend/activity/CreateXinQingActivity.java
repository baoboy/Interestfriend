package com.interestfriend.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.interfaces.CameraCallback;

public class CreateXinQingActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_camera_switch;
	private ImageView img_take_photo;
	private ImageView img_select_photo;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private CameraCallback mCallback;

	private boolean saved = true;
	private boolean isFrontCamera;

	public static final int MESSAGE_SVAE_SUCCESS = 0;
	public static final int MESSAGE_SVAE_FAILURE = 1;

	private final int FLASH_MODE_AUTO = 0;
	private final int FLASH_MODE_ON = 1;
	private final int FLASH_MODE_OFF = 2;
	private int mFlashMode = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			saved = true;
			switch (msg.what) {
			case MESSAGE_SVAE_SUCCESS:
				Toast.makeText(CreateXinQingActivity.this, "保存成功",
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_SVAE_FAILURE:
				Toast.makeText(CreateXinQingActivity.this, "保存成功",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_xin_qing);
		initView();
		initSurfaceView();
	}

	private void initView() {
		img_select_photo = (ImageView) findViewById(R.id.img_select_photo);
		img_take_photo = (ImageView) findViewById(R.id.img_take_photo);
		img_camera_switch = (ImageView) findViewById(R.id.rightImg);
		img_camera_switch.setVisibility(View.VISIBLE);
		img_camera_switch
				.setImageResource(R.drawable.scene_photo_button_switch);
		setListener();
	}

	private void setListener() {
		img_camera_switch.setOnClickListener(this);
		img_select_photo.setOnClickListener(this);
		img_take_photo.setOnClickListener(this);
	}

	private void initSurfaceView() {
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mHolder = mSurfaceView.getHolder();
		mCallback = new CameraCallback(this);
		mHolder.addCallback(mCallback);
		mSurfaceView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP
						&& !isFrontCamera) {// 前置摄像头取消触摸自动聚焦功能
					View view = findViewById(R.id.RelativeLayout1);
					mCallback.autoFocus(view, event);
				}

				return true;
			}
		});

		// 判断是否支持前置摄像头
		int cameras = mCallback.getNumberOfCameras();
		if (cameras <= 1) {
			img_camera_switch.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_take_photo:
			if (saved) {
				saved = false;
				mCallback.takePicture(mHandler);
			}
			break;
		case R.id.rightImg:
			isFrontCamera = !isFrontCamera;
			mCallback.switchCamera(mSurfaceView, isFrontCamera);
			break;

		default:
			break;
		}

	}

}
