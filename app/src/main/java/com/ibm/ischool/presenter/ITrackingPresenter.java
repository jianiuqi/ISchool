package com.ibm.ischool.presenter;

/**
 * Created by jnq on 2017/5/9.
 */

public interface ITrackingPresenter {

    void initView();

    /**
     * 点击录音按钮
     */
    void onRecordClick();

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
