package com.ibm.ischool.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.entity.WordEntity;
import com.ibm.ischool.presenter.ISpaceFragmentPresenter;
import com.ibm.ischool.presenter.impl.SpaceFragmentPresenterImpl;
import com.ibm.ischool.ui.activity.LessonDetailActivity;
import com.ibm.ischool.ui.view.RiseNumberTextView;
import com.ibm.ischool.view.ISpaceFragmentView;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

@EFragment(R.layout.fragment_detail_space)
public class SpaceFragment extends Fragment implements ISpaceFragmentView{

	LessonDetailActivity mActivity;
	
	ISpaceFragmentPresenter mPresenter;
	
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
	
	SpaceFragmentCallback mCallback;
	
	@AfterViews
	void initView(){
		mPresenter = new SpaceFragmentPresenterImpl(this);
		mPresenter.initView();
	}
	
	@Override
	public void setSentenceContent(CharSequence text) {
		mLessonSentence.setText(text);
	}

	@Override
	public void setTranslation(String translation) {
		mTranslation.setText(translation);
	}
	
	public void setFragmentCallback(SpaceFragmentCallback callback){
		mPresenter.onActivityCallRefresh(callback.setRefreshData());
	}
	
	public interface SpaceFragmentCallback {
		List<WordEntity> setRefreshData();
	}

	@Override
	public void setSentenceScore(int score) {
		mScoreTv.withNumber(score);
		mScoreTv.setDuration(5000);
		mScoreTv.start();
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
}
