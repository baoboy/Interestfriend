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
import com.interestfriend.parser.MyCircleListParser;

public class CategoryCircleList {
	private List<CategoryCircle> cateLists = new ArrayList<CategoryCircle>();

	private static final String API = "GetCircleCategoryServlet";
	private static final String GET_CIRCLES_BY_CATEGORY = "GetCirclesByCategoryServlet";

	private int category = 0;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();

	public List<MyCircles> getListCircles() {
		return listCircles;
	}

	public void setListCircles(List<MyCircles> listCircles) {
		this.listCircles = listCircles;
	}

	public List<CategoryCircle> getCateLists() {
		return cateLists;
	}

	public void setCateLists(List<CategoryCircle> cateLists) {
		this.cateLists = cateLists;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
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

	public RetError searchCirclesByCategory(int page) {
		IParser parser = new MyCircleListParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("category", category);
		params.put("page", page);
		Result<?> ret = ApiRequest.request(GET_CIRCLES_BY_CATEGORY, params,
				parser);
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
}
