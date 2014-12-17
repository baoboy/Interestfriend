package com.interestfriend.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.UploadErrorLogTask;
import com.interestfriend.utils.Utils;

public class UpLoadErrorLogService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Utils.print("err:::::::::::::OOOOOOOOOOO");

		if (intent != null) {
			String file_path = intent.getStringExtra("file_path");
			File file = new File(file_path);
			if (!file.exists()) {
				stopSelf();
			} else {
				Utils.print("err:::::::::::::>>>>>" + file.getAbsolutePath());
				uoLoadError(file);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void uoLoadError(File file) {
		UploadErrorLogTask task = new UploadErrorLogTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				stopSelf();
				super.taskFinish(result);
			}
		});
		task.execute(file);
	}
}
