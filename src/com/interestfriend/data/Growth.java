package com.interestfriend.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Growth implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int growth_id = 0;// 成长id
	private int cid = 0;// 所在圈子id
	private int publisher_id = 0;// 发布者id
	private String content = "";// 内容
	private String location = "";// 发布地点
	private String published = ""; // 发布时间
	private List<GrowthImage> images = new ArrayList<GrowthImage>();

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

}
