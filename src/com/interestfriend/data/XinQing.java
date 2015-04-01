package com.interestfriend.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.MapResult;
import com.interestfriend.data.result.Result;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MapParser;

public class XinQing {
	private static final String ADD_XINQING_API = "AddXinQingServlet";
	private int xinqing_id = 0;
	private int publisher_id = 0;// 发布者id
	private String content = "";// 内容
	private String publish_time = ""; // 发布时间
	private String publisher_name = "";
	private String publisher_avatar = "";
	private String image_url = "";

	public int getXinqing_id() {
		return xinqing_id;
	}

	public void setXinqing_id(int xinqing_id) {
		this.xinqing_id = xinqing_id;
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

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getPublisher_name() {
		return publisher_name;
	}

	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}

	public String getPublisher_avatar() {
		return publisher_avatar;
	}

	public void setPublisher_avatar(String publisher_avatar) {
		this.publisher_avatar = publisher_avatar;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public RetError uploadForAdd() {
		List<File> bytesimg = new ArrayList<File>();
		File file = new File(image_url);
		if (file.exists()) {
			bytesimg.add(file);
		}
		String[] keys = { "xinqing_id", "image_url" };
		IParser parser = new MapParser(keys);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("content", content);
		Result ret = ApiRequest.uploadFileArrayWithToken(ADD_XINQING_API,
				params, bytesimg, parser);
		delImgFile(bytesimg);
		if (ret.getStatus() == RetStatus.SUCC) {
			MapResult mret = (MapResult) ret;
			image_url = (String) (mret.getMaps().get("image_url"));
			xinqing_id = Integer.valueOf(String.valueOf((mret.getMaps()
					.get("xinqing_id"))));
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void delImgFile(List<File> files) {
		for (File file : files) {
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
