package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MyCircleListParser;

public class CirclesList extends AbstractData {
	private static final String CIRCLE_LIST_API = "CircleListServlet";
	private List<MyCircles> listCircles = new ArrayList<MyCircles>();

	public List<MyCircles> getListCircles() {
		return listCircles;
	}

	public void setListCircles(List<MyCircles> listCircles) {
		this.listCircles = listCircles;
	}

	public RetError getCircleList() {
		IParser parser = new MyCircleListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Result<?> ret = ApiRequest.request(CIRCLE_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CirclesList) {
				CirclesList lists = (CirclesList) ret.getData();
				this.listCircles = lists.getListCircles();
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		for (MyCircles circles : listCircles) {
			circles.write(db);
		}
	}
}
