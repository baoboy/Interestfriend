package com.interestfriend.data;

import java.util.ArrayList;
import java.util.List;

public class UserFriendList {
	private List<ChatUser> lists = new ArrayList<ChatUser>();

	public List<ChatUser> getLists() {
		return lists;
	}

	public void setLists(List<ChatUser> lists) {
		this.lists = lists;
	}
}
