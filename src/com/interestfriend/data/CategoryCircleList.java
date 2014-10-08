package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.CategoryCircleParser;
import com.interestfriend.parser.IParser;

public class CategoryCircleList {
	private List<CategoryCircle> cateLists = new ArrayList<CategoryCircle>();

	private static final String API = "GetCircleCategoryServlet";

	public List<CategoryCircle> getCateLists() {
		return cateLists;
	}

	public void setCateLists(List<CategoryCircle> cateLists) {
		this.cateLists = cateLists;
	}

	public RetError refush() {
		IParser parser = new CategoryCircleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Result<?> ret = ApiRequest.request(API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof CategoryCircleList) {
				CategoryCircleList lists = (CategoryCircleList) ret.getData();
				this.cateLists = lists.getCateLists();
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
