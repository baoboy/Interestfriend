package com.interestfriend.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.adapter.XinQingCommentAdapter;
import com.interestfriend.adapter.XinQingPraiseAdapter;
import com.interestfriend.data.XinQing;
import com.interestfriend.data.XinQingComment;
import com.interestfriend.data.XinQingPraise;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.popwindow.CommentPopwindow;
import com.interestfriend.popwindow.CommentPopwindow.OnCommentOnClick;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.task.CancelPraiseXinQingTask;
import com.interestfriend.task.GetXinQingTask;
import com.interestfriend.task.PraiseXinQingTask;
import com.interestfriend.task.SendXinQingCommentTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DateUtils;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.HorizontalListView;

public class RefushXinQingCommentActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, TextWatcher, OnCommentOnClick {
	private ImageView img_avatar;
	private TextView txt_user_name;
	private TextView txt_time;
	private TextView txt_context;
	private ImageView img;
	private ImageView back;
	private TextView txt_title;
	private Button btnComment;
	private EditText edit_comment;
	private ListView mListView;

	private XinQing xinqing = new XinQing();

	private Dialog dialog;

	private XinQingCommentAdapter adapter;
	private List<XinQingComment> comments = new ArrayList<XinQingComment>();

	private int position;

	private boolean isReplaySomeOne = false;

	private int replaySomeOneID = 0;
	private String replaySomeOneName = "";

	private CommentPopwindow pop;

	private LinearLayout parise_layout;
	private HorizontalListView praise_listView;
	private XinQingPraiseAdapter praiseAdapter;
	private LinearLayout comment_layout;

	private RelativeLayout layout_title;

	private TextView btn_praise;
	private TextView btn_comment;

	private boolean isTasking = false;

	private int xinqing_id;
	private String user_id = "";
	private EMConversation conversation;
	private EMMessage lastMessage;

