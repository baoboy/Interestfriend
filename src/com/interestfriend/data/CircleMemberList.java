package com.interestfriend.data;

import java.util.ArrayList;
import java.util.List;

public class CircleMemberList {
	private static final String CIRCLE_MEMBER_LIST_API = "GetMembersByCircleIDServlet";
	private final static int MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER = 500;

	private int circle_id = 0;

	private List<CircleMember> circleMemberLists = new ArrayList<CircleMember>();

	public List<CircleMember> getCircleMemberLists() {
		return circleMemberLists;
	}

	public void setCircleMemberLists(List<CircleMember> circleMemberLists) {
		this.circleMemberLists = circleMemberLists;
	}

	public void setCid(int circle_id) {
		this.circle_id = circle_id;
	}

	private void getCircleMemberList() {

	}
}
