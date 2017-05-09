package com.ibm.ischool.presenter.impl;

import java.util.List;

import com.ibm.ischool.base.Constant;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.entity.WordEntity;
import com.ibm.ischool.presenter.IWordFragmentPresenter;
import com.ibm.ischool.util.StringUtils;
import com.ibm.ischool.view.IWordFragmentView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

public class WordFragmentPresenterImpl implements IWordFragmentPresenter {

	Fragment mFragment;
	
	IWordFragmentView mView;
	
	LessonEntity mEntity;
	
	int count = 3;
	
	public WordFragmentPresenterImpl(IWordFragmentView view){
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

	private ClickableSpan getClickableSpan() {
		return new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				TextView tv = (TextView) widget;
	            String s = tv.getText().subSequence(tv.getSelectionStart(),tv.getSelectionEnd()).toString();
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(Color.BLACK);
				ds.setUnderlineText(false);
			}
		};
	}

	@Override
	public void onActivityCallRefresh(List<WordEntity> entities) {
		try {
			String[] raw_words = mEntity.getLessonContent().split(" ");
			// 改用SpannableStringBuilder来显示
			SpannableStringBuilder builder = new SpannableStringBuilder();
			for (int i = 0; i < raw_words.length; i++) {
				WordEntity wordEntity = entities.get(i);
				ClickableSpan clickSpan = getClickableSpan();
				if (wordEntity.getScore() < Constant.STANDARD_SCORE) {
					if (StringUtils.isEquals("N", wordEntity.getSilence())) {// N红线
						builder.append("|");
						builder.setSpan(new ForegroundColorSpan(Color.parseColor("#EF2E24")), builder.length()-1, 
								builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}else if(StringUtils.isEquals("Y", wordEntity.getSilence())){ // Y绿线
						if (i != 0) {
							builder.append("|");
							builder.setSpan(new ForegroundColorSpan(Color.parseColor("#52FF7E")), builder.length()-1, 
									builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
						}
					}
					if (i != 0) {
						builder.append(" ");
					}
					builder.append(raw_words[i]);
					if (raw_words[i].contains(",") || raw_words[i].contains(":") 
							|| raw_words[i].contains(".") || raw_words[i].contains("?")) {
						builder.setSpan(clickSpan, builder.length()-raw_words[i].length(), builder.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}else {
						builder.setSpan(clickSpan, builder.length()-raw_words[i].length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					builder.setSpan(new ForegroundColorSpan(Color.parseColor("#E73133")), builder.length()-raw_words[i].length(), 
							builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}else {
					if (StringUtils.isEquals("N", wordEntity.getSilence())) {// N红线
						builder.append("|");
						builder.setSpan(new ForegroundColorSpan(Color.parseColor("#EF2E24")), builder.length()-1, 
								builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}else if(StringUtils.isEquals("Y", wordEntity.getSilence())){ // Y绿线
						if (i != 0) {
							builder.append("|");
							builder.setSpan(new ForegroundColorSpan(Color.parseColor("#52FF7E")), builder.length()-1, 
									builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
						}
					}
					if (i != 0) {
						builder.append(" ");
					}
					builder.append(raw_words[i]);
					// 必须先设置点击事件
					if (raw_words[i].contains(",") || raw_words[i].contains(":") 
							|| raw_words[i].contains(".") || raw_words[i].contains("?")) {
						builder.setSpan(clickSpan, builder.length()-raw_words[i].length(), builder.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}else {
						builder.setSpan(clickSpan, builder.length()-raw_words[i].length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					if (wordEntity.isIswarning()) {
						builder.setSpan(new ForegroundColorSpan(Color.parseColor("#F4BB2C")), builder.length()-raw_words[i].length(), 
								builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}else {
						builder.setSpan(new ForegroundColorSpan(Color.parseColor("#4582C3")), builder.length()-raw_words[i].length(), 
								builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}
				}
			}
			mView.setSentenceContent(builder);
			mView.setMovementMethod();
			if (count == 1) {
				mView.setWord1RawTextView("ever");
				mView.setWord1PronounceText("/ˈɛvɚ/");
				mView.setPronounceLayoutVisiable(View.VISIBLE);
				mView.setSentenceScore(90);
				mView.setStarNum(3);
			}else if (count == 2) {
				mView.setWord1RawTextView("ever");
				mView.setWord1PronounceText("/ˈɛvɚ/");
				mView.setPronounceLayoutVisiable(View.VISIBLE);
				mView.setWord2RawTextView("hungry");
				mView.setWord2PronounceText("/ˈhʌŋɡri/");
				mView.setWord2PronounceLayoutVisiable(View.VISIBLE);
				mView.setSentenceScore(72);
				mView.setStarNum(2);
			}else if (count > 2) {
				mView.setPronounceLayoutVisiable(View.GONE);
				int[] score_star = WordEntity.getLessonScoreAndStar(entities);
				mView.setSentenceScore(score_star[0]);
				mView.setStarNum(score_star[1]);
			}
			count ++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
