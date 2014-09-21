package com.interestfriend.parser;

import org.json.JSONObject;

import com.interestfriend.data.result.Result;

public interface IParser {
	public Result parse(JSONObject jsonObj) throws Exception;
}
