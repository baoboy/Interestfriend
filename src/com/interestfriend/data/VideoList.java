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

	private String refushTime = "0";
	private int refushState = 1;// 1 上拉刷新 2 加载更多

	public VideoList(int cid) {
		this.cid = cid;
	}

	public String getRefushTime() {
		return refushTime;
	}

	public void setRefushTime(String refushTime) {
		this.refushTime = refushTime;
	}

	public int getRefushState() {
		return refushState;
	}

	public void setRefushState(int refushState) {
		this.refushState = refushState;
	}

	public List<Video> getVideos() {
		sort();
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

	private void sort() {
		Collections.sort(videos, new Comparator<Video>() {
			@Override
			public int compare(Video lhs, Video rhs) {
				return rhs.getTime().compareTo(lhs.getTime());
			}
		});

	}

	public RetError refushVideo() {
		IParser parser = new VideoListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("refushTime", refushTime);
		params.put("refushState", refushState);
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
	public void read(SQLiteDatabase db) {

		Cursor cursor = db.query(Const.VIDEO_TABLE_NAME, new String[] { "cid",
				"video_id", "video_size", "publisher_id", "video_duration",
				"publisher_name", "publisher_avatar", "video_img",
				"video_path", "time" }, null, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int j = 0; j < cursor.getCount(); j++) {
				int cid = cursor.getInt(cursor.getColumnIndex("cid"));
				int video_id = cursor.getInt(cursor.getColumnIndex("video_id"));
				int publisher = cursor.getInt(cursor
						.getColumnIndex("publisher_id"));
				int video_size = cursor.getInt(cursor
						.getColumnIndex("video_size"));
				int video_duration = cursor.getInt(cursor
						.getColumnIndex("video_duration"));
				String video_img = cursor.getString(cursor
						.getColumnIndex("video_img"));
				String video_path = cursor.getString(cursor
						.getColumnIndex("video_path"));
				String publisher_name = cursor.getString(cursor
						.getColumnIndex("publisher_name"));
				String publisher_avatar = cursor.getString(cursor
						.getColumnIndex("publisher_avatar"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				Video video = new Video();
				video.setCid(cid);
				video.setVideo_id(video_id);
				video.setTime(time);
				video.setPublisher_id(publisher);
				video.setPublisher_avatar(publisher_avatar);
				video.setPublisher_name(publisher_name);
				video.setVideo_duration(video_duration);
				video.setVideo_img(video_img);
				video.setVideo_path(video_path);
				video.setVideo_size(video_size);

				// read comment

				List<VideoComment> comments = new ArrayList<VideoComment>();

				Cursor cursor3 = db.query(Const.VIDEO_COMMENT_TABLE_NAME,
						new String[] { "comment_id", "comment_time",
								"comment_content", "publisher_id",
								"publisher_name", "publisher_avatar" },
						"video_id=?", new String[] { video_id + "" }, null,
						null, null);
				if (cursor3.getCount() > 0) {
					cursor3.moveToFirst();
					for (int i = 0; i < cursor3.getCount(); i++) {
						int comment_id = cursor3.getInt(cursor3
								.getColumnIndex("comment_id"));
						String comment_time = cursor3.getString(cursor3
								.getColumnIndex("comment_time"));
						String comment_content = cursor3.getString(cursor3
								.getColumnIndex("comment_content"));
						int publisher_id = cursor3.getInt(cursor3
								.getColumnIndex("publisher_id"));
						publisher_name = cursor3.getString(cursor3
								.getColumnIndex("publisher_name"));
						publisher_avatar = cursor3.getString(cursor3
								.getColumnIndex("publisher_avatar"));
						VideoComment comment = new VideoComment();
						comment.setComment_content(comment_content);
						comment.setComment_id(comment_id);
						comment.setPublisher_name(publisher_name);
						comment.setPublisher_avatar(publisher_avatar);
						comment.setComment_time(comment_time);
						comment.setPublisher_id(publisher_id);
						comments.add(comment);
						cursor3.moveToNext();
					}
					sortComment(comments);
					video.setComments(comments);
					int index = comments.size() > 2 ? 2 : comments.size();
					for (int i = 0; i < index; i++) {
						video.getCommentsListView().add(comments.get(i));
					}
				}
				cursor3.close();
				videos.add(video);
				cursor.moveToNext();
			}
			cursor.close();
		}
	}

	public void sortComment(List<VideoComment> comments) {
		Collections.sort(comments, new Comparator<VideoComment>() {
			@Override
			public int compare(VideoComment lhs, VideoComment rhs) {
				return rhs.getComment_time().compareTo(lhs.getComment_time());
			}
		});

	}

	@Override
	public void write(SQLiteDatabase db) {
		for (Video video : videos) {
			video.write(db);
		}
		writeVideos.clear();
	}

}
