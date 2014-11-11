package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.UpdateCircleDiscriptionTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;

public class UpdateCircleDiscriptionActivity extends BaseActivity implements
		OnClickListener {
	private Button btn_save;
	private EditText edit_content;
	private ImageView back;
	private TextView txt_title;
	private Dialog dialog;
	private Circles circle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_circle_discription);
		circle = (Circles) getIntent().getSerializableExtra("circle");
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("»¶◊”√Ë ˆ");
		edit_content = (EditText) findViewById(R.id.edit_content);
		edit_content.setText(circle.getCircle_description());
		btn_save = (Button) findViewById(R.id.btn_save);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	private void upload(String description) {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		UpdateCircleDiscriptionTask task = new UpdateCircleDiscriptionTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("≤Ÿ◊˜ ß∞‹", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("≤Ÿ◊˜≥…π¶", Toast.LENGTH_SHORT);

				Intent intent = new Intent();
				intent.putExtra("circle_description", edit_content.getText()
						.toString());
				setResult(300, intent);
				finishThisActivity();
			}
		});
		circle.setCircle_description(description);
		task.executeParallel(circle);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btn_save:
			String str = edit_content.getText().toString().trim();
			if (str.length() == 0) {
				ToastUtil.showToast("«Î ‰»Î»¶◊”√Ë ˆƒ⁄»›", Toast.LENGTH_SHORT);
				return;
			}
			upload(str);
			break;
		default:
			break;
		}
	}
}
