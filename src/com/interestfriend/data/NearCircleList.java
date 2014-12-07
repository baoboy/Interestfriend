package com.interestfriend.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
		sort();
		return listCircles;
	}

	public void setListCircles(List<MyCircles> listCircles) {
		this.listCircles = listCircles;
	}

	public RetError searchCirclesByLongitudey(double longitude,
			double latitude, int page) {
		IParser parser = new MyCircleListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("page", page);
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

	private void sort() {
		Collections.sort(listCircles, new Comparator<MyCircles>() {
			@Override
			public int compare(MyCircles lhs, MyCircles rhs) {
				if (lhs.getDistance() > rhs.getDistance()) {
					return 1;
				} else if (lhs.getDistance() < rhs.getDistance()) {
					return -1;
				} else {
					return 0;
				}

			}
		});

	}
}
