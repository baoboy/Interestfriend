package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.CircleState;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MyCircleListParser;
import com.interestfriend.utils.SharedUtils;

public class CirclesList extends AbstractData {
	private static final String CIRCLE_LIST_API = "CircleListServlet";
	private static final String CIRCLE_MEMBER_CIRCLE_LIST_API = "GetMemberCirclesServlet";
	private final static int MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER = 500;

	private List<MyCircles> servserCircles = new ArrayList<MyCircles>();
	private List<MyCircles> localCircles = new ArrayList<MyCircles>();

	private int member_id;

	public List<MyCircles> getLocalCircles() {
		return localCircles;
	}

	public void setLocalCircles(List<MyCircles> localCircles) {
		this.localCircles = localCircles;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public List<MyCircles> getListCircles() {
		return servserCircles;
	}

	public void setListCircles(List<MyCircles> listCircles) {
		this.servserCircles = listCircles;
	}

	public RetError getCircleList() {
		IParser parser = new MyCircleListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("circle_last_request_time",
				SharedUtils.getCircleLastRequestTime());
		Result<?> ret = ApiRequest.request(CIRCLE_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CirclesList) {
				CirclesList lists = (CirclesList) ret.getData();
				// this.listCircles = lists.getListCircles();
				updateCircles(lists.getListCircles());
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void updateCircles(List<MyCircles> listCircles) {
		for (MyCircles circle : listCircles) {
			if (circle.getCircle_state() == CircleState.DEL) {
				delLocalCircleByID(circle.getCircle_id());
			}
			this.servserCircles.add(circle);

		}
	}

	private void delLocalCircleByID(int circle_id) {
		for (Iterator<MyCircles> it = localCircles.iterator(); it.hasNext();) {
			if (it.next().getCircle_id() == circle_id) {
				it.remove();
			}
		}
	}

	/**
	 * 获取某一个成员的圈子列表
	 * 
	 * @return
	 */
	public RetError getMemberCircleList() {
		IParser parser = new MyCircleListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", member_id);
		Result<?> ret = ApiRequest.request(CIRCLE_MEMBER_CIRCLE_LIST_API,
				params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CirclesList) {
				CirclesList lists = (CirclesList) ret.getData();
				this.servserCircles = lists.getListCircles();
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		List<MyCircles> newCircles = new ArrayList<MyCircles>();
		List<MyCircles> delCircles = new ArrayList<MyCircles>();
		for (Iterator<MyCircles> it = servserCircles.iterator(); it.hasNext();) {
			MyCircles c = it.next();
			if (c.getCircle_state() == CircleState.DEL) {
				delCircles.add(c);
				it.remove();
				continue;
			}
			newCircles.add(c);
		}
		StringBuilder sqlBuffer = new StringBuilder();
		// basic info: delete circle
		sqlBuffer.append("delete from " + Const.MY_CIRCLE_TABLE_NAME
				+ " where circle_id in (");
		int cnt = 0;
		for (MyCircles circle : delCircles) {
			if (cnt > 0) {
				sqlBuffer.append(",");
			}

			sqlBuffer.append(circle.getCircle_id());
			cnt++;
		}
		if (cnt > 0) {
			sqlBuffer.append(")");
			System.out.println("sql::::::::::" + sqlBuffer.toString());

			try {
				db.execSQL(sqlBuffer.toString());

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// insert newcircle
		sqlBuffer = new StringBuilder();
		sqlBuffer.append("insert into " + Const.MY_CIRCLE_TABLE_NAME
				+ MyCircles.getDbInsertKeyString() + " select ");
		cnt = 0;
		for (MyCircles circle : newCircles) {
			if (cnt > 0) {
				sqlBuffer.append(" union all select ");
			}
			sqlBuffer.append(circle.toDbUnionInsertString());
			cnt++;
			if (cnt >= MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER) {
				db.execSQL(sqlBuffer.toString());
				cnt = 0;
				sqlBuffer = new StringBuilder();
				sqlBuffer.append("insert into " + Const.MY_CIRCLE_TABLE_NAME
						+ circle.getDbInsertKeyString() + " select ");
			}
		}
		if (cnt > 0) {
			db.execSQL(sqlBuffer.toString());
		}
	}
}
