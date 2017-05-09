package com.ibm.ischool.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public interface IChapterView {

	void setTitleText(String title);
	
	void setBackBtnVisiable(int visiablity);
	
	void setPagerAdapter(PagerAdapter adapter);
	
	void setOffscreenPageLimit(int size);
	
	/**
	 * 向LinearLayout中增加小圆点
	 * @param childView
	 */
	void addCardCircleView(View childView);
	
	/**
	 * 获取小圆点View
	 * @return
	 */
	View getCardCircelChildView(int index);
	
	void removeCircleViews();
}
