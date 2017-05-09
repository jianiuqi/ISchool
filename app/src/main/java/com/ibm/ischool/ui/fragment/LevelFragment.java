package com.ibm.ischool.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.presenter.ILevelFragmentPresenter;
import com.ibm.ischool.presenter.impl.LevelFragmentPresenterImpl;
import com.ibm.ischool.view.ILevelFragmentView;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

@EFragment(R.layout.fragment_main_level)
public class LevelFragment extends Fragment implements ILevelFragmentView{

	@ViewById(R.id.titlebar_text)
	TextView mTitleBar;
	
	@ViewById(R.id.level_1_star_progress)
	ProgressBar mLevel1Progress;
	
	@ViewById(R.id.level_2_star_progress)
	ProgressBar mLevel2Progress;
	
	@ViewById(R.id.level_3_star_progress)
	ProgressBar mLevel3Progress;
	
	@ViewById(R.id.level_1_star_num)
	TextView mLevel1StarNum;
	
	@ViewById(R.id.level_2_star_num)
	TextView mLevel2StarNum;
	
	@ViewById(R.id.level_3_star_num)
	TextView mLevel3StarNum;
	
	ILevelFragmentPresenter mPresenter;
	
	@AfterViews
	void initView(){
		mPresenter = new LevelFragmentPresenterImpl(this, getActivity());
		mPresenter.initView();
	}
	
	@Click({ R.id.lesson_level_1_layout, R.id.lesson_level_2_layout, R.id.lesson_level_3_layout })
	void onViewClick(View view) {
		switch (view.getId()) {
		case R.id.lesson_level_1_layout:
			mPresenter.startChapter(1);
			break;
		case R.id.lesson_level_2_layout:
			mPresenter.startChapter(2);
			break;
		case R.id.lesson_level_3_layout:
			mPresenter.startChapter(3);
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
	public void setLevelProgressAndStar(int progress, int max, CharSequence starNum, int levelNum) {
		switch (levelNum) {
		case 1:
			mLevel1Progress.setMax(max);
			mLevel1Progress.setProgress(progress);
			mLevel1StarNum.setText(starNum);
			break;
		case 2:
			mLevel2Progress.setMax(max);
			mLevel2Progress.setProgress(progress);
			mLevel2StarNum.setText(starNum);
			break;
		case 3:
			mLevel3Progress.setMax(max);
			mLevel3Progress.setProgress(progress);
			mLevel3StarNum.setText(starNum);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mPresenter.refreshStar();
	}
	
	public void refresh(){
		mPresenter.refreshStar();
	}
	
}
