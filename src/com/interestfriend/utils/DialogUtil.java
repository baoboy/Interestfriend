package com.interestfriend.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.interfaces.ConfirmDialog;

import fynn.app.PromptDialog;

public class DialogUtil {
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.load_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		// loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				Utils.getSecreenWidth(context) -130,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

		return loadingDialog;

	}

	/**
	 * 确认对话框
	 * 
	 * @param context
	 * @param title
	 * @param content
	 */
	public static PromptDialog.Builder confirmDialog(Context context,
			String content, String txtOk, String txtCancle,
			final ConfirmDialog callBack) {

		PromptDialog.Builder dialog = new PromptDialog.Builder(context);
		dialog.setTitle("提示");
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage(content);
		dialog.setButton1(txtOk, new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				callBack.onOKClick();

			}
		});
		dialog.setButton2(txtCancle, new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				callBack.onCancleClick();

			}
		});
		// View view = LayoutInflater.from(context).inflate(
		// R.layout.confirm_dialog, null);
		// final Dialog dialog = new Dialog(context, R.style.Dialog);
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(view);
		// Button btnOk = (Button) view.findViewById(R.id.btnOk);
		// Button btnCancle = (Button) view.findViewById(R.id.btnCancle);
		// btnOk.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// callBack.onOKClick();
		//
		// }
		// });
		// btnCancle.setText(txtCancle);
		// btnOk.setText(txtOk);
		// btnCancle.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// callBack.onCancleClick();
		// dialog.dismiss();
		// }
		// });
		// TextView txt = (TextView) view.findViewById(R.id.dialogContent);
		// txt.setText(content);
		// Window dialogWindow = dialog.getWindow();
		// dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
		// /* * 将对话框的大小按屏幕大小的百分比设置 */
		//
		// WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
		// // 获取对话框当前的参数值//
		// p.width = (int) (Utils.getSecreenWidth(context) * 0.8); //
		// // 宽度设置为屏幕的0.8//
		// p.y = -70;
		// dialogWindow.setAttributes(p);
		return dialog;

	}
}
