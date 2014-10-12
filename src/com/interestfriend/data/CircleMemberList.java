package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

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

	public List<CircleMember> getCircleMemberLists() {
		return circleMemberLists;
	}

	public void setCircleMemberLists(List<CircleMember> circleMemberLists) {
		this.circleMemberLists = circleMemberLists;
	}

	public void setCid(int circle_id) {
		this.circle_id = circle_id;
	}

	public RetError getCircleMemberList() {
		IParser parser = new CircleMemberListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_id", circle_id);
		params.put("lastReqTime", SharedUtils.getCircleMemberLastReqTime());
		Result<?> ret = ApiRequest.request(CIRCLE_MEMBER_LIST_API, params,
				parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CircleMemberList) {
				CircleMemberList lists = (CircleMemberList) ret.getData();
				this.circleMemberLists.addAll(lists.getCircleMemberLists());
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		// insert newMembers
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append("insert into " + Const.CIRCLE_MEMBER_TABLE_NAME
				+ CircleMember.getDbInsertKeyString() + " select ");
		int cnt = 0;
		for (CircleMember nm : circleMemberLists) {
			if (cnt > 0) {
				sqlBuffer.append(" union all select ");
			}
			sqlBuffer.append(nm.toDbUnionInsertString());
			cnt++;
			if (cnt >= MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER) {
				db.execSQL(sqlBuffer.toString());
				cnt = 0;
				sqlBuffer = new StringBuilder();
				sqlBuffer.append("insert into "
						+ Const.CIRCLE_MEMBER_TABLE_NAME
						+ CircleMember.getDbInsertKeyString() + " select ");
			}
		}
		if (cnt > 0) {
			db.execSQL(sqlBuffer.toString());
		}
	}
}
