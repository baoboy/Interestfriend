package com.interestfriend.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.db.Const;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.UploadGrowthParser;
import com.interestfriend.utils.BitmapUtils;

public class Growth extends AbstractData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String ADD_GROWTH_API = "AddGrowthServlet";
	private int growth_id = 0;// 成长id
	private int cid = 0;// 所在圈子id
	private int publisher_id = 0;// 发布者id
	private String content = "";// 内容
	private String location = "";// 发布地点
	private String published = ""; // 发布时间
	private List<GrowthImage> images = new ArrayList<GrowthImage>();
	private String tag = "";

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getGrowth_id() {
		return growth_id;
	}

	public void setGrowth_id(int growth_id) {
		this.growth_id = growth_id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public List<GrowthImage> getImages() {
		return images;
	}

	public void setImages(List<GrowthImage> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "gid:" + growth_id + "  content:" + this.content + "   images:"
				+ this.images;
	}

	public RetError uploadForAdd() {
		List<File> bytesimg = new ArrayList<File>();
		for (GrowthImage img : this.images) {
			File file = BitmapUtils.getImageFile(img.getImg());
			if (file == null) {
				continue;
			}
			bytesimg.add(file);
		}
		IParser parser = new UploadGrowthParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("content", content);
		params.put("publisher_id", publisher_id);
		params.put("time", published);
		Result ret = ApiRequest.uploadFileArrayWithToken(ADD_GROWTH_API,
				params, bytesimg, parser);
		delGorwthImgFile(bytesimg);
		if (ret.getStatus() == RetStatus.SUCC) {
			Growth g = (Growth) ret.getData();
			this.growth_id = g.getGrowth_id();
			this.images.clear();
			this.images = g.getImages();

			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void delGorwthImgFile(List<File> files) {
		for (File file : files) {
			if (file.exists()) {
				file.delete();
			}
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.GROWTHS_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("growth_id", this.growth_id);
		cv.put("cid", this.cid);
		cv.put("publisher_id", this.publisher_id);
		cv.put("content", this.content);
		cv.put("time", this.published);
		db.insert(dbName, null, cv);
	}
}
