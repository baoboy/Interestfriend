package com.interestfriend.task;

import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.enums.RetError;

public class SearchCirclesByCategoryTask extends
		BaseAsyncTask<CategoryCircleList, Void, RetError> {
	private CategoryCircleList list;

	@Override
	protected RetError doInBackground(CategoryCircleList... params) {
		list = params[0];
		RetError ret = list.searchCirclesByCategory();
		return ret;
	}

}
