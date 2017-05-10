package com.ibm.ischool.view;

import com.ibm.ischool.ui.view.HoloCircularProgressBar;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public interface ILessonDetailView extends IBaseView {

	void setTitleText(String title);
	
	void setBackBtnVisiable(int visiablity);

	void setViewPagerAdapter(PagerAdapter adapter);
	
	/**
	 * 向LinearLayout中增加小圆点
	 * @param childView
	 */
	void addCircleView(View childView);
	
	/**
	 * 获取小圆点View
	 * @return
	 */
	View getCircelChildView(int index);

	/**
	 * 设置标准读音布局可见性
	 */
	void setVoiceLayoutVisible(int visibility);

	/**
	 * 获取标准音进度条
	 * @return
	 */
	HoloCircularProgressBar getVoiceProgressBar();
	
	/**
	 * 获取录音进度条
	 * @return
	 */
	HoloCircularProgressBar getRecordProgressBar();
	
	/**
	 * 设置标准语音按钮图
	 * @param resourceId
	 */
	void setVoiceImageView(int resourceId);
	
	/**
	 * 设置录音按钮图
	 * @param resourceId
	 */
	void setRecordImageView(int resourceId);
	
	/**
	 * 设置播放录音布局的可见性
	 * @param visibility
	 */
	void setRecordLayoutVisible(int visibility);
	
}
