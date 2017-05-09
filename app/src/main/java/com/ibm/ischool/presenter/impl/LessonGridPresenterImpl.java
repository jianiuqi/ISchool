package com.ibm.ischool.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import com.ibm.ischool.adapter.LessonGridAdapter;
import com.ibm.ischool.api.ILessonApi;
import com.ibm.ischool.api.IResponseApi;
import com.ibm.ischool.api.impl.LessonApiImpl;
import com.ibm.ischool.entity.ChapterEntity;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.presenter.ILessonGridPresenter;
import com.ibm.ischool.view.ILessonGridView;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class LessonGridPresenterImpl implements ILessonGridPresenter {

	ILessonGridView mView;
	Activity mActivity;
	List<LessonEntity> mEntities;
	ChapterEntity mChapterEntity;
	int mCurrentIndex;
	LessonGridAdapter mAdapter;
	ILessonApi mApi;
	
	public LessonGridPresenterImpl(ILessonGridView view) {
		this.mView = view;
		this.mActivity = (Activity) view;
		this.mEntities = new ArrayList<LessonEntity>();
		this.mApi = new LessonApiImpl(mActivity);
	}
	
	@Override
	public void initView() {
		Intent intent = mActivity.getIntent();
		mChapterEntity = intent.getParcelableExtra(ChapterEntity.TAG);
		mView.setBackBtnVisiable(View.VISIBLE);
		mView.setTitleText("Lesson " + mChapterEntity.getChapterNum());
		initData();
	}
	
	void initData(){
		mView.setDialogShow();
		mApi.getLessonsChapterId(mChapterEntity, new IResponseApi<List<LessonEntity>>() {
			
			@Override
			public void onSuccess(List<LessonEntity> entities) {
				mEntities = entities;
				mAdapter = new LessonGridAdapter(mActivity, mEntities);
				mView.setGridAdapter(mAdapter);
				mView.setDialogDismiss();
			}
			
			@Override
			public void onFailure(String msg) {
				mView.setDialogDismiss();
			}
		});
	}
}
