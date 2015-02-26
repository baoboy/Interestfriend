package com.interestfriend.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.interestfriend.db.DBUtils;
import com.interestfriend.utils.Utils;

/**
 * �������ݿ�CircleMember���ContentProvider
 * 
 */
public class MyCircleMemberContentProvider extends ContentProvider {

	private static HashMap<String, String> sMyCirclesProjectionMap;

	private static final int CIRCLES = 1;
	private static final int CIRCLES_ID = 2;

	private static final UriMatcher sUriMatcher;

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MyCircleMemberProvider.MyCircleMemberColumns.TABLE_NAME);

		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			qb.setProjectionMap(sMyCirclesProjectionMap);
			break;

		case CIRCLES_ID:
			qb.setProjectionMap(sMyCirclesProjectionMap);
			qb.appendWhere(MyCircleMemberProvider.MyCircleMemberColumns._ID
					+ "=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = DBUtils.getDBsa(1);
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, null);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			return MyCircleMemberProvider.CONTENT_TYPE;
		case CIRCLES_ID:
			return MyCircleMemberProvider.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if (sUriMatcher.match(uri) != CIRCLES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
		if (values
				.containsKey(MyCircleMemberProvider.MyCircleMemberColumns.USER_ID) == false) {
			values.put(MyCircleMemberProvider.MyCircleMemberColumns.USER_ID, "");
		}

		if (values
				.containsKey(MyCircleMemberProvider.MyCircleMemberColumns.CIRCLE_ID) == false) {
			values.put(MyCircleMemberProvider.MyCircleMemberColumns.CIRCLE_ID,
					0);
		}

		SQLiteDatabase db = DBUtils.getDBsa(2);
		long rowId = db.insert(
				MyCircleMemberProvider.MyCircleMemberColumns.TABLE_NAME,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_NAME, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(
					MyCircleMemberProvider.MyCircleMemberColumns.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		Utils.print("error::::::::::::::::::::" + uri);
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		int count;
		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			count = db.delete(
					MyCircleMemberProvider.MyCircleMemberColumns.TABLE_NAME,
					where, whereArgs);
			break;

		case CIRCLES_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(
					MyCircleMemberProvider.MyCircleMemberColumns.TABLE_NAME,
					MyCircleMemberProvider.MyCircleMemberColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = DBUtils.getDBsa(2);
		int count;
		switch (sUriMatcher.match(uri)) {
		case CIRCLES:
			count = db.update(
					MyCircleMemberProvider.MyCircleMemberColumns.TABLE_NAME,
					values, where, whereArgs);
			break;

		case CIRCLES_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(
					MyCircleMemberProvider.MyCircleMemberColumns.TABLE_NAME,
					values, MyCircleMemberProvider.MyCircleMemberColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// ����ط���membersҪ��CircleMemberColumns.CONTENT_URI��������һ��Segmentһ��
		sUriMatcher
				.addURI(MyCircleMemberProvider.AUTHORITY, "members", CIRCLES);
		sUriMatcher.addURI(MyCircleMemberProvider.AUTHORITY, "members/#",
				CIRCLES_ID);

		sMyCirclesProjectionMap = new HashMap<String, String>();
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns._ID,
				MyCircleMemberProvider.MyCircleMemberColumns._ID);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.CIRCLE_ID,
				MyCircleMemberProvider.MyCircleMemberColumns.CIRCLE_ID);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.PINYIN_FIR,
				MyCircleMemberProvider.MyCircleMemberColumns.PINYIN_FIR);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.SORT_KEY,
				MyCircleMemberProvider.MyCircleMemberColumns.SORT_KEY);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_AVATAR,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_AVATAR);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_BIRTHDAY,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_BIRTHDAY);

		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_CELLPHONE,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_CELLPHONE);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_GENDER,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_GENDER);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_ID,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_ID);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_NAME,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_NAME);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_CHAT_ID,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_CHAT_ID);
		sMyCirclesProjectionMap
				.put(MyCircleMemberProvider.MyCircleMemberColumns.USER_REGISTER_TIME,
						MyCircleMemberProvider.MyCircleMemberColumns.USER_REGISTER_TIME);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_DECLARATION,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_DECLARATION);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_DESCRIPTION,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_DESCRIPTION);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_ADDRESS,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_ADDRESS);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_PROVINCE,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_PROVINCE);
		sMyCirclesProjectionMap.put(
				MyCircleMemberProvider.MyCircleMemberColumns.USER_PROVINCE_KEY,
				MyCircleMemberProvider.MyCircleMemberColumns.USER_PROVINCE_KEY);
	}
}
