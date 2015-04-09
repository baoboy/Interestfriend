package com.interestfriend.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.MapResult;
import com.interestfriend.data.result.Result;
import com.interestfriend.data.result.StringResult;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.MapParser;
import com.interestfriend.parser.StringParser;
import com.interestfriend.parser.XinQingListParser;
import com.interestfriend.parser.XinQingParser;
import com.interestfriend.utils.SharedUtils;

public class XinQing implements Serializable {
	private static final String ADD_XINQING_API = "AddXinQingServlet";
	private static final String PRAISE_XINQING_API = "XinQingPraiseServlet";
	private static final String CANCEL_PRAISE_XINQING_API = "CancelXinQingPraiseServlet";
	private static final String GET_XINQING_BY_ID = "GetXinQingByIDServlet";
	private int xinqing_id = 0;
	private int publisher_id = 0;// 发布者id
	private String content = "";// 内容
	private String publish_time = ""; // 发布时间
	private String publisher_name = "";
	private String publisher_avatar = "";
	private String image_url = "";
	private int praise_count;
	private List<XinQingPraise> praises = new ArrayList<XinQingPraise>();
	private List<XinQingComment> comments = new ArrayList<XinQingComment>();
	private boolean isPraise;

	public boolean isPraise() {
		return isPraise;
	}

	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}

	public List<XinQingPraise> getPraises() {
		return praises;
	}

	public void setPraises(List<XinQingPraise> praises) {
		this.praises = praises;
	}

	public List<XinQingComment> getComments() {
		return comments;
	}

	public void setComments(List<XinQingComment> comments) {
		this.comments = comments;
	}

	public int getPraise_count() {
		return praise_count;
	}

	public void setPraise_count(int praise_count) {
		this.praise_count = praise_count;
	}

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

	public RetError praiseXinQing() {
		IParser parser = new StringParser("praise_count");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xinqing_id", xinqing_id);
		params.put("xinqing_publisher_id", publisher_id);
		params.put("user_name", SharedUtils.getAPPUserName());
		Result ret = ApiRequest.request(PRAISE_XINQING_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			StringResult sr = (StringResult) ret;
			this.praise_count = Integer.valueOf(sr.getStr());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public RetError cancelpraiseGrowth() {
		IParser parser = new StringParser("praise_count");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xinqing_id", xinqing_id);
		Result ret = ApiRequest.request(CANCEL_PRAISE_XINQING_API, params,
				parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			StringResult sr = (StringResult) ret;
			this.praise_count = Integer.valueOf(sr.getStr());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	public RetError refush() {
		IParser parser = new XinQingParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xinqing_id", xinqing_id);
		Result ret = ApiRequest.request(GET_XINQING_BY_ID, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			XinQing xin = (XinQing) ret.getData();
			this.comments = xin.getComments();
			this.content = xin.getContent();
			this.image_url = xin.getImage_url();
			this.isPraise = xin.isPraise();
			this.praise_count = xin.getPraise_count();
			this.praises = xin.getPraises();
			this.publish_time = xin.getPublish_time();
			this.publisher_avatar = xin.getPublisher_avatar();
			this.publisher_name = xin.getPublisher_name();
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
