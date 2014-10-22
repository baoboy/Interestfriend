package com.interestfriend.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.GrowthImgAdapter;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Comment;
import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.SendCommentTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.ExpandGridView;

public class CommentActivity extends BaseActivity implements OnClickListener {
	private ImageView img_avatar;
	private TextView txt_user_name;
	private TextView txt_time;
	private TextView txt_context;
	private ImageView img;
	private ExpandGridView img_grid_view;
	private ImageView back;
	private TextView txt_title;
	private Button btn_comment;
	private EditText edit_comment;

	private Growth growth;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		growth = (Growth) getIntent().getSerializableExtra("growth");
		initView();
		setValue();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		img = (ImageView) findViewById(R.id.img);
		txt_context = (TextView) findViewById(R.id.txt_content);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		img_grid_view = (ExpandGridView) findViewById(R.id.imgGridview);
		btn_comment = (Button) findViewById(R.id.btn_comment);
		edit_comment = (EditText) findViewById(R.id.edit_content);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
	}

	private void setValue() {
		txt_title.setText("∆¿¬€");
		CircleMember member = new CircleMember();
		member.setUser_id(growth.getPublisher_id());
		member.getNameAndAvatar(DBUtils.getDBsa(1));
		int imageSize = growth.getImages().size();
		if (imageSize > 1) {
			if (imageSize > 2) {
				img_grid_view.setNumColumns(3);
			} else {
				img_grid_view.setNumColumns(2);
			}
			img_grid_view.setAdapter(new GrowthImgAdapter(this, growth
					.getImages()));
			img.setVisibility(View.GONE);
			img_grid_view.setVisibility(View.VISIBLE);
		} else if (imageSize == 1) {
			String path = growth.getImages().get(0).getImg();
			if (!path.startsWith("http")) {
				path = "file://" + path;
			}
			UniversalImageLoadTool.disPlay(path, img, R.drawable.empty_photo);
			img.setVisibility(View.VISIBLE);
			img_grid_view.setVisibility(View.GONE);
		} else {
			img.setVisibility(View.GONE);
			img_grid_view.setVisibility(View.GONE);
		}
		String content = growth.getContent();
		if ("".equals(content)) {
			txt_context.setVisibility(View.GONE);
		} else {
			txt_context.setVisibility(View.VISIBLE);
			txt_context.setText(content);

		}
		txt_time.setText(growth.getPublished());
		UniversalImageLoadTool.disPlay(member.getUser_avatar(), img_avatar,
				R.drawable.default_avatar);
		txt_user_name.setText(member.getUser_name());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btn_comment:
			String content = edit_comment.getText().toString().trim();
			if (content.length() == 0) {
				return;
			}
			sendComment(content);
			break;
		default:
			break;
		}
	}

	private void sendComment(String content) {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		Comment comment = new Comment();
		comment.setComment_content(content);
		comment.setGrowth_id(growth.getGrowth_id());
		SendCommentTask task = new SendCommentTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("ªÿ∏¥ ß∞‹", Toast.LENGTH_SHORT);
					return;
				}
				ToastUtil.showToast("ªÿ∏¥≥…π¶", Toast.LENGTH_SHORT);

			}
		});
		task.execute(comment);
	}
}
