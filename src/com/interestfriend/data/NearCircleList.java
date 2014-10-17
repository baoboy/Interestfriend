package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MyCircleListParser;

public class NearCircleList {

	private static final String SEARCH_NEAR_CIRCLES_API = "SearchNearCirclesServlet";

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();

	public List<MyCircles> getListCircles() {
		return listCircles;
	}

	public void setListCircles(List<MyCircles> listCircles) {
		this.listCircles = listCircles;
	}

	public RetError searchCirclesByLongitudey() {
		IParser parser = new MyCircleListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("longitude", MyApplation.getnLontitude());
		params.put("latitude", MyApplation.getnLatitude());
		Result<?> ret = ApiRequest.request(SEARCH_NEAR_CIRCLES_API, params,
				parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CirclesList) {
				CirclesList lists = (CirclesList) ret.getData();
				this.listCircles.addAll(lists.getListCircles());
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
