package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.GrowthListParser;
import com.interestfriend.parser.IParser;

public class GrowthList {
	private final String GROWTH_LIST_API = "GetGrowthListServlet";

	private List<Growth> growths = new ArrayList<Growth>();

	private int cid = 0;

	public List<Growth> getGrowths() {
		return growths;
	}

	public void setGrowths(List<Growth> growths) {
		this.growths = growths;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public RetError getGrowthList() {
		IParser parser = new GrowthListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		Result ret = ApiRequest.request(GROWTH_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			GrowthList lists = (GrowthList) ret.getData();
			this.growths.addAll(lists.getGrowths());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
