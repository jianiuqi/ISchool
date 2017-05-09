package com.ibm.ischool.view;

import android.support.v4.app.FragmentActivity;

public interface IWordFragmentView {

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
	
	FragmentActivity getLessonDetailActivity();
	
	/**
	 * 获取第一个星星在屏幕中的位置
	 * @param location
	 */
	void getStar1Location(int[] location);
	
	/**
	 * 获取星星1的宽度
	 * @return
	 */
	int getStar1Width();
	
	/**
	 * 获取星星1的高度
	 * @return
	 */
	int getStar1Height();
	
	/**
	 * 设置星星亮起个数
	 * @param resourceId
	 */
	void setStarNum(int starNum);
	
	/**
	 * 设置发音布局可见性
	 * @param visiablity
	 */
	void setPronounceLayoutVisiable(int visiablity);
	
	/**
	 * 设置单词读音的原始单词
	 * @param text
	 */
	void setWord1RawTextView(CharSequence text);
	
	/**
	 * 设置单词读音的原始单词
	 * @param text
	 */
	void setWord2RawTextView(CharSequence text);
	
	/**
	 * 设置单词正确音标
	 * @param text
	 */
	void setWord1PronounceText(CharSequence text);
	
	/**
	 * 设置单词正确音标
	 * @param text
	 */
	void setWord2PronounceText(CharSequence text);
	
	/**
	 * 设置第二个单词发音布局可见性
	 * @param visiablity
	 */
	void setWord2PronounceLayoutVisiable(int visiablity);
	
	void setMovementMethod();
	
	/**
	 * 获取句子内容
	 * @return
	 */
	CharSequence getSetenceText();
}
