package com.ibm.ischool.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import com.ibm.ischool.R;
import com.ibm.ischool.adapter.ChapterAdapter;
import com.ibm.ischool.base.Constant;
import com.ibm.ischool.dao.LessonDao;
import com.ibm.ischool.entity.ChapterEntity;
import com.ibm.ischool.entity.LevelEntity;
import com.ibm.ischool.presenter.IChapterPresenter;
import com.ibm.ischool.util.ResourceUtils;
import com.ibm.ischool.util.SizeUtils;
import com.ibm.ischool.view.IChapterView;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class ChapterPresenterImpl implements IChapterPresenter {

	IChapterView mView;
	Activity mActivity;
	ChapterAdapter mPagerAdapter;
	LevelEntity mLevelEntity;
	List<ChapterEntity> mEntities;
	int mCurrentIndex;
	LessonDao mDao;
	int mLearnMode = Constant.PASS_MODE;
	
	public ChapterPresenterImpl(IChapterView view) {
		this.mView = view;
		this.mActivity = (Activity) view;
		this.mEntities = new ArrayList<ChapterEntity>();
		this.mDao = new LessonDao(mActivity);
	}
	
	@Override
	public void initView() {
		mView.setBackBtnVisiable(View.VISIBLE);
		mView.setTitleText(ResourceUtils.getStringResource(mActivity, R.string.main_lesson_titlebar_text));
		Intent intent = mActivity.getIntent();
		mLevelEntity = intent.getParcelableExtra(LevelEntity.TAG);
		mLearnMode = intent.getIntExtra(Constant.LEARNING_MODE, Constant.PASS_MODE);
	}
	
	@Override
	public void refresh() {
		// TODO 动态获取数据
		initData();
		mPagerAdapter = new ChapterAdapter(mActivity, mEntities);
		mView.setPagerAdapter(mPagerAdapter);
		//设置OffscreenPageLimit
        mView.setOffscreenPageLimit(mEntities.size());
        addCardCircle();
	}
	
	private void initData() {
		mEntities.clear();
		for (int i = 0; i < 10; i++) {
			ChapterEntity entity = new ChapterEntity();
			entity.setLevelId(mLevelEntity.getLevelId());
			entity.setChapterId("chapter"+(i+1));
			entity.setChapterNum("" + (i + 1));
			entity.setChapterTip("Read the sentence according to the standard pronunciation and you will get a higher score!");
			entity.setLevelStar(mDao.getLevelScore(mLevelEntity).getLevelStar());
			entity.setChapterMode(mLearnMode);
			mEntities.add(mDao.getChapterScore(entity));
		}
	}
	
	private void addCardCircle(){
		mView.removeCircleViews();
		for (int i = 0; i < mEntities.size(); i++) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setBackgroundResource(R.drawable.selector_card_position_dot);
			LayoutParams layoutparams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutparams.rightMargin = SizeUtils.dpToPxInt(mActivity, 8);
			imageView.setLayoutParams(layoutparams);
			if(i == 0){
				imageView.setEnabled(true);
			}else{
				imageView.setEnabled(false);
			}
			mView.addCardCircleView(imageView);
		}
		mCurrentIndex = 0;
	}

	@Override
	public void setCurrentCardCirclePosition(int position) {
		if (position < 0 || position > mEntities.size() - 1
				|| mCurrentIndex == position) {
			return;
		}
		mView.getCardCircelChildView(position).setEnabled(true);
		mView.getCardCircelChildView(mCurrentIndex).setEnabled(false);
		mCurrentIndex = position;
	}
}
