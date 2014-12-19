package com.interestfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.activity.CircleInfoActivity;
import com.interestfriend.activity.CircleMemberActivity;
import com.interestfriend.activity.CircleMemberOfSelfInfoActivity;
import com.interestfriend.adapter.CircleMemberAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.contentprovider.MyCircleMemberProvider;
import com.interestfriend.data.AbstractData.Status;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.CircleMemberList;
import com.interestfriend.data.Circles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.interfaces.ConfirmDialog;
import com.interestfriend.popwindow.CircleMemberFragmentRightPopwindow;
import com.interestfriend.popwindow.CircleMemberFragmentRightPopwindow.OnMenuClick;
import com.interestfriend.task.DissolveCircleTask;
import com.interestfriend.task.GetCircleMemberTask;
import com.interestfriend.task.QuitCircleTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

import fynn.app.PromptDialog;

@SuppressLint("NewApi")
public class CircleMemberFragment extends Fragment implements
		OnItemClickListener, OnClickListener, OnMenuClick {
	private int circle_id = 0;

	private CircleMemberList list;

	private Dialog dialog;

	private List<CircleMember> cirlceMemberLists = new ArrayList<CircleMember>();

	private CircleMemberAdapter adapter;

	private ListView circle_member_listView;
	private TextView txt_title;
	private ImageView right_image;
	private RelativeLayout title_layout;
	private ImageView back;

	private AsyncQueryHandler asyncQuery;

	private CircleMemberFragmentRightPopwindow pop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.circle_member_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		circle_id = MyApplation.getCircle_id();
		list = new CircleMemberList();
		list.setCid(circle_id);
		initView();
		setValue();
		registerBoradcastReceiver();
	}

	private void initView() {
		back = (ImageView) getView().findViewById(R.id.back);
		title_layout = (RelativeLayout) getView().findViewById(
				R.id.title_layout);
		txt_title = (TextView) getView().findViewById(R.id.title_txt);
		circle_member_listView = (ListView) getView().findViewById(
				R.id.circle_member_listview);
		right_image = (ImageView) getView().findViewById(R.id.rightImg);
		right_image.setVisibility(View.VISIBLE);
		right_image.setImageResource(R.drawable.right_menu);
		setListener();
	}

	private void setListener() {
		circle_member_listView.setOnItemClickListener(this);
		right_image.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	private void setValue() {
		txt_title.setText(MyApplation.getCircle_name());
		adapter = new CircleMemberAdapter(getActivity(), cirlceMemberLists);
		circle_member_listView.setAdapter(adapter);
		initQuery();
	}

	private void initQuery() {
		asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
		String[] projection = {
				MyCircleMemberProvider.MyCircleMemberColumns.CIRCLE_ID,
				MyCircleMemberProvider.MyCircleMemberColumns.PINYIN_FIR,
				MyCircleMemberProvider.MyCircleMemberColumns.SORT_KEY,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_AVATAR,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_BIRTHDAY,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_CELLPHONE,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_GENDER,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_ID,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_NAME,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_CHAT_ID,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_REGISTER_TIME,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_DECLARATION,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_DESCRIPTION }; // 查询的列
		asyncQuery.startQuery(0, null,
				MyCircleMemberProvider.MyCircleMemberColumns.CONTENT_URI,
				projection,
				MyCircleMemberProvider.MyCircleMemberColumns.CIRCLE_ID + "=?",
				new String[] { MyApplation.getCircle_id() + "" },
				MyCircleMemberProvider.MyCircleMemberColumns.SORT_KEY
						+ "COLLATE LOCALIZED asc");
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie,
				final Cursor cursor) {
			if (cursor == null) {
				return;
			}
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					CircleMember member = new CircleMember();
					member.setCircle_id(cursor.getInt(0));
					member.setPinyinFir(cursor.getString(1));
					member.setSortkey(cursor.getString(2));
					member.setUser_avatar(cursor.getString(3));
					member.setUser_birthday(cursor.getString(4));
					member.setUser_cellphone(cursor.getString(5));
					member.setUser_gender(cursor.getString(6));
					member.setUser_id(cursor.getInt(7));
					member.setUser_name(cursor.getString(8));
					member.setUser_chat_id(cursor.getString(9));
					member.setUser_register_time(cursor.getString(10));
					member.setUser_declaration(cursor.getString(11));
					member.setUser_description(cursor.getString(12));
					cirlceMemberLists.add(member);
					cursor.moveToNext();
				}
				list.setLocalMembersLists(cirlceMemberLists);
				list.sort(cirlceMemberLists);
			}
			cirlceMemberLists.add(0, MyApplation.getMemberSelf());
			adapter.notifyDataSetChanged();
			if (System.currentTimeMillis()
					- SharedUtils.getCircleMemberLocalLastReqTime(circle_id) > 10000) {
				if (cirlceMemberLists.size() == 1) {
					dialog = DialogUtil.createLoadingDialog(getActivity(),
							"请稍候");
					dialog.show();
				}
				getCircleMemberFormServer();
				SharedUtils.setCircleMemberLocalLastReqTime(circle_id,
						System.currentTimeMillis());
			}
		}
	}

	private void getCircleMemberFormServer() {
		GetCircleMemberTask task = new GetCircleMemberTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {

			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (getActivity() == null) {
					return;
				}
				if (result != RetError.NONE) {
					if (result == RetError.CIRCLE_ALERADY_DISSOLVE
							|| result == RetError.KICKOUT_CIRCLE) {
						Circles circle = new Circles();
						circle.setCircle_id(circle_id);
						circle.setStatus(Status.DEL);
						circle.write(DBUtils.getDBsa(2));
						Intent intent = new Intent(Constants.DISSOLVE_CIRCLE);
						intent.putExtra("circle_id", circle_id);
						getActivity().sendBroadcast(intent);
						getActivity().finish();
						Utils.rightOut(getActivity());
						return;
					}
					return;
				}
				int size = cirlceMemberLists.size();
				cirlceMemberLists.remove(0);
				cirlceMemberLists.addAll(list.getCircleMemberLists());
				list.sort(cirlceMemberLists);
				cirlceMemberLists.add(0, MyApplation.getMemberSelf());
				adapter.notifyDataSetChanged();
				int newSize = cirlceMemberLists.size();
				if (newSize != size) {
					Intent intent = new Intent(
							Constants.UPDATE_CIRCLE_MEMBER_NUM);
					intent.putExtra("circle_id", circle_id);
					intent.putExtra("num", newSize);
					getActivity().sendBroadcast(intent);
				}
			}
		});
		task.executeParallel(list);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		CircleMember m = cirlceMemberLists.get(position);
		Intent intent = new Intent();
		intent.putExtra("circle_member", m);
		Utils.print("membe_id::::::::::::" + m.getUser_id());
		if (m.getUser_id() == SharedUtils.getIntUid()) {
			intent.setClass(getActivity(), CircleMemberOfSelfInfoActivity.class);
		} else {
			intent.setClass(getActivity(), CircleMemberActivity.class);
		}
		startActivity(intent);
		Utils.leftOutRightIn(getActivity());
	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.UPDATE_USER_INFO);
		myIntentFilter.addAction(Constants.KICK_OUT_MEMBER);

		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 定义广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.UPDATE_USER_INFO)) {
				CircleMember member = (CircleMember) intent
						.getSerializableExtra("member");
				cirlceMemberLists.remove(0);
				cirlceMemberLists.add(0, member);
				adapter.notifyDataSetChanged();

			} else if (action.equals(Constants.KICK_OUT_MEMBER)) {
				int user_id = intent.getIntExtra("user_id", 0);
				for (CircleMember m : cirlceMemberLists) {
					if (m.getUser_id() == user_id) {
						cirlceMemberLists.remove(m);
						break;
					}
				}
				adapter.notifyDataSetChanged();

			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rightImg:
			Circles circle = new Circles();
			circle.setCircle_id(circle_id);
			circle.findCircleCreatorByID(DBUtils.getDBsa(1));
			pop = new CircleMemberFragmentRightPopwindow(getActivity(),
					title_layout,
					circle.getCreator_id() == SharedUtils.getIntUid());
			pop.setmCallBack(this);
			pop.show();
			break;
		case R.id.back:
			getActivity().finish();
			Utils.rightOut(getActivity());

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(int id) {
		switch (id) {
		case R.id.txt_quit_circle:
			quitPrompt();
			break;
		case R.id.txt_circle_info:
			intentCircleInfo();
			break;
		case R.id.txt_dissolve_circle:
			dissolvePrompt();
			break;
		default:
			break;
		}

	};

	private void dissolvePrompt() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(getActivity(),
				"确定要解散圈子吗?", "确定", "取消", new ConfirmDialog() {

					@Override
					public void onOKClick() {
						dissolveCircle();
					}

					@Override
					public void onCancleClick() {

					}
				});
		dialog.show();
	}

	private void dissolveCircle() {
		final Dialog dialog = DialogUtil.createLoadingDialog(getActivity(),
				"请稍候");
		dialog.show();
		DissolveCircleTask task = new DissolveCircleTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("解散成功", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.setAction(Constants.QUIT_CIRCLE);
				intent.putExtra("circle_id", circle_id);
				getActivity().sendBroadcast(intent);
				getActivity().finish();
				Utils.rightOut(getActivity());
			}
		});
		Circles circle = new Circles();
		circle.setCircle_id(circle_id);
		circle.setCircle_name(MyApplation.getCircle_name());
		task.executeParallel(circle);
	}

	private void quitPrompt() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(getActivity(),
				"确定要退出圈子吗?", "确定", "取消", new ConfirmDialog() {

					@Override
					public void onOKClick() {
						quitCircle();
					}

					@Override
					public void onCancleClick() {

					}
				});
		dialog.show();
	}

	private void quitCircle() {
		final Dialog dialog = DialogUtil.createLoadingDialog(getActivity(),
				"请稍候");
		dialog.show();
		QuitCircleTask task = new QuitCircleTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("退出成功", Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.setAction(Constants.QUIT_CIRCLE);
				intent.putExtra("circle_id", circle_id);
				getActivity().sendBroadcast(intent);
				getActivity().finish();
				Utils.rightOut(getActivity());
			}
		});
		Circles circle = new Circles();
		circle.setCircle_id(circle_id);
		task.executeParallel(circle);
	}

	private void intentCircleInfo() {
		Circles circle = new Circles();
		circle.setCircle_id(circle_id);
		circle.read(DBUtils.getDBsa(1));
		Intent intent = new Intent();
		intent.putExtra("circle", circle);
		intent.putExtra("isLocalCircle", true);
		intent.setClass(getActivity(), CircleInfoActivity.class);
		startActivity(intent);
		Utils.leftOutRightIn(getActivity());
	}
}
