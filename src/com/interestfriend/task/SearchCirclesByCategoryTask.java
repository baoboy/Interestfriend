package com.interestfriend.task;

import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.enums.RetError;

public class SearchCirclesByCategoryTask extends
		BaseAsyncTask<CategoryCircleList, Void, RetError> {
	private CategoryCircleList list;
	private int page;

	public SearchCirclesByCategoryTask(int page) {
		this.page = page;
	}

	@Override
	protected RetError doInBackground(CategoryCircleList... params) {
		list = params[0];
		RetError ret = list.searchCirclesByCategory(page);
		return ret;
	}

}
