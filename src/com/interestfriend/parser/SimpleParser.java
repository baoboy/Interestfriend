package com.interestfriend.parser;

import org.json.JSONObject;

import com.interestfriend.data.result.Result;
import com.interestfriend.data.result.SimpleResult;

public class SimpleParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		return new SimpleResult();
	}

}
