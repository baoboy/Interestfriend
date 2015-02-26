package com.interestfriend.parser;

import org.json.JSONObject;

import com.interestfriend.data.CircleMember;
import com.interestfriend.data.result.Result;
import com.interestfriend.utils.Utils;

public class MemberSelfPaser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}

		String jsonArr = jsonObj.getString("user");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		JSONObject obj = new JSONObject(jsonArr);
		int userID = obj.getInt("userID");
		String userName = obj.getString("userName");
		String userAvatar = obj.getString("userAvatar");
		String userGender = obj.getString("userGender");
		String userBirthday = obj.getString("userBirthday");
		String userRegisterTime = obj.getString("userRegisterTime");
		String pinyinFir = obj.getString("pinYinFir");
		String sortKey = obj.getString("sortKey");
		String user_declaration = obj.getString("userDeclaration");
		String user_description = obj.getString("userDescription");
		String user_chat_id = obj.getString("userChatId");
		String user_address = obj.getString("userAddress");
		String user_province = obj.getString("userProvince");
		String user_province_key = obj.getString("userProvinceKey");
		System.out.println("user::::::::::::" + user_declaration + "    "
				+ userName);
		CircleMember member = new CircleMember();
		member.setUser_id(userID);
		member.setUser_name(userName);
		member.setUser_avatar(userAvatar);
		member.setUser_birthday(userBirthday);
		member.setUser_gender(userGender);
		member.setUser_register_time(userRegisterTime);
		member.setCircle_id(0);
		member.setSortkey(sortKey);
		member.setPinyinFir(pinyinFir);
		member.setUser_declaration(user_declaration);
		member.setUser_description(user_description);
		member.setUser_chat_id(user_chat_id);
		member.setUser_address(user_address);
		member.setUser_province(user_province);
		member.setUser_province_key(user_province_key);
		Result ret = new Result();
		ret.setData(member);
		return ret;
	}
}
