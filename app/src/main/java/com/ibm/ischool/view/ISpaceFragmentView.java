package com.ibm.ischool.view;

public interface ISpaceFragmentView {

	/**
	 * 设置句子内容
	 * @param text
	 */
	void setSentenceContent(CharSequence text);
	
	/**
	 * 翻译
	 * @param translation
	 */
	void setTranslation(String translation);
	
	/**
	 * 设置句子分数
	 * @param score
	 */
	void setSentenceScore(int score);
	
	/**
	 * 设置星星亮起个数
	 * @param resourceId
	 */
	void setStarNum(int starNum);
}
