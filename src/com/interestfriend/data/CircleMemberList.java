package com.interestfriend.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.CircleMemberState;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.CircleMemberListParser;
import com.interestfriend.parser.IParser;
import com.interestfriend.utils.SharedUtils;

public class CircleMemberList extends AbstractData {
	private static final String CIRCLE_MEMBER_LIST_API = "GetMembersByCircleIDServlet";
	private final static int MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER = 500;

	private int circle_id = 0;

	private List<CircleMember> circleMemberLists = new ArrayList<CircleMember>();

	private List<CircleMember> localMembersLists = new ArrayList<CircleMember>();
	private List<CircleMember> newMembers = new ArrayList<CircleMember>();
	private List<CircleMember> delMembers = new ArrayList<CircleMember>();

	public int getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(int circle_id) {
		this.circle_id = circle_id;
	}

	public List<CircleMember> getLocalMembersLists() {
		return localMembersLists;
	}

	public void setLocalMembersLists(List<CircleMember> localMembersLists) {
		this.localMembersLists = localMembersLists;
	}

	public List<CircleMember> getCircleMemberLists() {
		sort(circleMemberLists);
		getMe(circleMemberLists);
		return circleMemberLists;
	}

	public void getMe(List<CircleMember> circleMemberLists) {
		for (CircleMember m : circleMemberLists) {
			if (m.getUser_id() == SharedUtils.getIntUid()) {
				circleMemberLists.remove(m);
				circleMemberLists.add(0, m);
				break;
			}
		}
	}

	public void setCircleMemberLists(List<CircleMember> circleMemberLists) {
		this.circleMemberLists = circleMemberLists;
	}

	public void setCid(int circle_id) {
		this.circle_id = circle_id;
	}

	public void sort(List<CircleMember> circleMemberLists) {
		Collections.sort(circleMemberLists, new Comparator<CircleMember>() {
			@Override
			public int compare(CircleMember lhs, CircleMember rhs) {
				return lhs.getSortkey().compareTo(rhs.getSortkey());
			}
		});

	}

	public RetError getCircleMemberList() {
		IParser parser = new CircleMemberListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_id", circle_id);
		params.put("lastReqTime",
				SharedUtils.getCircleMemberLastReqTime(circle_id));
		Result<?> ret = ApiRequest.request(CIRCLE_MEMBER_LIST_API, params,
				parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CircleMemberList) {
				CircleMemberList lists = (CircleMemberList) ret.getData();
				// this.circleMemberLists.addAll(lists.getCircleMemberLists());
				updateMembers(lists.getCircleMemberLists());
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void delById(int uid) {
		for (Iterator<CircleMember> it = localMembersLists.iterator(); it
				.hasNext();) {
			if (it.next().getUser_id() == uid) {
				it.remove();
			}
		}
	}

	private void updateMembers(List<CircleMember> circleMemberLists) {
		for (CircleMember m : circleMemberLists) {
			if (m.getState() == CircleMemberState.ADD) {
				this.circleMemberLists.add(m);
				this.newMembers.add(m);

			}
			if (m.getState() == CircleMemberState.DEL) {
				delById(m.getUser_id());
				delMembers.add(m);
				continue;
			}
			if (m.getState() == CircleMemberState.UPDATE) {
				delById(m.getUser_id());
				this.circleMemberLists.add(m);
				delMembers.add(m);
				this.newMembers.add(m);

			}
		}

	}

	@Override
	public void write(SQLiteDatabase db) {
		StringBuilder sqlBuffer = new StringBuilder();

		// basic info: delete delMembers
		sqlBuffer.append("delete from " + Const.CIRCLE_MEMBER_TABLE
				+ " where user_id in (");
		int cnt = 0;
		for (CircleMember dm : delMembers) {
			if (cnt > 0) {
				sqlBuffer.append(",");
			}

			sqlBuffer.append(dm.getUser_id());
			cnt++;
		}
		if (cnt > 0) {
			sqlBuffer.append(") and circle_id=" + circle_id);
			try {
				db.execSQL(sqlBuffer.toString());
			} catch (Exception e) {
			}
		}
		// insert newMembers
		sqlBuffer = new StringBuilder();
		sqlBuffer.append("insert into " + Const.CIRCLE_MEMBER_TABLE
				+ CircleMember.getDbInsertKeyString() + " select ");
		cnt = 0;
		for (CircleMember nm : newMembers) {
			if (cnt > 0) {
				sqlBuffer.append(" union all select ");
			}
			sqlBuffer.append(nm.toDbUnionInsertString());
			cnt++;
			if (cnt >= MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER) {
				db.execSQL(sqlBuffer.toString());
				cnt = 0;
				sqlBuffer = new StringBuilder();
				sqlBuffer.append("insert into " + Const.MY_CIRCLE_TABLE_NAME
						+ CircleMember.getDbInsertKeyString() + " select ");
			}
		}
		if (cnt > 0) {
			db.execSQL(sqlBuffer.toString());
		}
		this.newMembers.clear();
		this.delMembers.clear();
	}
}
