package com.interestfriend.data;

import java.io.Serializable;

import com.interestfriend.db.Const;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Image in growth.
 * 
 */
public class GrowthImage extends AbstractData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cid = 0;
	private int gid = 0;
	private int imgId = 0;
	private String img = "";

	public GrowthImage() {
	}

	public GrowthImage(int cid, int gid, int imgId) {
		this(cid, gid, imgId, "");
	}

	public GrowthImage(int cid, int gid, int imgId, String img) {
		this.cid = cid;
		this.gid = gid;
		this.imgId = imgId;
		this.img = img;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "GrowthImage [cid=" + cid + ", gid=" + gid + ", imgId=" + imgId
				+ ", img=" + img + "]";
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.GROWTH_IMAGE_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("cid", cid);
		cv.put("growth_id", gid);
		cv.put("img_id", imgId);
		cv.put("img", img);
		db.insert(dbName, null, cv);
	}

}
