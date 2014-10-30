package com.interestfriend.task;

import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.GrowthList;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.db.DBUtils;

public class GetGrowthListTask extends
		BaseAsyncTask<GrowthList, Void, RetError> {
	private GrowthList list;

	@Override
	protected RetError doInBackground(GrowthList... params) {
		list = params[0];
		RetError ret = list.refushGrowth();
		long time = System.currentTimeMillis();
		System.out.println("");
		if (ret == RetError.NONE) {
			SQLiteDatabase db = DBUtils.getDBsa(2);
			db.beginTransaction();
			list.writeGrowth(db);
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		System.out.println("time:::::::::::::::"
				+ (System.currentTimeMillis() - time));
		return ret;
	}

}
