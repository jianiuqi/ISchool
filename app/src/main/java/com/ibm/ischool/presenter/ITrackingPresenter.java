package com.ibm.ischool.presenter;

/**
 * Created by jnq on 2017/5/9.
 */

public interface ITrackingPresenter {

    void initView();

    /**
     * 开始录音
     */
    void startRecord();

    /**
     * 结束录音
     */
    void stopRecord();

    /**
     * 删除录音文件
     */
    void removeRecordFile();

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
