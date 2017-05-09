package com.ibm.ischool.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ScaleImageView extends ImageView{

	public ScaleImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getDrawable();
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = width * drawable.getIntrinsicHeight()/drawable.getIntrinsicWidth();
		setMeasuredDimension(width, height);
	}
}
