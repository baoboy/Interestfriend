package com.interestfriend.task;

import android.os.AsyncTask;

import com.interestfriend.interfaces.AbstractTaskPostCallBack;

public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	private AbstractTaskPostCallBack<Result> mCallBack;

	public void setmCallBack(AbstractTaskPostCallBack<Result> mCallBack) {
		this.mCallBack = mCallBack;
	}

	@Override
	protected void onPostExecute(Result result) {
		System.out.println("result:::::::::::;" + result);
		if (mCallBack != null) {
			mCallBack.taskFinish(result);
		}
		// if (result instanceof RetError) {
		// if (result == RetError.NONE) {
		// ToastUtil.showToast(RetError.toText(((RetError) result)),
		// Toast.LENGTH_SHORT);
		// }
		// if (result == RetError.UNKOWN || result == RetError.INVALID) {
		// ToastUtil.showToast("啊哦，很抱歉没有成功，请确认是否是网络的缘故！",
		// Toast.LENGTH_SHORT);
		// } else {
		// ToastUtil.showToast(RetError.toText(((RetError) result)),
		// Toast.LENGTH_SHORT);
		// }
		// }

	}

}
