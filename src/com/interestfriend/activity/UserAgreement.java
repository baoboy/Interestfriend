package com.interestfriend.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.Utils;

public class UserAgreement extends BaseActivity {
	private ImageView back;
	private TextView titleTxt;
	private WebView wb;
	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_problem);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Utils.rightOut(UserAgreement.this);

			}
		});
		titleTxt = (TextView) findViewById(R.id.title_txt);
		titleTxt.setText("隐私条款");
		wb = (WebView) findViewById(R.id.webView1);
		wb.setWebChromeClient(new WebViewClient());
		wb.loadUrl("http://192.168.20.103:8080/InterestFriend/app/user_agreement.html");
		dialog = DialogUtil.createLoadingDialog(this, "加载中");
		dialog.show();
	}

	private class WebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
			super.onProgressChanged(view, newProgress);
		}
	}

}
