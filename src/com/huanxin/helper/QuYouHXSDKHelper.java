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
import com.interestfriend.activity.HomeActivity;
import com.interestfriend.activity.JoinCircleActivity;
import com.interestfriend.activity.KickOutActivity;
import com.interestfriend.activity.MainActivity;
import com.interestfriend.activity.ReceiveJoinCircleActivity;
import com.interestfriend.activity.RefuseJoinCircleActivity;
import com.interestfriend.receive.VoiceCallReceiver;
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
		// ȡ��ע�ͣ�app�ں�̨��������Ϣ��ʱ��״̬������Ϣ��ʾ�����Լ�д��
		return new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				if (Constants.GROWTH_USER_ID.equals(message.getFrom())) {
					return "���˸����˶�̬��ȥ������";

				}
				if (Utils.isSystemUser(message.getFrom())) {
					return "ϵͳ֪ͨ";
				}
				String user_name = "";
				try {
					user_name = message.getStringAttribute("from_user_name");
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				return "���Ȥ�� " + user_name + " ������һ����Ϣ";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				if (Constants.GROWTH_USER_ID.equals(message.getFrom())) {
					return "���˸����˶�̬��ȥ������";

				}
				if (Utils.isSystemUser(message.getFrom())) {
					return "ϵͳ֪ͨ";
				}
				String user_name = "";
				String circle_name = "";
				try {
					user_name = message.getStringAttribute("from_user_name");
					circle_name = message.getStringAttribute("circle_name");
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				return "'" + user_name + "' ������" + messageNum + "����Ϣ��"
						+ " ���� '" + circle_name + "' Ȧ��";

			}

			@Override
			public String onSetNotificationTitle(EMMessage message) {
				// �޸ı���
				return "Ȥ��";
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
				if (chatType == ChatType.Chat) { // ������Ϣ
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
				.getIncomingVoiceCallBroadcastAction());
		appContext.registerReceiver(new VoiceCallReceiver(), callFilter);
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
