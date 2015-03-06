package com.interestfriend.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.GridView;

import com.interestfriend.R;

public class ChatItemGridView extends GridView {
	private Paint paint;

	public ChatItemGridView(Context context) {
		super(context);
	}

	public ChatItemGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(context.getResources().getColor(R.color.color_d5));
		// paint.setStrokeWidth((float) 2);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount();
		int columns = getNumColumns();
		// 计算行数
		int yu = count % columns;
		int row = count / columns;
		if (yu != 0) {
			row = row + 1;
		}
		int width = getWidth();
		int height = getHeight();
		for (int x = 0; x < width; x += width / columns) {
			if (x == 0 || x > width - columns)// 第一条和最后一条不画
				continue;
			canvas.drawLine(x, 0, x, height, paint);// 画竖线
		}
		for (int y = 0; y < height; y += height / row) {
			if (y == 0 || y > height - row)// 第一条和最后一条不画
				continue;
			canvas.drawLine(0, y, width, y, paint);// 划横线
		}
		super.dispatchDraw(canvas);
	}

}
