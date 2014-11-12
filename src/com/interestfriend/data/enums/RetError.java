package com.interestfriend.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum RetError {
	NONE, INVALID, UNKOWN, NOT_EXIST_USER, USER_ALREADY_EXIST, WRONG_PASSWORD, NETWORK_ERROR, CIRCLE_ALERADY_DISSOLVE;

	public static Map<String, RetError> str2Error = new HashMap<String, RetError>();
	static {
		for (RetError err : RetError.values()) {
			str2Error.put(err.name(), err);
		}
	}
	public static Map<String, String> s2t = new HashMap<String, String>();
	static {
		s2t.put("NOT_EXIST_USER", "�û�������");
		s2t.put("USER_ALREADY_EXIST", "�û��Ѵ���");
		s2t.put("WRONG_PASSWORD", "���벻��ȷ");
		s2t.put("NETWORK_ERROR", "���ߣ����粻ͨ�������°ɣ�");
		s2t.put("CIRCLE_ALERADY_DISSOLVE", "Ȧ���Ѿ���ɢ");

	}

	public static RetError convert(String err) {
		if (!str2Error.containsKey(err)) {
			return RetError.UNKOWN;
		} else {
			return str2Error.get(err);
		}
	}

	public static String toText(RetError err) {
		if (s2t.containsKey(err.toString())) {
			return s2t.get(err.toString());
		}
		return "δ֪����";
	}

}
