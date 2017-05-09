package com.ibm.ischool.presenter;

public interface ILevelFragmentPresenter {
	
	void initView();
	
	/**
	 * 开始第一级别课程
	 */
	void startChapter(int level);
	
	/**
	 * 刷新进度和星星个数
	 */
	void refreshStar();
}
