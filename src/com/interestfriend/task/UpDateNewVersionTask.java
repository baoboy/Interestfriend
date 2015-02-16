package com.interestfriend.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.interestfriend.utils.HttpUrlHelper;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;

public class UpDateNewVersionTask extends AsyncTask<String, Integer, Integer> {
	// private String version = "";
	private String serverVersion = "";
	private String versionLink = "";
	private String version_info = "";
	private UpDateVersion callBack;
	private boolean isShowToast;
	private int version_code;
	private Context mContext;

	public UpDateNewVersionTask(Context mContext, boolean isShowToast) {
		// version = Utils.getVersionName(mContext);
		this.isShowToast = isShowToast;
		this.mContext = mContext;
	}

	@Override
	protected Integer doInBackground(String... params) {

		Map<String, Object> map = new HashMap<String, Object>();
		String result = HttpUrlHelper.postData(map, "APPVersionUpdateServlet");
		if (result == null || "".equals(result)) {
			return 0;
		}
		try {
			JSONObject json = new JSONObject(result);
			int ret = json.getInt("rt");
			serverVersion = json.getString("app_version");
			versionLink = json.getString("app_link");
			version_info = json.getString("version_info");
			if (json.has("app_version_code")) {
				serverVersion = json.getString("app_version_name");
				version_code = json.getInt("app_version_code");
			}
			return ret;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (result == 0) {
			if (isShowToast) {
				ToastUtil.showToast("操作失败,请稍候再试", Toast.LENGTH_SHORT);
			}
			this.callBack.getNewVersion(0, "", "", "");
			return;
		}
		if (version_code <= Utils.getVersionCode(mContext)) {
			if (isShowToast) {
				ToastUtil.showToast("您现在用的已经是最新版，最最新版值得您期待！",
						Toast.LENGTH_SHORT);

			}
			this.callBack.getNewVersion(0, "", "", "");
			return;
		}
		// if (serverVersion.compareTo(version) <= 0) {
		// if (isShowToast) {
		// ToastUtil.showToast("您现在用的已经是最新版，最最新版值得您期待！",
		// Toast.LENGTH_SHORT);
		//
		// }
		// this.callBack.getNewVersion(0, "", "", "");
		// return;
		// }
		this.callBack.getNewVersion(result, "检测到新版本\n\n" + serverVersion,
				versionLink, version_info);
	}

	public UpDateVersion getCallBack() {
		return callBack;
	}

	public void setCallBack(UpDateVersion callBack) {
		this.callBack = callBack;
	}

	public interface UpDateVersion {
		void getNewVersion(int rt, String versionCode, String link,
				String version_info);
	}
}
