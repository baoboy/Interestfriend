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

import com.interestfriend.activity.VideoCallActivity;
import com.interestfriend.activity.VoiceCallActivity;
import com.interestfriend.data.CircleMember;
import com.interestfriend.db.DBUtils;

public class CallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// 拨打方username
		String from = intent.getStringExtra("from");
		CircleMember member = new CircleMember();
		member.setUser_chat_id(from);
		member.getUserDetailByUserChatId(DBUtils.getDBsa(2));
		// call type
		String type = intent.getStringExtra("type");
		if ("video".equals(type)) { // 视频通话
			context.startActivity(new Intent(context, VideoCallActivity.class)
					.putExtra("user_chat_id", from)
					.putExtra("isComingCall", true)
					.putExtra("call_user_avatar", member.getUser_avatar())
					.putExtra("call_user_name", member.getUser_name())
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} else { // 音频通话
			context.startActivity(new Intent(context, VoiceCallActivity.class)
					.putExtra("user_chat_id", from)
					.putExtra("isComingCall", true)
					.putExtra("call_user_name", member.getUser_name())
					.putExtra("call_user_avatar", member.getUser_avatar())
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
	}

}
