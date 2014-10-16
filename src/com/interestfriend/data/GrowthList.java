package com.interestfriend.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.GrowthListParser;
import com.interestfriend.parser.IParser;

public class GrowthList extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String GROWTH_LIST_API = "GetGrowthListServlet";

	private List<Growth> growths = new ArrayList<Growth>();
	private List<Growth> writeGrowths = new ArrayList<Growth>();

	private int cid = 0;

	private String refushTime = "0";
	private int refushState = 1;// 1 上拉刷新 2 加载更多

	public int getRefushState() {
		return refushState;
	}

	public void setRefushState(int refushState) {
		this.refushState = refushState;
	}

	public GrowthList(int cid) {
		this.cid = cid;
	}

	public List<Growth> getWriteGrowths() {
		return writeGrowths;
	}

	public void setWriteGrowths(List<Growth> writeGrowths) {
		this.writeGrowths = writeGrowths;
	}

	public String getRefushTime() {
		return refushTime;
	}

	public void setRefushTime(String refushTime) {
		this.refushTime = refushTime;
	}

	public List<Growth> getGrowths() {
		sort();
		return growths;
	}

	public void setGrowths(List<Growth> growths) {
		this.growths = growths;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	private void sort() {
		Collections.sort(growths, new Comparator<Growth>() {
			@Override
			public int compare(Growth lhs, Growth rhs) {
				return rhs.getPublished().compareTo(lhs.getPublished());
			}
		});

	}

	public RetError refushGrowth() {
		IParser parser = new GrowthListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("refushTime", refushTime);
		params.put("refushState", refushState);
		Result ret = ApiRequest.request(GROWTH_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			GrowthList lists = (GrowthList) ret.getData();
			this.growths.addAll(lists.getGrowths());
			writeGrowths.addAll(lists.getGrowths());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public void writeGrowth(SQLiteDatabase db) {
		for (Growth growth : writeGrowths) {
			growth.write(db);
		}
		writeGrowths.clear();
	}

	@Override
	public void read(SQLiteDatabase db) {
		// read growth basic info
		Cursor cursor = db.query(Const.GROWTHS_TABLE_NAME, new String[] {
				"cid", "growth_id", "content", "publisher_id", "time", }, null,
				null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int j = 0; j < cursor.getCount(); j++) {
				int cid = cursor.getInt(cursor.getColumnIndex("cid"));
				int growth_id = cursor.getInt(cursor
						.getColumnIndex("growth_id"));
				int publisher = cursor.getInt(cursor
						.getColumnIndex("publisher_id"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				Growth growth = new Growth();
				growth.setCid(cid);
				growth.setContent(content);
				growth.setGrowth_id(growth_id);
				growth.setPublished(time);
				growth.setPublisher_id(publisher);

				// read growth images
				List<GrowthImage> images = new ArrayList<GrowthImage>();
				Cursor cursor2 = db.query(Const.GROWTH_IMAGE_TABLE_NAME,
						new String[] { "img_id", "img" }, "growth_id=?",
						new String[] { growth_id + "" }, null, null, null);
				if (cursor2.getCount() > 0) {
					cursor2.moveToFirst();
					for (int i = 0; i < cursor2.getCount(); i++) {
						int imgId = cursor2.getInt(cursor2
								.getColumnIndex("img_id"));
						String img = cursor2.getString(cursor2
								.getColumnIndex("img"));
						GrowthImage image = new GrowthImage(cid, growth_id,
								imgId, img);
						images.add(image);
						cursor2.moveToNext();
					}
					growth.setImages(images);
				}
				cursor2.close();
				growths.add(growth);
				cursor.moveToNext();
			}
			cursor.close();
		}
	}
}
