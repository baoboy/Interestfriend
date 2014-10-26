package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.util.DateUtils;
import com.easemob.util.TextFormater;
import com.interestfriend.R;
import com.interestfriend.adapter.VideoCommentAdapter;
import com.interestfriend.data.Video;
import com.interestfriend.data.VideoComment;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.SendVideoCommentTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;

public class VideoCommentActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_avatar;
	private TextView txt_time;
	private TextView txt_user_name;
	private TextView txt_context;
	private ImageView playBtn;
	private TextView video_timeLength;
	private TextView video_size;
	private ImageView iv;

	private ImageView back;
	private TextView txt_title;
	private Button btn_comment;
	private EditText edit_comment;
	private ListView mListView;

	private Video video;

	private Dialog dialog;

	private VideoCommentAdapter adapter;
	private List<VideoComment> comments = new ArrayList<VideoComment>();

	private int position;

	private ScrollView layout_scroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_comment);
		video = (Video) getIntent().getSerializableExtra("video");
		position = getIntent().getIntExtra("position", 0);
		initView();
		setValue();
	}

	private void initView() {
		layout_scroll = (ScrollView) findViewById(R.id.layout_scroll);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		btn_comment = (Button) findViewById(R.id.btn_comment);
		edit_comment = (EditText) findViewById(R.id.edit_content);
		mListView = (ListView) findViewById(R.id.listView1);
		video_size = (TextView) findViewById(R.id.chatting_size_iv);
		video_timeLength = (TextView) findViewById(R.id.chatting_length_iv);
		playBtn = (ImageView) findViewById(R.id.chatting_status_btn);
		iv = (ImageView) findViewById(R.id.chatting_content_iv);
		txt_context = (TextView) findViewById(R.id.txt_content);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		txt_time = (TextView) findViewById(R.id.txt_time);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
	}

	private void setValue() {
		txt_title.setText("∆¿¬€");
		String content = video.getVideo_txt_content();
		if ("".equals(content)) {
			txt_context.setVisibility(View.GONE);
		} else {
			txt_context.setVisibility(View.VISIBLE);
			txt_context.setText(content);

		}
		txt_time.setText(video.getTime());
		UniversalImageLoadTool.disPlay(video.getPublisher_avatar(), img_avatar,
				R.drawable.default_avatar);
		txt_user_name.setText(video.getPublisher_name());
		video_timeLength.setText(DateUtils.toTime(video.getVideo_duration()));
		video_size.setText(TextFormater.getDataSize(Long.valueOf(video
				.getVideo_size())));
		String path = video.getVideo_img();
		if (!path.startsWith("http")) {
			path = "file://" + path;
		}
		UniversalImageLoadTool.disPlay(path, iv, R.drawable.empty_photo);
		playBtn.setImageResource(R.drawable.video_download_btn_nor);

		iv.setClickable(true);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(VideoCommentActivity.this,
						ShowVideoActivity.class);
				String path = video.getVideo_path();
				if (path.startsWith("http")) {
					intent.putExtra("remotepath", path);
					intent.putExtra("localpath", "");

				} else {
					intent.putExtra("localpath", path);
					intent.putExtra("remotepath", "");
				}
				intent.putExtra("secret", "");
				startActivity(intent);
			}
		});
		comments = video.getComments();
		adapter = new VideoCommentAdapter(this, comments);
		mListView.setAdapter(adapter);
		layout_scroll.post(new Runnable() {
			@Override
			public void run() {
				layout_scroll.fullScroll(ScrollView.FOCUS_UP);
			}
		});
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
		final VideoComment comment = new VideoComment();
		comment.setComment_content(content);
		comment.setVideo_id(video.getVideo_id());
		comment.setComment_time(com.interestfriend.utils.DateUtils
				.getGrowthShowTime());
		comment.setPublisher_id(SharedUtils.getIntUid());
		comment.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
		comment.setPublisher_name(SharedUtils.getAPPUserName());
		SendVideoCommentTask task = new SendVideoCommentTask();
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
				comments.add(0, comment);
				adapter.notifyDataSetChanged();
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("comment", comment);
				intent.setAction(Constants.COMMENT_VIDEO);
				sendBroadcast(intent);
			}
		});
		task.execute(comment);
	}

}
