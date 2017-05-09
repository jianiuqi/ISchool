package com.ibm.ischool.presenter;

public interface IChapterPresenter {

	void initView();
	
	/**
	 * 设置当前小圆点位置
	 * @param position
	 */
	void setCurrentCardCirclePosition(int position);
	
	/**
	 * 可时刷新数据
	 */
	void refresh();
}