	private ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xin_qing_comment);
		getDataByMessage();
		initView();
		refush();
	}

	private void initView() {
		mScrollView = (ScrollView) findViewById(R.id.layout_scroll);
		mScrollView.setVisibility(View.GONE);
		btn_praise = (TextView) findViewById(R.id.btn_prise);
		btn_comment = (TextView) findViewById(R.id.btn_comment);
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		img = (ImageView) findViewById(R.id.img);
		txt_context = (TextView) findViewById(R.id.txt_content);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		btnComment = (Button) findViewById(R.id.btnComment);
		edit_comment = (EditText) findViewById(R.id.edit_content);
		mListView = (ListView) findViewById(R.id.listView1);
		parise_layout = (LinearLayout) findViewById(R.id.layout_praise);
		praise_listView = (HorizontalListView) findViewById(R.id.praise_listView);
		comment_layout = (LinearLayout) findViewById(R.id.layout_comment);
		setListener();
		txt_title.setText("∆¿¬€");
	}

	private void getDataByMessage() {
		user_id = getIntent().getStringExtra("userId");
		conversation = EMChatManager.getInstance().getConversation(user_id);
		lastMessage = conversation.getLastMessage();
		try {
			xinqing_id = Integer.valueOf(lastMessage
					.getStringAttribute("xinqing_id"));
			xinqing.setXinqing_id(xinqing_id);
			;
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
	}

	private void setListener() {
		img.setOnClickListener(this);
		back.setOnClickListener(this);
		btnComment.setOnClickListener(this);
		edit_comment.addTextChangedListener(this);
		mListView.setOnItemClickListener(this);
		btn_praise.setOnClickListener(this);
		Utils.getFocus(layout_title);
	}

	private void setValue() {
		String path = xinqing.getImage_url();
		if (!path.startsWith("http")) {
			path = "file://" + path;
		}
		UniversalImageLoadTool.disPlay(path, img, R.drawable.empty_photo);
		img.setVisibility(View.VISIBLE);

		String content = xinqing.getContent();
		txt_context.setText(content);
		txt_time.setText(xinqing.getPublish_time());
		UniversalImageLoadTool.disPlay(xinqing.getPublisher_avatar(),
				img_avatar, R.drawable.default_avatar);
		txt_user_name.setText(xinqing.getPublisher_name());
		comments = xinqing.getComments();
		adapter = new XinQingCommentAdapter(this, comments);
		mListView.setAdapter(adapter);
		viewLineVisible();
		praiseAdapter = new XinQingPraiseAdapter(this, xinqing.getPraises());
		if (xinqing.getPraises().size() > 0) {
			parise_layout.setVisibility(View.VISIBLE);
			praise_listView.setAdapter(praiseAdapter);
		} else {
			parise_layout.setVisibility(View.GONE);

		}
		if (comments.size() > 0) {
			btn_comment.setText("ªÿ∏¥(" + xinqing.getComments().size() + ")");
		} else {
			btn_comment.setText("ªÿ∏¥");
		}
		Drawable drawable = getResources().getDrawable(
				R.drawable.praise_img_nomal);
		if (xinqing.isPraise()) {
			drawable = getResources().getDrawable(R.drawable.praise_img_focus);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		btn_praise.setCompoundDrawables(drawable, null, null, null);
		if (xinqing.getPraise_count() > 0) {
			btn_praise.setText("‘ﬁ(" + xinqing.getPraise_count() + ")");
		} else {
			btn_praise.setText("‘ﬁ");
		}
	}

	private void cancelPraise() {
		isTasking = true;
		Drawable drawable = getResources().getDrawable(
				R.drawable.praise_img_nomal);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		btn_praise.setCompoundDrawables(drawable, null, null, null);
		int count = xinqing.getPraise_count() - 1;
		if (count > 0) {
			btn_praise.setText("‘ﬁ(" + (xinqing.getPraise_count() - 1) + ")");
		} else {
			btn_praise.setText("‘ﬁ ");
		}
		for (XinQingPraise prais : xinqing.getPraises()) {
			if (prais.getUser_id() == SharedUtils.getIntUid()) {
				xinqing.getPraises().remove(prais);
				praiseAdapter.notifyDataSetChanged();
				break;
			}
		}
		CancelPraiseXinQingTask task = new CancelPraiseXinQingTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					Intent intent = new Intent();
					intent.putExtra("xinqing_id", xinqing.getXinqing_id());
					intent.setAction(Constants.XINQINGCOMMENT_CANCEL_PRAISE);
					sendBroadcast(intent);
				}
			}
		});
		task.executeParallel(xinqing);
	}

	private void praise() {
		isTasking = true;
		Drawable drawable = getResources().getDrawable(
				R.drawable.praise_img_focus);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		btn_praise.setCompoundDrawables(drawable, null, null, null);
		btn_praise.setText("‘ﬁ(" + (xinqing.getPraise_count() + 1) + ")");
		XinQingPraise praise = new XinQingPraise();
		praise.setXinqing_id(xinqing.getXinqing_id());
		praise.setUser_avatar(SharedUtils.getAPPUserAvatar());
		praise.setUser_id(SharedUtils.getIntUid());
		praise.write(DBUtils.getDBsa(2));
		xinqing.getPraises().add(praise);
		praiseAdapter.notifyDataSetChanged();
		PraiseXinQingTask task = new PraiseXinQingTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					Intent intent = new Intent();
					intent.putExtra("xinqing_id", xinqing.getXinqing_id());
					intent.setAction(Constants.XINQING_COMMENT_PRAISE);
					sendBroadcast(intent);
				}
			}
		});
		task.executeParallel(xinqing);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btnComment:
			String content = edit_comment.getText().toString().trim();
			if (content.length() == 0) {
				return;
			}
			sendComment(content.replace("@" + replaySomeOneName, ""));
			break;
		case R.id.btn_prise:
			if (isTasking) {
				return;
			}
			if (!xinqing.isPraise()) {
				praise();
			} else {
				cancelPraise();
			}
			break;
		case R.id.img:
			intentImagePager(1);
			break;
		default:
			break;
		}
	}

	private void intentImagePager(int index) {
		List<String> imgUrl = new ArrayList<String>();
		imgUrl.add(xinqing.getImage_url());
		Intent intent = new Intent(this, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
		startActivity(intent);
	}

	private void sendComment(String content) {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		final XinQingComment comment = new XinQingComment();
		comment.setComment_content(content);
		if (isReplaySomeOne) {
			comment.setReply_someone_name(replaySomeOneName);
			comment.setReply_someone_id(replaySomeOneID);
		}
		comment.setXinqing_id(xinqing.getXinqing_id());
		comment.setComment_time(DateUtils.getGrowthShowTime());
		comment.setPublisher_id(SharedUtils.getIntUid());
		comment.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
		comment.setPublisher_name(SharedUtils.getAPPUserName());
		SendXinQingCommentTask task = new SendXinQingCommentTask(
				xinqing.getPublisher_id());
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				edit_comment.setText("");
				ToastUtil.showToast("ªÿ∏¥≥…π¶", Toast.LENGTH_SHORT);
				comments.add(0, comment);
				adapter.notifyDataSetChanged();
				viewLineVisible();
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("comment", comment);
				intent.setAction(Constants.COMMENT_XINQING);
				sendBroadcast(intent);
			}
		});
		task.executeParallel(comment);
	}

	private void viewLineVisible() {
		if (comments.size() > 0) {
			comment_layout.setVisibility(View.VISIBLE);
			btn_comment.setText("ªÿ∏¥(" + xinqing.getComments().size() + ")");
		} else {
			comment_layout.setVisibility(View.GONE);
			btn_comment.setText("ªÿ∏¥");
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
		pop = new CommentPopwindow(this, view, position, 0);
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
			break;
		default:
			break;
		}
	}

	private void reply(int position) {
		XinQingComment comment = comments.get(position);
		edit_comment.setText(Html.fromHtml("<font color=#F06617>@"
				+ comment.getPublisher_name() + "</font> "));
		edit_comment.setSelection(edit_comment.getText().toString().length());
		Utils.getFocus(edit_comment);
		isReplaySomeOne = true;
		replaySomeOneID = comment.getPublisher_id();
		replaySomeOneName = comment.getPublisher_name();
	}

	private void refush() {
		GetXinQingTask task = new GetXinQingTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (result != RetError.NONE) {
					return;
				}
				setValue();
				mScrollView.setVisibility(View.VISIBLE);
				conversation.removeMessage(lastMessage.getMsgId());
			}
		});
		task.executeParallel(xinqing);
	}
}
