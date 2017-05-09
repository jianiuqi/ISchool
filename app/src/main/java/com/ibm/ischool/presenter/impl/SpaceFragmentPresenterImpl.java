package com.ibm.ischool.presenter.impl;

import java.util.List;

import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.entity.WordEntity;
import com.ibm.ischool.presenter.ISpaceFragmentPresenter;
import com.ibm.ischool.util.StringUtils;
import com.ibm.ischool.view.ISpaceFragmentView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;

public class SpaceFragmentPresenterImpl implements ISpaceFragmentPresenter{

	Fragment mFragment;
	
	ISpaceFragmentView mView;
	
	LessonEntity mEntity;
	
	public SpaceFragmentPresenterImpl(ISpaceFragmentView view){
		this.mFragment = (Fragment) view;
		this.mView = view;
	}
	
	@Override
	public void initView() {
		Bundle bundle = mFragment.getArguments();//从activity传过来的Bundle
		mEntity = bundle.getParcelable(LessonEntity.TAG);
		mView.setSentenceContent(mEntity.getLessonContent());
		mView.setTranslation(mEntity.getTranslation());
	}

	@Override
	public void onActivityCallRefresh(List<WordEntity> entities) {
		try {
			String[] raw_words = mEntity.getLessonContent().split(" ");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < raw_words.length; i++) {
				WordEntity wordEntity = entities.get(i);
				if (StringUtils.isEquals("N", wordEntity.getSilence())) {// N红线
					sb.append("<font color='#EF2E24'>|</font>");
				}else if(StringUtils.isEquals("Y", wordEntity.getSilence())){ // Y绿线
					if (i != 0) {
						sb.append("<font color='#52FF7E'>|</font>");
					}
				}else { // R不操作
					
				}
				sb.append(" <font color='#070809'>" + raw_words[i] + "</font>");
			}
			sb.replace(0, 1, "");
			mView.setSentenceContent(Html.fromHtml(sb.toString()));
			int[] score_star = WordEntity.getLessonScoreAndStar(entities);
			mView.setSentenceScore(score_star[0]);
			mView.setStarNum(score_star[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
