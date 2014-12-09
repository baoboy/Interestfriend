package com.interestfriend.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.interestfriend.showbigpic.ImagePagerActivity;
import com.interestfriend.utils.Constants;

public class ShowBigAvatariListener implements OnClickListener {
	private String avatar_path;
	private Context mContext;

	public ShowBigAvatariListener(Context mContext, String avatar_path) {
		this.avatar_path = avatar_path;
		this.mContext = mContext;
	}

	@Override
	public void onClick(View arg0) {
		List<String> imgUrl = new ArrayList<String>();
		imgUrl.add(avatar_path);
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
		mContext.startActivity(intent);
	}

}
