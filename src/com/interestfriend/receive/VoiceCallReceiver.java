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

package com.interestfriend.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChatManager;
import com.interestfriend.activity.VoiceCallActivity;
import com.interestfriend.data.CircleMember;
import com.interestfriend.db.DBUtils;

public class VoiceCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!intent.getAction().equals(
				EMChatManager.getInstance()
						.getIncomingVoiceCallBroadcastAction()))
			return;

		// ²¦´̣·½username
		String from = intent.getStringExtra("from");
		CircleMember mbmer = new CircleMember();
		mbmer.setUser_chat_id(from);
		mbmer.getNameAndAvatarByUserChatId(DBUtils.getDBsa(1));
		context.startActivity(new Intent(context, VoiceCallActivity.class)
				.putExtra("username", from).putExtra("isComingCall", true)
				.putExtra("user_name", mbmer.getUser_name())
				.putExtra("user_avatar", mbmer.getUser_avatar())
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}
