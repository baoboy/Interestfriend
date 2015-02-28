package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.adapter.InviteMessageAdapter;
import com.interestfriend.data.InviteMessgeDao;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.GetInviteMessageTask;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.Utils;

public class FriendVertifyListActivity extends BaseActivity implements
		OnClickListener {
	private ImageView back;
	private TextView txt_title;
	private ListView mListView;

	private Dialog dialog;

	private InviteMessgeDao dao = new InviteMessgeDao();

	private InviteMessageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_vertify_list);
		initView();
		setValue();
		getInviteMessage();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		mListView = (ListView) findViewById(R.id.listview);
		txt_title.setText("∫√”—…Í«Î");
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				startActivity(new Intent(FriendVertifyListActivity.this,
						FriendVertifyActivity.class).putExtra("message", dao
						.getLists().get(position)));
				Utils.leftOutRightIn(FriendVertifyListActivity.this);
			}
		});
	}

	private void setValue() {
		adapter = new InviteMessageAdapter(this, dao.getLists());
		mListView.setAdapter(adapter);
	}

	private void getInviteMessage() {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		GetInviteMessageTask task = new GetInviteMessageTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(dao);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;

		default:
			break;
		}
	}
}
