package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.adapter.CommentAdapter;
import com.interestfriend.adapter.GrowthImgAdapter;
import com.interestfriend.adapter.PraiseAdapter;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Comment;
import com.interestfriend.data.Growth;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.CommentPopwindow;
import com.interestfriend.popwindow.CommentPopwindow.OnCommentOnClick;
import com.interestfriend.task.DeleteCommentTask;
import com.interestfriend.task.SendCommentTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DateUtils;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.ExpandGridView;
import com.interestfriend.view.HorizontalListView;

public class CommentActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, TextWatcher, OnCommentOnClick {
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
	private ListView mListView;

	private Growth growth;

	private Dialog dialog;

	private CommentAdapter adapter;
	private List<Comment> comments = new ArrayList<Comment>();

	private int position;

	private boolean isReplaySomeOne = false;

	private int replaySomeOneID = 0;
	private String replaySomeOneName = "";

	private CommentPopwindow pop;

	private LinearLayout parise_layout;
	private HorizontalListView praise_listView;
	private PraiseAdapter praiseAdapter;
	private LinearLayout comment_layout;

	private RelativeLayout layout_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		growth = (Growth) getIntent().getSerializableExtra("growth");
		position = getIntent().getIntExtra("position", 0);
		initView();
		setValue();
	}

	private void initView() {
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
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
		mListView = (ListView) findViewById(R.id.listView1);
		parise_layout = (LinearLayout) findViewById(R.id.layout_praise);
		praise_listView = (HorizontalListView) findViewById(R.id.praise_listView);
		comment_layout = (LinearLayout) findViewById(R.id.layout_comment);

		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		edit_comment.addTextChangedListener(this);
		mListView.setOnItemClickListener(this);
		Utils.getFocus(layout_title);
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
		comments = growth.getComments();
		adapter = new CommentAdapter(this, comments);
		mListView.setAdapter(adapter);
		viewLineVisible();
		if (growth.getPraises().size() > 0) {
			parise_layout.setVisibility(View.VISIBLE);
			praiseAdapter = new PraiseAdapter(this, growth.getPraises());
			praise_listView.setAdapter(praiseAdapter);
		} else {
			parise_layout.setVisibility(View.GONE);

		}
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
			sendComment(content.replace("@" + replaySomeOneName, ""));
			break;
		default:
			break;
		}
	}

	private void sendComment(String content) {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		final Comment comment = new Comment();
		comment.setComment_content(content);
		if (isReplaySomeOne) {
			comment.setReply_someone_name(replaySomeOneName);
			comment.setReply_someone_id(replaySomeOneID);
		}
		comment.setGrowth_id(growth.getGrowth_id());
		comment.setComment_time(DateUtils.getGrowthShowTime());
		comment.setPublisher_id(SharedUtils.getIntUid());
		comment.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
		comment.setPublisher_name(SharedUtils.getAPPUserName());
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
				comments.add(0, comment);
				adapter.notifyDataSetChanged();
				viewLineVisible();
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("comment", comment);
				intent.setAction(Constants.COMMENT_GROWTH);
				sendBroadcast(intent);
			}
		});
		task.executeParallel(comment);
	}

	private void viewLineVisible() {
		if (comments.size() > 0) {
			comment_layout.setVisibility(View.VISIBLE);

		} else {
			comment_layout.setVisibility(View.GONE);

		}
	}

	private void delReplaySomeOne() {
		isReplaySomeOne = false;
		replaySomeOneID = 0;
		replaySomeOneName = "";
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		pop = new CommentPopwindow(this, view, position,
				growth.getPublisher_id());
		pop.setmCallBack(this);
		pop.show();

	}

	@Override
	public void afterTextChanged(Editable str) {
		if (isReplaySomeOne) {
			if (replaySomeOneName.equals(str.toString().replace("@", ""))) {
				edit_comment.setText("");
				delReplaySomeOne();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onClick(int position, int id) {
		switch (id) {
		case R.id.txt_reply:
			reply(position);
			break;
		case R.id.txt_del:
			del(position);
			break;
		default:
			break;
		}
	}

	private void del(final int position) {
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		Comment comment = comments.get(position);
		DeleteCommentTask task = new DeleteCommentTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					ToastUtil.showToast("…æ≥˝ ß∞‹", Toast.LENGTH_SHORT);
					return;
				}
				comments.remove(position);
				adapter.notifyDataSetChanged();
				viewLineVisible();

			}
		});
		task.executeParallel(comment);
	}

	private void reply(int position) {
		Comment comment = comments.get(position);
		edit_comment.setText(Html.fromHtml("<font color=#F06617>@"
				+ comment.getPublisher_name() + "</font> "));
		edit_comment.setSelection(edit_comment.getText().toString().length());
		Utils.getFocus(edit_comment);
		isReplaySomeOne = true;
		replaySomeOneID = comment.getPublisher_id();
		replaySomeOneName = comment.getPublisher_name();
	}
}
