package com.ibm.ischool.view;

public interface ILevelFragmentView {

	void setTitleText(String title);
	
	/**
	 * 设置Level的进度和星星数
	 * @param progress
	 * @param starNum
	 * @param levelNum
	 */
	void setLevelProgressAndStar(int progress, int max, CharSequence starNum, int levelNum);
}
