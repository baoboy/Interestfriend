package com.interestfriend.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.VideoListParser;

public class VideoList extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String GROWTH_LIST_API = "GetVideoListServlet";

	private List<Video> videos = new ArrayList<Video>();
	private List<Video> writeVideos = new ArrayList<Video>();

	private int cid = 0;

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public List<Video> getWriteVideos() {
		return writeVideos;
	}

	public void setWriteVideos(List<Video> writeVideos) {
		this.writeVideos = writeVideos;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public RetError getGrowthList() {
		IParser parser = new VideoListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		Result ret = ApiRequest.request(GROWTH_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			VideoList lists = (VideoList) ret.getData();
			this.videos.addAll(lists.getVideos());
			this.writeVideos.addAll(lists.getVideos());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		for (Video video : videos) {
			video.write(db);
		}
		writeVideos.clear();
	}

}
