package com.interestfriend.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class MyRadioButton extends RadioButton {
	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public MyRadioButton(Context context) {
		super(context);
	}

	public MyRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (num == 0) {
			return;
		}
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		canvas.drawCircle(getWidth() / 2 + 30, 20, 11, paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(17);
		canvas.drawText(num + "", getWidth() / 2 + 26, 25, paint);
	}
}
