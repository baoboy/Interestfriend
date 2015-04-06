package com.interestfriend.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.GrowthListParser;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.XinQingListParser;

public class XinQingList {
	private static final String GET_XINQING_LIST_API = "GetXinQingListServlet";
	private List<XinQing> lists = new ArrayList<XinQing>();

	private String refushTime = "0";
	private int refushState = 1;// 1 上拉刷新 2 加载更多

	public String getRefushTime() {
		return refushTime;
	}

	public void setRefushTime(String refushTime) {
		this.refushTime = refushTime;
	}

	public int getRefushState() {
		return refushState;
	}

	public void setRefushState(int refushState) {
		this.refushState = refushState;
	}

	public List<XinQing> getLists() {
		return lists;
	}

	public void setLists(List<XinQing> lists) {
		this.lists = lists;
	}

	private void sort() {
		Collections.sort(lists, new Comparator<XinQing>() {
			@Override
			public int compare(XinQing lhs, XinQing rhs) {
				return rhs.getPublish_time().compareTo(lhs.getPublish_time());
			}
		});

	}

	public RetError refush() {
		IParser parser = new XinQingListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("refushTime", refushTime);
		params.put("refushState", refushState);
		Result ret = ApiRequest.request(GET_XINQING_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			XinQingList lists = (XinQingList) ret.getData();
			if (refushState == 1) {
				this.lists.addAll(0, lists.getLists());
			} else {
				this.lists.addAll(lists.getLists());
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
