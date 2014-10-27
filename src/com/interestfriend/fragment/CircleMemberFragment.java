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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.interestfriend.R;
import com.interestfriend.activity.CircleMemberActivity;
import com.interestfriend.activity.CircleMemberOfSelfInfoActivity;
import com.interestfriend.adapter.CircleMemberAdapter;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.contentprovider.MyCircleMemberProvider;
import com.interestfriend.data.CircleMember;
import com.interestfriend.data.CircleMemberList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetCircleMemberTask;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

@SuppressLint("NewApi")
public class CircleMemberFragment extends Fragment implements
		OnItemClickListener {
	private int circle_id = 0;

	private CircleMemberList list;

	private Dialog dialog;

	private List<CircleMember> cirlceMemberLists = new ArrayList<CircleMember>();

	private CircleMemberAdapter adapter;

	private ListView circle_member_listView;
	private TextView txt_title;

	private AsyncQueryHandler asyncQuery;

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
		txt_title = (TextView) getView().findViewById(R.id.title_txt);
		circle_member_listView = (ListView) getView().findViewById(
				R.id.circle_member_listview);
		setListener();
	}

	private void setListener() {
		circle_member_listView.setOnItemClickListener(this);
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
				list.getMe(cirlceMemberLists);
				adapter.notifyDataSetChanged();
				getCircleMemberFormServer();
			} else {
				dialog = DialogUtil.createLoadingDialog(getActivity(), "请稍候");
				dialog.show();
				getCircleMemberFormServer();

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
				if (result != RetError.NONE) {
					ToastUtil.showToast("获取失败", Toast.LENGTH_LONG);
					return;
				}
				cirlceMemberLists.addAll(list.getCircleMemberLists());
				list.sort(cirlceMemberLists);
				list.getMe(cirlceMemberLists);
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(list);
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
		intent.putExtra("circle_ember", m);
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

			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	};
}
