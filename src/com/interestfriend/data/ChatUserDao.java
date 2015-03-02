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

import com.interestfriend.db.DBUtils;

public class ChatUserDao {
	public static final String TABLE_NAME = "chat_uers";
	public static final String COLUMN_USER_NAME = "use_rname";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_USER_AVATAR = "use_avatar";
	public static final String COLUMN_USER_CHAT_ID = "user_chat_id";
	public static final String COLUMN_USER_FROM_CIRCLE = "user_from_circle";

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public List<ChatUser> getContactList() {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		List<ChatUser> lists = new ArrayList<ChatUser>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String user_name = cursor.getString(cursor
						.getColumnIndex(COLUMN_USER_NAME));
				String user_avatar = cursor.getString(cursor
						.getColumnIndex(COLUMN_USER_AVATAR));
				int user_id = cursor.getInt(cursor
						.getColumnIndex(COLUMN_USER_ID));
				String user_chat_id = cursor.getString(cursor
						.getColumnIndex(COLUMN_USER_CHAT_ID));
				String user_from_circle = cursor.getString(cursor
						.getColumnIndex(COLUMN_USER_FROM_CIRCLE));
				ChatUser user = new ChatUser();
				user.setUser_avatar(user_avatar);
				user.setUser_chat_id(user_chat_id);
				user.setUser_name(user_name);
				user.setUser_id(user_id);
				user.setUser_friend_circle(user_from_circle);
				lists.add(user);
			}
			cursor.close();
		}
		return lists;
	}

	/**
	 * 删除�?��联系�? * @param username
	 */
	public void deleteContact(String username) {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_USER_CHAT_ID + " = ?",
					new String[] { username });
		}
	}

	/**
	 * 保存�?��联系�? * @param user
	 */
	public void saveContact(ChatUser user) {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		ContentValues values = new ContentValues();
		values.put(COLUMN_USER_NAME, user.getUser_name());
		values.put(COLUMN_USER_AVATAR, user.getUser_avatar());
		values.put(COLUMN_USER_ID, user.getUser_id());
		values.put(COLUMN_USER_CHAT_ID, user.getUser_chat_id());
		values.put(COLUMN_USER_FROM_CIRCLE, user.getUser_friend_circle());
		if (db.isOpen()) {
			db.replace(TABLE_NAME, null, values);
		}
	}
}
