package com.ibm.ischool.ui.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibm.ischool.R;
import com.ibm.ischool.base.BaseActivity;
import com.ibm.ischool.presenter.ITrackingPresenter;
import com.ibm.ischool.presenter.impl.TrackingPresenterImpl;
import com.ibm.ischool.ui.view.HoloCircularProgressBar;
import com.ibm.ischool.ui.view.RippleLayout;
import com.ibm.ischool.view.ITrackingView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by jnq on 2017/5/9.
 */
@EActivity(R.layout.activity_tracking)
public class TrackingActivity extends BaseActivity implements ITrackingView{

    @ViewById(R.id.back_btn)
    ImageButton mBackBtn;

    @ViewById(R.id.titlebar_text)
    TextView mTitleBar;

    @ViewById(R.id.lesson_sentence)
    TextView mLessonSentence;

    @ViewById(R.id.lesson_sentence_translation)
    TextView mTranslation;

    @ViewById(R.id.mic_imgview)
    ImageView mMicImageview;

    @ViewById(R.id.ripple_layout)
    RippleLayout mRippleLayout;

    @ViewById(R.id.server_voice_progress)
    HoloCircularProgressBar mServerPlayBar;

    @ViewById(R.id.record_play_progress)
    HoloCircularProgressBar mRecordPlayBar;

    @ViewById(R.id.server_voice_play_iv)
    ImageView mServerPlayIV;

    @ViewById(R.id.local_record_play_iv)
    ImageView mRecordPlayIV;

    @ViewById(R.id.record_play_layout)
    RelativeLayout mRecordPlayLayout;

    @ViewById(R.id.server_voice_layout)
    RelativeLayout mServerVoiceLayout;

    ITrackingPresenter mPresenter;

    boolean mIsRecording = false;

    @AfterViews
    void initView(){
        mPresenter = new TrackingPresenterImpl(this);
        mPresenter.initView();
    }

    @Click({R.id.back_btn, R.id.server_voice_play_iv, R.id.local_record_play_iv, R.id.mic_imgview})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.server_voice_play_iv:
                mPresenter.onVoiceImageClick();
                break;
            case R.id.local_record_play_iv:
                mPresenter.onRecordImageClick();
                break;
            case R.id.mic_imgview:
                if (mIsRecording) {
                    mRippleLayout.stopRippleAnimation();
                    mPresenter.stopRecord();
                    mIsRecording = false;
                }else {
                    mRippleLayout.startRippleAnimation();
                    mPresenter.startRecord();
                    mIsRecording = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setTitleText(String title) {
        mTitleBar.setText(title);
    }

    @Override
    public void setBackBtnVisiable(int visibility) {
        mBackBtn.setVisibility(visibility);
    }

    @Override
    public void setSentenceContent(CharSequence text) {
        mLessonSentence.setText(text);//点击每个单词响应
    }

    @Override
    public void setTranslation(String translation) {
        mTranslation.setText(translation);
    }

    @Override
    public HoloCircularProgressBar getVoiceProgressBar() {
        return mServerPlayBar;
    }

    @Override
    public HoloCircularProgressBar getRecordProgressBar() {
        return mRecordPlayBar;
    }

    @Override
    public void setVoiceLayoutVisible(int visibility) {
        mServerVoiceLayout.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onActivityDestroy();
    }

    @Override
    public void setVoiceImageView(int resourceId) {
        mServerPlayIV.setImageResource(resourceId);
    }

    @Override
    public void setRecordImageView(int resourceId) {
        mRecordPlayIV.setImageResource(resourceId);
    }

    @Override
    public void setRecordLayoutVisible(int visibility) {
        mRecordPlayLayout.setVisibility(visibility);
    }
}
