/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huanxin.helper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.activity.ChatActivity;
import com.interestfriend.activity.DissolveCircleActivity;
import com.interestfriend.activity.FriendVertifyActivity;
import com.interestfriend.activity.HomeActivity;
import com.interestfriend.activity.JoinCircleActivity;
import com.interestfriend.activity.KickOutActivity;
import com.interestfriend.activity.ReceiveJoinCircleActivity;
import com.interestfriend.activity.RefuseJoinCircleActivity;
import com.interestfriend.activity.RefushXinQingCommentActivity;
import com.interestfriend.data.InviteMessage;
import com.interestfriend.receive.CallReceiver;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * 
 * @author easemob
 * 
 */
public class QuYouHXSDKHelper extends HXSDKHelper {

	@Override
	protected void initHXOptions() {
		super.initHXOptions();
		// you can also get EMChatOptions to set related SDK options
		// EMChatOptions options = EMChatManager.getInstance().getChatOptions();
	}

	@Override
	protected OnMessageNotifyListener getMessageNotifyListener() {
		// 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		return new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				if (Constants.GROWTH_USER_ID.equals(message.getFrom())) {
					return "有人更新了动态快去看看吧";

				}
				if (Utils.isSystemUser(message.getFrom())) {
					return "系统通知";
				}
				String user_name = "";
				try {
					user_name = message.getStringAttribute("from_user_name");
				} catch (EaseMobException e) {
					try {
						user_name = message.getStringAttribute("user_name");
					} catch (EaseMobException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				return "你的趣友 " + user_name + " 发来了一条消息";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				if (Constants.GROWTH_USER_ID.equals(message.getFrom())) {
					return "有人更新了动态快去看看吧";

				}
				if (Utils.isSystemUser(message.getFrom())) {
					return "系统通知";
				}
				String user_name = "";
				String circle_name = "";
				try {
					user_name = message.getStringAttribute("from_user_name");
					circle_name = message.getStringAttribute("circle_name");
				} catch (EaseMobException e) {
					e.printStackTrace();
					try {
						user_name = message.getStringAttribute("user_name");
						circle_name = message.getStringAttribute("circle_name");
					} catch (EaseMobException e1) {
						e1.printStackTrace();
					}
				}
				return "'" + user_name + "' 发来了" + messageNum + "条消息。"
						+ " 来自 '" + circle_name + "' 圈子";

			}

			@Override
			public String onSetNotificationTitle(EMMessage message) {
				// 修改标题
				return "趣友";
			}

			@Override
			public int onSetSmallIcon(EMMessage arg0) {
				return R.drawable.app_icon_small;
			}
		};
	}

	@Override
	protected OnNotificationClickListener getNotificationClickListener() {
		return new OnNotificationClickListener() {

			@SuppressLint("InlinedApi")
			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = null;
				ChatType chatType = message.getChatType();
				String username = message.getFrom();
				if (chatType == ChatType.Chat) { // 单聊信息
					if (Constants.JOIN_CIRCLE_USER_ID.equals(username)) {
						intent = new Intent(appContext,
								JoinCircleActivity.class);
					} else if (Constants.RECEIVE_JOIN_CIRCLE_USER_ID
							.equals(username)) {
						intent = new Intent(appContext,
								ReceiveJoinCircleActivity.class);
					} else if (Constants.REFUSE_JON_CIRCLE_USER_ID
							.equals(username)) {
						intent = new Intent(appContext,
								RefuseJoinCircleActivity.class);
					} else if (Constants.PRAISE_USER_ID.equals(username)) {
						intent = new Intent(appContext, HomeActivity.class);
					} else if (Constants.KICK_OUT_USER_ID.equals(username)) {
						intent = new Intent(appContext, KickOutActivity.class);
					} else if (Constants.XINQING_PRAISE_AND_COMMENT_USER_ID
							.equals(username)) {
						intent = new Intent(appContext,
								RefushXinQingCommentActivity.class);
					} else if (Constants.ADD_USER_FRIEND_INVITE
							.equals(username)) {
						String reason = "";
						int user_friend_id = 0;
						String user_friend_name = "";
						String user_firend_avatar = "";
						String from_circle = "";
						try {
							user_friend_id = Integer.valueOf(message
									.getIntAttribute("user_friend_id"));
							reason = message.getStringAttribute("reason");
							user_friend_name = message
									.getStringAttribute("user_friend_name");
							user_firend_avatar = message
									.getStringAttribute("user_firend_avatar");
							from_circle = message
									.getStringAttribute("from_circle");
						} catch (EaseMobException e) {
							e.printStackTrace();

						}
						InviteMessage i_message = new InviteMessage();
						i_message.setFrom_circle(from_circle);
						i_message.setFrom_user_avatar(user_firend_avatar);
						i_message.setFrom_user_id(user_friend_id);
						i_message.setFrom_user_name(user_friend_name);
						i_message.setReason(reason);
						intent = new Intent(appContext, HomeActivity.class);
						intent.putExtra("message", i_message).putExtra(
								"msg_id", message.getMsgId());

					} else {
						intent = new Intent(appContext, ChatActivity.class);
						intent.putExtra("chatType",
								ChatActivity.CHATTYPE_SINGLE);
						try {
							String user_name = "";
							String user_avatar = "";
							int user_id = message.getIntAttribute("user_id");
							user_name = message
									.getStringAttribute("from_user_name");
							user_avatar = message
									.getStringAttribute("from_user_avatar");
							intent.putExtra("user_name", user_name);
							intent.putExtra("user_avatar", user_avatar);
							intent.putExtra("user_id", user_id);
						} catch (EaseMobException e) {
							e.printStackTrace();
						}
					}
					intent.putExtra("userId", message.getFrom());

				} else {
					if (Constants.DISSOLVE_CIRCLE_USER_ID.equals(username)) {
						intent = new Intent(appContext,
								DissolveCircleActivity.class);
						intent.putExtra("userId", message.getTo());

					} else {
						intent = new Intent(appContext, HomeActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					}
				}
				return intent;
			}
		};
	}

	@Override
	protected void onConnectionConflict() {
		Intent intent = new Intent(appContext, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("conflict", true);
		appContext.startActivity(intent);
	}

	@Override
	protected void initListener() {
		super.initListener();
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance()
				.getIncomingCallBroadcastAction());
		appContext.registerReceiver(new CallReceiver(), callFilter);
	}

	@Override
	public void logout(final EMCallBack callback) {
		super.logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				SharedUtils.saveHuanXinPassword(null);
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

		});
	}
}
