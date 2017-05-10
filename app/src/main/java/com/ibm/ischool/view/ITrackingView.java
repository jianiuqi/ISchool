package com.ibm.ischool.view;

import com.ibm.ischool.ui.view.HoloCircularProgressBar;

/**
 * Created by jnq on 2017/5/9.
 */

public interface ITrackingView extends IBaseView {

    void setTitleText(String title);

    void setBackBtnVisiable(int visiablity);

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
     * 设置标准读音布局可见性
     * @param visibility
     */
    void setVoiceLayoutVisible(int visibility);

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
