package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.CircleMemberListParser;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.UserFriendsParser;

public class UserFriendList extends AbstractData {

	private static final String USER_FRIENDS_API = "GetUserFriendServlet";

	private List<ChatUser> lists = new ArrayList<ChatUser>();

	public List<ChatUser> getLists() {
		return lists;
	}

	public void setLists(List<ChatUser> lists) {
		this.lists = lists;
	}

	public RetError getUserFriendList() {
		IParser parser = new UserFriendsParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Result<?> ret = ApiRequest.request(USER_FRIENDS_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			if (ret.getData() instanceof UserFriendList) {
				UserFriendList lists = (UserFriendList) ret.getData();
				this.lists.addAll(lists.getLists());
			}
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		db.beginTransaction();
		db.delete(ChatUserDao.TABLE_NAME, null, null);
		for (ChatUser user : lists) {
			ChatUserDao dao = new ChatUserDao();
			dao.saveContact(user, db);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
