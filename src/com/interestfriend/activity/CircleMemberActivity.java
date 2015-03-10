package com.interestfriend.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.interestfriend.R;
import com.interestfriend.adapter.MemberCirclesAdapter;
import com.interestfriend.data.ChatUser;
import com.interestfriend.data.ChatUserDao;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.Circles;
import com.interestfriend.data.CirclesList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.ConfirmDialog;
import com.interestfriend.popwindow.RightMenuPopwindow;
import com.interestfriend.popwindow.RightMenuPopwindow.OnlistOnclick;
import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.task.DelUserFriendTask;
import com.interestfriend.task.GetMemberCircleListTask;
import com.interestfriend.task.GetUserInfoTask;
import com.interestfriend.task.KickOutMemberTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.DampView;

import fynn.app.PromptDialog;

public class CircleMemberActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private ImageView img_avatar;
	private TextView txt_register_time;
	private TextView txt_gender;
	private TextView txt_birthday;
	private TextView txt_title;
	private TextView txt_user_name;
	private TextView txt_declaration;
	private TextView txt_description;
	private ListView circle_listView;
	private ImageView back;
	private ImageView right_image;
	private TextView txt_address;

	private CircleMember member;

	private CirclesList circleList;

	private List<MyCircles> lists = new ArrayList<MyCircles>();

	private MemberCirclesAdapter adapter;

	private RelativeLayout title_layout;

	private RightMenuPopwindow pop;

	private String[] menuStr;

	private DampView view;

	private Dialog dialog;

	private int from;// 从哪个界面进入 -1从好友界面进入
	private boolean isFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_member);
		member = (CircleMember) getIntent().getSerializableExtra(
				"circle_member");
		from = getIntent().getIntExtra("from", 0);
		circleList = new CirclesList();
		initView();
	}

	private void initView() {
		view = (DampView) findViewById(R.id.scrollView1);
		title_layout = (RelativeLayout) findViewById(R.id.title);
		right_image = (ImageView) findViewById(R.id.rightImg);
		right_image.setVisibility(View.VISIBLE);
		right_image.setImageResource(R.drawable.right_menu);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		view.setImageView(img_avatar);
		txt_birthday = (TextView) findViewById(R.id.txt_birthday);
		txt_gender = (TextView) findViewById(R.id.txt_gender);
		txt_register_time = (TextView) findViewById(R.id.txt_register_time);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("个人资料");
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		txt_declaration = (TextView) findViewById(R.id.txt_declaration);
		txt_description = (TextView) findViewById(R.id.txt_description);
		circle_listView = (ListView) findViewById(R.id.circle_listView);
		txt_address = (TextView) findViewById(R.id.txt_address);
		back = (ImageView) findViewById(R.id.back);
		Utils.getFocus(txt_title);
		setListener();
		if ("".equals(member.getUser_name())) {
			dialog = DialogUtil.createLoadingDialog(this, "请稍候");
			dialog.show();
			getUserInfo();
		} else {
			setValue();
		}
	}

	private void setValue() {
		UniversalImageLoadTool.disPlay(member.getUser_avatar(), img_avatar,
				R.drawable.picture_default_head);
		txt_birthday.setText(member.getUser_birthday());
		txt_gender.setText(member.getUser_gender());
		txt_register_time.setText(member.getUser_register_time());
		txt_user_name.setText(member.getUser_name());
		txt_declaration.setText(member.getUser_declaration());
		txt_description.setText(member.getUser_description());
		txt_address.setText(member.getUser_address());
		adapter = new MemberCirclesAdapter(this, lists);
		circle_listView.setAdapter(adapter);
		Circles circlr = new Circles();
		circlr.setCircle_id(member.getCircle_id());
		circlr.findCircleCreatorByID(DBUtils.getDBsa(1));
		if (from == -1) {
			menuStr = new String[] { "私聊", "删除好友" };
		} else {
			ChatUserDao dao = new ChatUserDao();
			isFriend = dao.getFriendByUserID(member.getUser_id());
			int creator = circlr.getCreator_id();
			if (isFriend) {
				if (creator == SharedUtils.getIntUid()) {
					menuStr = new String[] { "私聊", "踢出圈子" };
				} else {
					menuStr = new String[] { "私聊" };
				}
			} else {
				if (creator == SharedUtils.getIntUid()) {
					menuStr = new String[] { "私聊", "加好友", "踢出圈子" };
				} else {
					menuStr = new String[] { "私聊", "加好友" };
				}
			}

		}

		getCircleList();
	}

	private void setListener() {
		txt_user_name.setOnClickListener(this);
		circle_listView.setOnItemClickListener(this);
		back.setOnClickListener(this);
		right_image.setOnClickListener(this);
		img_avatar.setOnClickListener(this);
	}

	private void getUserInfo() {
		GetUserInfoTask task = new GetUserInfoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				setValue();
			}
		});
		task.executeParallel(member);
	}

	private void showAvatar() {
		List<String> imgUrl = new ArrayList<String>();
		imgUrl.add(member.getUser_avatar());
		Intent intent = new Intent(this, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back:
			finishThisActivity();
			break;
		case R.id.img_avatar:
			showAvatar();
			break;
		case R.id.rightImg:
			pop = new RightMenuPopwindow(this, title_layout, menuStr);
			pop.setOnlistOnclick(new OnlistOnclick() {
				@Override
				public void onclick(int position) {
					switch (position) {
					case 0:
						Intent intent = new Intent();
						intent.putExtra("user_id", member.getUser_id());
						intent.putExtra("userId", member.getUser_chat_id());
						intent.putExtra("user_name", member.getUser_name());
						intent.putExtra("user_avatar", member.getUser_avatar());
						intent.setClass(CircleMemberActivity.this,
								ChatActivity.class);
						startActivity(intent);
						Utils.leftOutRightIn(CircleMemberActivity.this);
						break;
					case 1:
						if (from == -1) {
							delFriendDialog();
						} else {
							if (isFriend) {
								kickOutDialog();
							} else {
								startActivity(new Intent(
										CircleMemberActivity.this,
										AddFriendActivity.class)
										.putExtra("add_user_name",
												member.getUser_name())
										.putExtra("add_user_avatar",
												member.getUser_avatar())
										.putExtra("add_user_chat_id",
												member.getUser_chat_id()));
								Utils.leftOutRightIn(CircleMemberActivity.this);
							}
						}

						break;
					case 2:
						kickOutDialog();
						break;
					default:
						break;
					}
				}
			});
			pop.show();
			break;
		default:
			break;
		}
	}

	private void delFromServer() {
		DelUserFriendTask task = new DelUserFriendTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("删除成功", Toast.LENGTH_SHORT);
				sendBroadcast(new Intent(Constants.DEL_USER_FRIEND).putExtra(
						"user_id", member.getUser_id()));
				finishThisActivity();
			}
		});
		ChatUser user = new ChatUser();
		user.setUser_id(member.getUser_id());
		task.execute(user);
	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact() {
		dialog = DialogUtil.createLoadingDialog(this, "请稍候");
		dialog.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					String chat_id = member.getUser_chat_id();
					EMContactManager.getInstance().deleteContact(chat_id);
					runOnUiThread(new Runnable() {
						public void run() {
							delFromServer();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							ToastUtil.showToast(
									"删除失败" + e.toString()
											+ member.getUser_chat_id(),
									Toast.LENGTH_LONG);
							System.out.println("chat:::::::::::::;"
									+ e.toString());
						}
					});

				}

			}
		}).start();

	}

	private void delFriendDialog() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(this,
				"确定要删除好友吗?", "确定", "取消", new ConfirmDialog() {

					@Override
					public void onOKClick() {
						deleteContact();
					}

					@Override
					public void onCancleClick() {
					}
				});
		dialog.show();
	}

	private void kickOutDialog() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(this, "确定要把 "
				+ member.getUser_name() + " 提出圈子吗?", "确定", "取消",
				new ConfirmDialog() {

					@Override
					public void onOKClick() {
						kickOut();
					}

					@Override
					public void onCancleClick() {
					}
				});
		dialog.show();

	}

	private void kickOut() {
		final Dialog dialogLoad = DialogUtil.createLoadingDialog(this, "请稍候");
		dialogLoad.show();
		KickOutMemberTask task = new KickOutMemberTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialogLoad != null) {
					dialogLoad.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("踢出成功", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("user_id", member.getUser_id());
				intent.setAction(Constants.KICK_OUT_MEMBER);
				sendBroadcast(intent);

				intent = new Intent(Constants.REMOVE_CIRCLE_MEMBER_COUNT);
				intent.putExtra("circle_id", member.getCircle_id());
				sendBroadcast(intent);
				finishThisActivity();
			}
		});
		task.executeParallel(member);
	}

	private void getCircleList() {
		circleList.setMember_id(member.getUser_id());
		GetMemberCircleListTask task = new GetMemberCircleListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				lists.addAll(circleList.getListCircles());
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(circleList);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("circle", lists.get(position));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(this);
	}

}
