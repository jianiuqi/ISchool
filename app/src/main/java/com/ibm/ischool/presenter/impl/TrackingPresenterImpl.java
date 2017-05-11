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

import cn.pedant.SweetAlert.SweetAlertDialog;

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

    /**
     * 待匹配的停顿
     */
    private List<String> mNeedPauses = new ArrayList<>();

    boolean mIsRecording = false;

    int mCursorPosition = 0;

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
        mView.setSentenceContent(mEntity.getLessonContent().replace("|", ""));
        mView.setTranslation(mEntity.getTranslation());
        // 初始化播放器
        if (!StringUtils.isEmpty(mEntity.getUrl())){
            mPlayerUtils = new PlayerUtils(mView.getVoiceProgressBar());
            mPlayerUtils.setUrlPrepare(Constant.BASE_SERVER_URL + "/" + mEntity.getUrl());
            mPlayerUtils.setOnPlayListener(new TrackingPresenterImpl.VoicePlayerListener());
            mRecordPlayer = new PlayerUtils(mView.getRecordProgressBar());
            mRecordPlayer.setOnPlayListener(new TrackingPresenterImpl.RecordPlayerListener());
            mView.setVoiceLayoutVisible(View.VISIBLE);
        }else {
            mView.setVoiceLayoutVisible(View.GONE);
        }
        //初始化待匹配的单词
        String[] sentenceWords = mEntity.getLessonContent().split(" ");
        for (String word : sentenceWords) {
            mNeedMatchWords.add(word);
        }
        //初始化待匹配的停顿
        String[] pauseSentences = mEntity.getLessonContent().split("\\|");
        for (String pauseSentence: pauseSentences) {
            mNeedPauses.add(pauseSentence.trim());
        }
    }

    @Override
    public void onRecordClick() {
        if(mIsRecording) {
            stopRecord();
            mIsRecording = false;
            mView.stopRippleAnim();
        }else {
            startRecord();
            mIsRecording = true;
            mView.startRippleAnim();
        }
    }

    private void startRecord() {
        final SweetAlertDialog dialog = new SweetAlertDialog(mActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        CRLSpeechUtil.getInstance().startRecog();
        CRLSpeechUtil.getInstance().setListener(new CRLSpeechUtil.OnResultListener() {
            @Override
            public void onSuccess(final String content) {
                System.out.println("content:" + content);
                if (mNeedMatchWords.size() > 0) {
                    if (StringUtils.isEquals("0", content.substring(0,1))) { // 临时识别结果
                        if (content.length() > 3){ //识别有内容时再返回
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.setSentenceContent(containsTempMatch(content.substring(4)));
                                    mView.setCursorPosition(mCursorPosition);
                                }
                            });
                        }
                    }else {
                        if (content.length() > 3){ //识别有内容时再返回
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.setSentenceContent(pauseMatchRule(content.substring(4)));
                                    mView.setCursorPosition(mCursorPosition);
                                }
                            });
                        }
                    }
                }else {
                    // 读完之后停止录音并结束动画
                    CRLSpeechUtil.getInstance().stopRecording();
                    mIsRecording = false;
                    mCursorPosition = 0;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!dialog.isShowing()) {
                                mView.stopRippleAnim();
                                dialog.setTitleText("太棒了")
                                        .setCustomImage(R.drawable.icon_level_1)
                                        .setConfirmText("重读")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                mRightWords.clear();
                                                mNeedMatchWords.clear();
                                                mNeedPauses.clear();
                                                mView.setSentenceContent(mEntity.getLessonContent().replace("|", ""));
                                                //初始化待匹配的单词
                                                String[] sentenceWords = mEntity.getLessonContent().split(" ");
                                                for (String word : sentenceWords) {
                                                    mNeedMatchWords.add(word);
                                                }
                                                //初始化待匹配的停顿
                                                String[] pauseSentences = mEntity.getLessonContent().split("\\|");
                                                for (String pauseSentence: pauseSentences) {
                                                    mNeedPauses.add(pauseSentence.trim());
                                                }
                                                mView.setCursorPosition(mCursorPosition);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setCancelText("关闭")
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                dialog.dismiss();
                                            }
                                        });
                                dialog.show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 停顿匹配规则
     * @param content
     * @return
     */
    private SpannableStringBuilder pauseMatchRule(String content) {
        String[] words = content.trim().split(" ");
        String firstNeedMatchWord = mNeedMatchWords.get(0).replace(".", "").replace(",", "")
                .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
        //1.先获取待匹配的第一个单词在识别结果中的所有的index
        List<Integer> firstMatchIndexes = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (StringUtils.isEquals(words[i].replace(" ", ""), firstNeedMatchWord)) {
                firstMatchIndexes.add(i);
            }
        }
        //2.查看每个index后有多少匹配的单词
        int maxMatchSize = 0;
        for (int i = 0; i < firstMatchIndexes.size(); i++) {
            int index = firstMatchIndexes.get(i);
            int maxMatchSizeTemp = 0;
            int sizeCount = (words.length - index) > mNeedMatchWords.size()? mNeedMatchWords.size() : (words.length - index);
            for (int j = 0; j < sizeCount; j++) {
                String needWord = mNeedMatchWords.get(j).replace(".", "").replace(",", "")
                        .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
                String recogWord = words[j + index].replace(" ", "");
                if (StringUtils.isEquals(recogWord, needWord)){
                    maxMatchSizeTemp ++;
                }
            }
            if (maxMatchSizeTemp > maxMatchSize) {
                maxMatchSize = maxMatchSizeTemp;
            }
        }
        //3.判断正确的单词个数和停顿长度匹配度
        int needPauseWordLength = 0;
        int maxPauseMatchSize = 0;
        int maxMatchSizeCopy = 0;
        for (int i = 0; i < mNeedPauses.size(); i++) {
            String[] pauseWords = mNeedPauses.get(i).split(" ");
            needPauseWordLength = needPauseWordLength + pauseWords.length;
            //待匹配的单词长度大于最大匹配个数
            if (needPauseWordLength > maxMatchSize){
                break;
            }
            maxPauseMatchSize ++;
            maxMatchSizeCopy = needPauseWordLength;
        }
        maxMatchSize = maxMatchSizeCopy;
        //4.移除待匹配的断句
        for (int i = 0; i < maxPauseMatchSize; i++) {
            mNeedPauses.remove(0);
        }
        //5.操作正确的List和待匹配的List
        for (int i = 0; i < maxMatchSize; i++) {
            mRightWords.add(mNeedMatchWords.get(i));
        }
        for (int i = 0; i < maxMatchSize; i++) {
            mNeedMatchWords.remove(0);
        }
        // 拼接句子
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        //对的单词
        for (int i = 0; i < mRightWords.size(); i++) {
            if (i != 0){
                builder.append(" ");
            }
            String rightWord = mRightWords.get(i).replace("|", "");
            builder.append(rightWord);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#2BBFEA")), builder.length()-rightWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        mCursorPosition = builder.length();
        //待匹配的单词
        for (int i = 0; i < mNeedMatchWords.size(); i++) {
            if (mRightWords.size() > 0){
                builder.append(" ");
            } else{
                if (i != 0) builder.append(" ");
            }
            String needWord = mNeedMatchWords.get(i).replace("|", "");
            builder.append(needWord);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length()-needWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

    /**
     * 最大长度匹配规则
     */
    private SpannableStringBuilder containsMatch(String content){
        String[] words = content.trim().split(" ");
        String firstNeedMatchWord = mNeedMatchWords.get(0).replace(".", "").replace(",", "")
                .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
        //1.先获取待匹配的第一个单词在识别结果中的所有的index
        List<Integer> firstMatchIndexes = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (StringUtils.isEquals(words[i].replace(" ", ""), firstNeedMatchWord)) {
                firstMatchIndexes.add(i);
            }
        }
        //2.查看每个index后有多少匹配的单词
        int maxMatchSize = 0;
        for (int i = 0; i < firstMatchIndexes.size(); i++) {
            int index = firstMatchIndexes.get(i);
            int maxMatchSizeTemp = 0;
            int sizeCount = (words.length - index) > mNeedMatchWords.size()? mNeedMatchWords.size() : (words.length - index);
            for (int j = 0; j < sizeCount; j++) {
                String needWord = mNeedMatchWords.get(j).replace(".", "").replace(",", "")
                        .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
                String recogWord = words[j + index].replace(" ", "");
                if (StringUtils.isEquals(recogWord, needWord)){
                    maxMatchSizeTemp ++;
                }
            }
            if (maxMatchSizeTemp > maxMatchSize) {
                maxMatchSize = maxMatchSizeTemp;
            }
        }
        //3.操作正确的List和待匹配的List
        for (int i = 0; i < maxMatchSize; i++) {
            mRightWords.add(mNeedMatchWords.get(i));
        }
        for (int i = 0; i < maxMatchSize; i++) {
            mNeedMatchWords.remove(0);
        }
        // 拼接句子
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        //对的单词
        for (int i = 0; i < mRightWords.size(); i++) {
            if (i != 0){
                builder.append(" ");
            }
            String rightWord = mRightWords.get(i).replace("|", "");
            builder.append(rightWord);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#2BBFEA")), builder.length()-rightWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        mCursorPosition = builder.length();
        //待匹配的单词
        for (int i = 0; i < mNeedMatchWords.size(); i++) {
            if (mRightWords.size() > 0){
                builder.append(" ");
            } else{
                if (i != 0) builder.append(" ");
            }
            String needWord = mNeedMatchWords.get(i).replace("|", "");
            builder.append(needWord);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length()-needWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

    /**
     * 临时结果匹配
     * @param content
     * @return
     */
    private SpannableStringBuilder containsTempMatch(String content) {
        List<String> tempRightWords = new ArrayList<>();
        String[] tempWords = content.trim().split(" ");
        String firstNeedMatchWord = mNeedMatchWords.get(0).replace(".", "").replace(",", "")
                .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
        //1.先获取待匹配的第一个单词在识别结果中的所有的index
        List<Integer> firstMatchIndexes = new ArrayList<>();
        for (int i = 0; i < tempWords.length; i++) {
            if (StringUtils.isEquals(tempWords[i].replace(" ", ""), firstNeedMatchWord)) {
                firstMatchIndexes.add(i);
            }
        }
        //2.查看每个index后有多少匹配的单词
        int maxMatchSize = 0;
        for (int i = 0; i < firstMatchIndexes.size(); i++) {
            int index = firstMatchIndexes.get(i);
            int maxMatchSizeTemp = 0;
            int sizeCount = (tempWords.length - index) > mNeedMatchWords.size()? mNeedMatchWords.size() : (tempWords.length - index);
            for (int j = 0; j < sizeCount; j++) {
                String needWord = mNeedMatchWords.get(j).replace(".", "").replace(",", "")
                        .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
                String recogWord = tempWords[j + index].replace(" ", "");
                if (StringUtils.isEquals(recogWord, needWord)){
                    maxMatchSizeTemp ++;
                }
            }
            if (maxMatchSizeTemp > maxMatchSize) {
                maxMatchSize = maxMatchSizeTemp;
            }
        }
        //3.操作正确的List和待匹配的List
        for (int i = 0; i < maxMatchSize; i++) {
            tempRightWords.add(mNeedMatchWords.get(i));
        }
        // 拼接句子
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        //对的单词
        for (int i = 0; i < mRightWords.size(); i++) {
            if (i != 0){
                builder.append(" ");
            }
            String rightWord = mRightWords.get(i).replace("|", "");
            builder.append(rightWord);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#2BBFEA")), builder.length()-rightWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        mCursorPosition = builder.length();
        //临时识别对的单词 用黄色标记#F4BB2C
        for (int i = 0; i < tempRightWords.size(); i++) {
            if (mRightWords.size() > 0){
                builder.append(" ");
            } else{
                if (i != 0) builder.append(" ");
            }
            String tempRightWord = tempRightWords.get(i).replace("|", "");
            builder.append(tempRightWord);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#F4BB2C")), builder.length()-tempRightWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        //待匹配的单词
        for (int i = tempRightWords.size(); i < mNeedMatchWords.size(); i++) {
            if (mRightWords.size() > 0){
                builder.append(" ");
            } else{
                if (i != 0) builder.append(" ");
            }
            String needWord = mNeedMatchWords.get(i).replace("|", "");
            builder.append(needWord);
            builder.setSpan(new ForegroundColorSpan(Color.BLACK), builder.length()-needWord.length(),
                    builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

    /**
     * 开头匹配规则
     * @return
     */
    @SuppressWarnings("unused")
    private SpannableStringBuilder matchStart(String content){
        String[] words = content.trim().split(" ");
        int sizeCount = words.length > mNeedMatchWords.size()? mNeedMatchWords.size() : words.length;
        // 本次读对的单词个数
        int rightNum = 0;
        for (int i = 0; i < sizeCount; i++) {
            String needMatchWord = mNeedMatchWords.get(i).replace(".", "").replace(",", "")
                    .replace(":", "").replace("?", "").replace("|", "").toUpperCase();
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
        return builder;
    }

    private void stopRecord() {
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
        if (mPlayerUtils != null)
            mPlayerUtils.stop();
        if (mRecordPlayer != null)
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
