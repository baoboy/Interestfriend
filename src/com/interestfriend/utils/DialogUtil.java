package com.interestfriend.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.interfaces.ConfirmDialog;

public class DialogUtil {
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		// main.xml�е�ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// ��ʾ����
		// ���ض���
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.load_animation);
		// ʹ��ImageView��ʾ����
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// ���ü�����Ϣ

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog

		// loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// ���ò���
		return loadingDialog;

	}

	/**
	 * ȷ�϶Ի���
	 * 
	 * @param context
	 * @param title
	 * @param content
	 */
	public static Dialog confirmDialog(Context context, String content,
			String txtOk, String txtCancle, final ConfirmDialog callBack) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.confirm_dialog, null);
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		Button btnCancle = (Button) view.findViewById(R.id.btnCancle);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onOKClick();
				dialog.dismiss();

			}
		});
		btnCancle.setText(txtCancle);
		btnOk.setText(txtOk);
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callBack.onCancleClick();
				dialog.dismiss();
			}
		});
		TextView txt = (TextView) view.findViewById(R.id.dialogContent);
		txt.setText(content);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
		/* * ���Ի���Ĵ�С����Ļ��С�İٷֱ����� */

		WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
		// ��ȡ�Ի���ǰ�Ĳ���ֵ//
		p.width = (int) (Utils.getSecreenWidth(context) * 0.8); //
		// �������Ϊ��Ļ��0.8//
		p.y = -70;
		dialogWindow.setAttributes(p);
		return dialog;

	}
}
