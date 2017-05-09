package com.ibm.ischool.presenter;

public interface ILessonDetailPresenter {

	void initView();
	
	/**
	 * 开始录音
	 */
	void startRecord();
	
	/**
	 * 结束录音
	 */
	void stopRecord(boolean upload);
	
	/**
	 * 删除录音文件
	 */
	void removeRecordFile();
	
	/**
	 * 设置当前小圆点位置
	 * @param position
	 */
	void setCurrentCardCirclePosition(int position);
	
	/**
	 * Activity执行onDestroy执行的操作
	 */
	void onActivityDestroy();
	
	/**
	 * 点击标准语音时的操作
	 */
	void onVoiceImageClick();
	
	/**
	 * 点击播放录音文件时的操作
	 */
	void onRecordImageClick();
	
}
