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
package com.interestfriend.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.InviteMessage.InviteMesageStatus;

public class InviteMessgeDao extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "new_friends_msgs";
	public static final String COLUMN_NAME_ID = "id";
	public static final String COLUMN_NAME_FROM_USER_NAME = "from_user_name";
	public static final String COLUMN_NAME_FROM_USER_AVATAR = "from_user_avatar";
	public static final String COLUMN_NAME_FROM_USER_ID = "from_user_id";
	public static final String COLUMN_NAME_FROM_USER_CHAT_ID = "from_user_chat_id";
	public static final String COLUMN_FROM_CIRCLE = "from_circle";
	public static final String COLUMN_NAME_TIME = "time";
	public static final String COLUMN_NAME_REASON = "reason";
	public static final String COLUMN_NAME_STATUS = "status";
	public static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
	private List<InviteMessage> lists = new ArrayList<InviteMessage>();

	public List<InviteMessage> getLists() {
		return lists;
	}

	public void setLists(List<InviteMessage> lists) {
		this.lists = lists;
	}

	public InviteMessgeDao() {
	}

	public int getInviteMessageCount(SQLiteDatabase db) {
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor.getCount();
	}

	/**
	 * 保存message
	 * 
	 * @param message
	 * @return 返回这条messaged在db中的id
	 */
	public synchronized Integer saveMessage(InviteMessage message,
			SQLiteDatabase db) {
		int id = -1;
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_FROM_USER_NAME, message.getFrom_user_name());
			values.put(COLUMN_NAME_FROM_USER_AVATAR,
					message.getFrom_user_avatar());
			values.put(COLUMN_NAME_FROM_USER_ID, message.getFrom_user_id());
			values.put(COLUMN_NAME_FROM_USER_CHAT_ID,
					message.getFrom_user_chat_id());
			values.put(COLUMN_FROM_CIRCLE, message.getFrom_circle());
			values.put(COLUMN_NAME_REASON, message.getReason());
			values.put(COLUMN_NAME_TIME, message.getTime());
			values.put(COLUMN_NAME_STATUS, message.getStatus().ordinal());
			db.insert(TABLE_NAME, null, values);

			// Cursor cursor = db.rawQuery("select last_insert_rowid() from "
			// + TABLE_NAME, null);
			// if (cursor.moveToFirst()) {
			// id = cursor.getInt(0);
			// }

			// cursor.close();
		}

		return id;
	}

	/**
	 * 更新message
	 * 
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(int msgId, ContentValues values, SQLiteDatabase db) {
		if (db.isOpen()) {
			db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?",
					new String[] { String.valueOf(msgId) });
		}
	}

	/**
	 * 获取messges
	 * 
	 * @return
	 */
	public List<InviteMessage> getMessagesList(SQLiteDatabase db) {
		List<InviteMessage> msgs = new ArrayList<InviteMessage>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from " + TABLE_NAME + " desc", null);
			while (cursor.moveToNext()) {
				InviteMessage msg = new InviteMessage();
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
				String from_user_name = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_FROM_USER_NAME));
				int from_user_id = cursor.getInt(cursor
						.getColumnIndex(COLUMN_NAME_FROM_USER_ID));
				String from_user_chat_id = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_FROM_USER_CHAT_ID));
				String from_user_avatar = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_FROM_USER_AVATAR));
				String reason = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_REASON));
				String from_circle = cursor.getString(cursor
						.getColumnIndex(COLUMN_FROM_CIRCLE));
				long time = cursor.getLong(cursor
						.getColumnIndex(COLUMN_NAME_TIME));
				int status = cursor.getInt(cursor
						.getColumnIndex(COLUMN_NAME_STATUS));
				msg.setId(id);
				msg.setFrom_user_name(from_user_name);
				msg.setFrom_user_avatar(from_user_avatar);
				msg.setFrom_user_id(from_user_id);
				msg.setFrom_user_chat_id(from_user_chat_id);
				msg.setReason(reason);
				msg.setFrom_circle(from_circle);
				msg.setTime(time);
				if (status == InviteMesageStatus.BEINVITEED.ordinal())
					msg.setStatus(InviteMesageStatus.BEINVITEED);
				else if (status == InviteMesageStatus.BEAGREED.ordinal())
					msg.setStatus(InviteMesageStatus.BEAGREED);
				else if (status == InviteMesageStatus.BEREFUSED.ordinal())
					msg.setStatus(InviteMesageStatus.BEREFUSED);
				else if (status == InviteMesageStatus.AGREED.ordinal())
					msg.setStatus(InviteMesageStatus.AGREED);
				else if (status == InviteMesageStatus.REFUSED.ordinal())
					msg.setStatus(InviteMesageStatus.REFUSED);
				else if (status == InviteMesageStatus.BEAPPLYED.ordinal()) {
					msg.setStatus(InviteMesageStatus.BEAPPLYED);
				}
				msgs.add(msg);
				lists.add(msg);
			}
			cursor.close();
		}
		return msgs;
	}

	public void deleteMessage(String from, SQLiteDatabase db) {
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_NAME_FROM_USER_CHAT_ID + " = ?",
					new String[] { from });
		}
	}
}
