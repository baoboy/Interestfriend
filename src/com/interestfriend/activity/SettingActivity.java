package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.db.DBUtils;
import com.interestfriend.db.DataBaseHelper;
import com.interestfriend.interfaces.ConfirmDialog;
import com.interestfriend.task.UpDateNewVersionTask;
import com.interestfriend.task.UpDateNewVersionTask.UpDateVersion;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

import fynn.app.PromptDialog;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private TextView txt_message_prompt;
	private TextView txt_feddback;
	private TextView txt_new_version;
	private TextView txt_about;
	private ImageView back;
	private TextView txt_title;
	private Button btn_quit;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	private void initView() {
		btn_quit = (Button) findViewById(R.id.btn_quit);
		txt_about = (TextView) findViewById(R.id.txt_about);
		txt_message_prompt = (TextView) findViewById(R.id.txt_message_prompt);
		txt_feddback = (TextView) findViewById(R.id.txt_feedback);
		txt_new_version = (TextView) findViewById(R.id.txt_new_version);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("ÉèÖÃ");
		if (SharedUtils.getNewVersion()) {
			Drawable prompt = getResources().getDrawable(R.drawable.prompt);
			prompt.setBounds(0, 0, prompt.getMinimumWidth(),
					prompt.getMinimumHeight());
			txt_new_version.setCompoundDrawables(null, null, prompt, null);
		}
		setListener();
	}

	private void setListener() {
		txt_about.setOnClickListener(this);
		txt_new_version.setOnClickListener(this);
		txt_message_prompt.setOnClickListener(this);
		txt_feddback.setOnClickListener(this);
		back.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
	}

	private void quitPrompt() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(this, "È·¶¨ÒªÍË³öÂð?",
				"È·¶¨", "È¡Ïû", new ConfirmDialog() {
					@Override
					public void onOKClick() {
						quit();
					}

					@Override
					public void onCancleClick() {
					}
				});
		dialog.show();
	}

	private void quit() {
		dialog = DialogUtil.createLoadingDialog(this, "ÇëÉÔºò");
		dialog.show();
		MyApplation.logoutHuanXin(new EMCallBack() {

			@Override
			public void onSuccess() {
				dialog.dismiss();
				MyApplation.exit(false);
				Utils.cleanDatabaseByName(SettingActivity.this);
				SharedUtils.setUid(0 + "");
				SharedUtils.setAPPUserAvatar("");
				DataBaseHelper.setIinstanceNull();
				SharedUtils.setCircleLastRequestTime(0);
				SharedUtils.clearData();
				DBUtils.dbase = null;
				DBUtils.close();
				startActivity(new Intent(SettingActivity.this,
						LoginActivity.class));
			}

			@Override
			public void onProgress(int arg0, String arg1) {

			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			quitPrompt();
			break;
		case R.id.txt_feedback:
			startActivity(new Intent(this, FeedBackActivity.class));
			Utils.leftOutRightIn(this);
			break;
		case R.id.txt_message_prompt:
			startActivity(new Intent(this, MessageWarnctivity.class));
			Utils.leftOutRightIn(this);
			break;
		case R.id.txt_new_version:
			if (!Utils.isNetworkAvailable()) {
				ToastUtil.showToast("ÍøÂç´íÎó,Çë¼ì²éÍøÂç", Toast.LENGTH_SHORT);
				return;
			}
			getNewVersion();
			break;
		case R.id.txt_about:
			startActivity(new Intent(this, AboutActivity.class));
			Utils.leftOutRightIn(this);
			break;
		case R.id.back:
			finishThisActivity();
			break;
		default:
			break;
		}
	}

	private void getNewVersion() {
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "ÇëÉÔºò");
		dialog.show();
		UpDateNewVersionTask task = new UpDateNewVersionTask(this, true);
		task.setCallBack(new UpDateVersion() {
			@Override
			public void getNewVersion(int rt, String versionCode, String link,
					String version_info) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (rt == 0) {
					return;
				}
				DialogUtil.newVewsionDialog(SettingActivity.this, versionCode,
						link, version_info);
			}
		});
		task.execute();
	}

}
