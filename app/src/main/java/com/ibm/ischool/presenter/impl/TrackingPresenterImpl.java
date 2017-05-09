package com.ibm.ischool.presenter.impl;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.ibm.ischool.R;
import com.ibm.ischool.base.Constant;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.presenter.ITrackingPresenter;
import com.ibm.ischool.util.AudioFileUtils;
import com.ibm.ischool.util.CRLSpeechUtil;
import com.ibm.ischool.util.PlayerUtils;
import com.ibm.ischool.util.StringUtils;
import com.ibm.ischool.view.ITrackingView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnq on 2017/5/9.
 */

public class TrackingPresenterImpl implements ITrackingPresenter{

    ITrackingView mView;

    FragmentActivity mActivity;

    /**
     * 标准音播放器
     */
    PlayerUtils mPlayerUtils;
    /**
     * 录音播放器
     */
    PlayerUtils mRecordPlayer;
    /**
     * 标准录音是否正在播放
     */
    boolean mVoicePlaying = false;
    /**
     * 录音是否正在播放
     */
    boolean mRecordPlaying = false;

    /**
     * 获取的课程内容
     */
    LessonEntity mEntity;

    /**
     * 匹配正确的单词
     */
    private List<String> mRightWords = new ArrayList<>();

    /**
     * 待匹配的单词
     */
    private List<String> mNeedMatchWords = new ArrayList<>();

    public TrackingPresenterImpl(ITrackingView view){
        this.mView = view;
        this.mActivity = (FragmentActivity) view;
    }

    @Override
    public void initView() {
        Intent intent = mActivity.getIntent();
        mEntity = intent.getParcelableExtra(LessonEntity.TAG);
        mView.setBackBtnVisiable(View.VISIBLE);
        mView.setTitleText("Lesson " + mEntity.getLessonNum());
        mView.setSentenceContent(mEntity.getLessonContent());
        mView.setTranslation(mEntity.getTranslation());
        // 初始化播放器
        mPlayerUtils = new PlayerUtils(mView.getVoiceProgressBar());
        mPlayerUtils.setUrlPrepare(Constant.BASE_SERVER_URL + "/" + mEntity.getUrl());
        mPlayerUtils.setOnPlayListener(new TrackingPresenterImpl.VoicePlayerListener());
        mRecordPlayer = new PlayerUtils(mView.getRecordProgressBar());
        mRecordPlayer.setOnPlayListener(new TrackingPresenterImpl.RecordPlayerListener());
        //初始化待匹配的单词
        String[] sentenceWords = mEntity.getLessonContent().split(" ");
        for (String word : sentenceWords) {
            mNeedMatchWords.add(word);
        }
    }

    @Override
    public void startRecord() {
        CRLSpeechUtil.getInstance().startRecog();
        CRLSpeechUtil.getInstance().setListener(new CRLSpeechUtil.OnResultListener() {
            @Override
            public void onSuccess(String content) {
                System.out.println("content:" + content);
                String[] words = content.split(" ");
                int sizeCount = words.length > mNeedMatchWords.size()? mNeedMatchWords.size() : words.length;
                // 本次读对的单词个数
                int rightNum = 0;
                for (int i = 0; i < sizeCount; i++) {
                    String needMatchWord = mNeedMatchWords.get(i).replace(".", "").replace(",", "")
                            .replace(":", "").replace("?", "").toUpperCase();
                    if (StringUtils.isEquals(words[i].replace(" ", ""), needMatchWord)) {
                        mRightWords.add(mNeedMatchWords.get(i));
                        rightNum ++;
                    }else {
                        break;
                    }
                }
                for (int i = 0; i < rightNum; i++) {
                    mNeedMatchWords.remove(0);
                }
                // 拼接句子
                final SpannableStringBuilder builder = new SpannableStringBuilder();
                //对的单词
                for (int i = 0; i < mRightWords.size(); i++) {
                    if (i != 0){
                        builder.append(" ");
                    }
                    String rightWord = mRightWords.get(i);
                    builder.append(rightWord);
                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#2BBFEA")), builder.length()-rightWord.length(),
                            builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                //待匹配的单词
                for (int i = 0; i < mNeedMatchWords.size(); i++) {
                    if (mRightWords.size() > 0){
                        builder.append(" ");
                    } else{
                        if (i != 0) builder.append(" ");
                    }
                    String needWord = mNeedMatchWords.get(i);
                    builder.append(needWord);
                    builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length()-needWord.length(),
                            builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.setSentenceContent(builder);
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }



    @Override
    public void stopRecord() {
        CRLSpeechUtil.getInstance().stopRecording();
    }

    @Override
    public void removeRecordFile() {
        File file_wav = new File(AudioFileUtils.getWavFilePath());
        File file_raw = new File(AudioFileUtils.getRawFilePath());
        if (file_wav.exists()) {
            file_wav.delete();
        }
        if (file_raw.exists()) {
            file_raw.delete();
        }
    }

    @Override
    public void onActivityDestroy() {
        mPlayerUtils.stop();
        mRecordPlayer.stop();
        removeRecordFile();
        CRLSpeechUtil.getInstance().stopRecording();
    }

    @Override
    public void onVoiceImageClick() {
        if (mVoicePlaying) {
            mVoicePlaying = false;
            mPlayerUtils.pause();
            mView.setVoiceImageView(R.drawable.selector_voice_play);
        }else {
            mVoicePlaying = true;
            mPlayerUtils.play();
            mView.setVoiceImageView(R.drawable.selector_voice_pause);
        }
    }

    @Override
    public void onRecordImageClick() {
        if (mRecordPlaying) {
            mRecordPlaying = false;
            mRecordPlayer.pause();
            mView.setRecordImageView(R.drawable.selector_record_play);
        }else {
            mRecordPlaying = true;
            mRecordPlayer.play();
            mView.setRecordImageView(R.drawable.selector_record_pause);
        }
    }

    class VoicePlayerListener implements PlayerUtils.OnPlayListener {

        @Override
        public void onPlayReady() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPlayFinish() {
            if (mVoicePlaying) {
                mVoicePlaying = false;
                mView.setVoiceImageView(R.drawable.selector_voice_play);
                mView.getVoiceProgressBar().setProgress(0);
            }
        }
    }

    class RecordPlayerListener implements PlayerUtils.OnPlayListener {

        @Override
        public void onPlayReady() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPlayFinish() {
            if (mRecordPlaying) {
                mRecordPlaying = false;
                mView.setRecordImageView(R.drawable.selector_record_play);
                mView.getRecordProgressBar().setProgress(0);
            }
        }
    }
}
