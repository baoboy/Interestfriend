package com.interestfriend.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.interestfriend.R;

/**
 * ѡ��ͼƬ ���� ѡ���
 * 
 * @author teeker_bin
 * 
 */
public class SelectPicPopwindow implements OnClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private Button btnTakePhoto;
	private Button btnPickPhoto;
	private Button btnCancle;
	private View view;
	private String fileName = "";
	private SelectOnclick mSelectOnclick;

	public void setmSelectOnclick(SelectOnclick mSelectOnclick) {
		this.mSelectOnclick = mSelectOnclick;
	}

	public SelectPicPopwindow(Context mContext, View v, int cid, int imageNum) {
		this(mContext, v);
	}

	public SelectPicPopwindow(Context context, View v) {
		this.mContext = context;
		this.v = v;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.select_image_layout, null);
		initView();
		initPopwindow();
	}

	private void initView() {
		btnCancle = (Button) view.findViewById(R.id.btn_cancel);
		btnPickPhoto = (Button) view.findViewById(R.id.btn_pick_photo);
		btnTakePhoto = (Button) view.findViewById(R.id.btn_take_photo);
		btnCancle.setOnClickListener(this);
		btnPickPhoto.setOnClickListener(this);
		btnTakePhoto.setOnClickListener(this);
	}

	/**
	 * ��ʼ��popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.AnimBottom);
	}

	/**
	 * popwindow����ʾ
	 */
	public void show() {
		popupWindow.showAtLocation(v, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ����
	public void dismiss() {
		popupWindow.dismiss();
	}

	/**
	 * ��������֮�󱣴�·��
	 */
	public String getTakePhotoPath() {
		return fileName;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_cancel:
			break;
		case R.id.btn_pick_photo:
			mSelectOnclick.pickPhoto();
			break;
		case R.id.btn_take_photo:
			mSelectOnclick.takePhoto();
			break;
		default:
			break;
		}

	}

	public interface SelectOnclick {
		void pickPhoto();

		void takePhoto();

	}
}
