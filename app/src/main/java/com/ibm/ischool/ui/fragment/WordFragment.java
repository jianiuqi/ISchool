package com.ibm.ischool.ui.fragment;

import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.entity.WordEntity;
import com.ibm.ischool.presenter.IWordFragmentPresenter;
import com.ibm.ischool.presenter.impl.WordFragmentPresenterImpl;
import com.ibm.ischool.ui.view.RiseNumberTextView;
import com.ibm.ischool.view.IWordFragmentView;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EFragment(R.layout.fragment_detail_word)
public class WordFragment extends Fragment implements IWordFragmentView {

	IWordFragmentPresenter mPresenter;
	
	@ViewById(R.id.icon_level_iv)
	ImageView mLevelIconIV;
	
	@ViewById(R.id.lesson_sentence)
	TextView mLessonSentence;
	
	@ViewById(R.id.lesson_sentence_translation)
	TextView mTranslation;
	
	@ViewById(R.id.score_tv)
	RiseNumberTextView mScoreTv;
	
	@ViewById(R.id.score_star_1)
	ImageView mScoreStar1;
	
	@ViewById(R.id.score_star_2)
	ImageView mScoreStar2;
	
	@ViewById(R.id.score_star_3)
	ImageView mScoreStar3;
	
	@ViewById(R.id.word_pronounce_layout)
	LinearLayout mPronounceLayout;
	
	@ViewById(R.id.word1_raw_tv)
	TextView mWord1RawTv;
	
	@ViewById(R.id.word2_raw_tv)
	TextView mWord2RawTv;
	
	@ViewById(R.id.word1_pronounce_tv)
	TextView mWord1PronounceTv;
	
	@ViewById(R.id.word2_pronounce_tv)
	TextView mWord2PronounceTv;
	
	@ViewById(R.id.word2_pronounce_layout)
	LinearLayout mWord2PronounceLayout;
	
	WordFragmentCallback mCallback;
	
	SoundPool mSoundPool;
	
	HashMap<Integer, Integer> mSoundData;
	
	@AfterViews
	void initView(){
		mPresenter = new WordFragmentPresenterImpl(this);
		mPresenter.initView();
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mSoundData = new HashMap<Integer, Integer>();
		mSoundData.put(1, mSoundPool.load(getActivity(), R.raw.ever, 1));
		mSoundData.put(2, mSoundPool.load(getActivity(), R.raw.hungry, 1));
	}
	
	@Click({R.id.word1_mp3_iv, R.id.word2_mp3_iv})
	void onViewClick(View view){
		switch (view.getId()) {
		case R.id.word1_mp3_iv:
			playSound(1);
			break;
		case R.id.word2_mp3_iv:
			playSound(2);
			break;
		default:
			break;
		}
	}
	
    private void playSound(int sound) {
        AudioManager am = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = volumnCurrent / audioMaxVolumn;

        mSoundPool.play(mSoundData.get(sound),   
                volumnRatio,// 左声道音量  
                volumnRatio,// 右声道音量  
                1, // 优先级  
                1,// 循环播放次数  
                1);// 回放速度，该值在0.5-2.0之间 1为正常速度  
    }
	
	@Override
	public void setSentenceContent(CharSequence text) {
		mLessonSentence.setText(text);//点击每个单词响应
	}

	@Override
	public void setTranslation(String translation) {
		mTranslation.setText(translation);
	}

	public void setFragmentCallback(WordFragmentCallback callback){
		mPresenter.onActivityCallRefresh(callback.setRefreshData());
	}
	
	public interface WordFragmentCallback {
		List<WordEntity> setRefreshData();
	}

	@Override
	public void setSentenceScore(int score) {
		mScoreTv.withNumber(score);
//		mScoreTv.setDuration(3600);
		mScoreTv.start();
	}

	@Override
	public FragmentActivity getLessonDetailActivity() {
		return getActivity();
	}

	@Override
	public void getStar1Location(int[] location) {
		mScoreStar1.getLocationInWindow(location);
	}

	@Override
	public int getStar1Width() {
		return mScoreStar1.getWidth();
	}

	@Override
	public int getStar1Height() {
		return mScoreStar1.getHeight();
	}

	@Override
	public void setStarNum(int starNum) {
		if (starNum == 0) {
			mScoreStar1.setImageResource(R.drawable.icon_star_grey);
			mScoreStar2.setImageResource(R.drawable.icon_star_grey);
			mScoreStar3.setImageResource(R.drawable.icon_star_grey);
		}else if (starNum == 1) {
			mScoreStar1.setImageResource(R.drawable.icon_star_get);
			mScoreStar2.setImageResource(R.drawable.icon_star_grey);
			mScoreStar3.setImageResource(R.drawable.icon_star_grey);
		}else if (starNum == 2) {
			mScoreStar1.setImageResource(R.drawable.icon_star_get);
			mScoreStar2.setImageResource(R.drawable.icon_star_get);
			mScoreStar3.setImageResource(R.drawable.icon_star_grey);
		}else if (starNum >= 3) {
			mScoreStar1.setImageResource(R.drawable.icon_star_get);
			mScoreStar2.setImageResource(R.drawable.icon_star_get);
			mScoreStar3.setImageResource(R.drawable.icon_star_get);
		}
	}

	@Override
	public void setPronounceLayoutVisiable(int visiablity) {
		mPronounceLayout.setVisibility(visiablity);
	}

	@Override
	public void setWord1RawTextView(CharSequence text) {
		mWord1RawTv.setText(text);
	}
	
	@Override
	public void setWord2RawTextView(CharSequence text) {
		mWord2RawTv.setText(text);
	}

	@Override
	public void setWord1PronounceText(CharSequence text) {
		Typeface mFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/phonetic.ttf");
		mWord1PronounceTv.setTypeface(mFace);
		mWord1PronounceTv.setText(text);
	}
	
	@Override
	public void setWord2PronounceText(CharSequence text) {
		Typeface mFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/phonetic.ttf");
		mWord2PronounceTv.setTypeface(mFace);
		mWord2PronounceTv.setText(text);
	}

	@Override
	public void setWord2PronounceLayoutVisiable(int visiablity) {
		mWord2PronounceLayout.setVisibility(visiablity);
	}

	@Override
	public void setMovementMethod() {
		mLessonSentence.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public CharSequence getSetenceText() {
		return mLessonSentence.getText();
	}
	
}
