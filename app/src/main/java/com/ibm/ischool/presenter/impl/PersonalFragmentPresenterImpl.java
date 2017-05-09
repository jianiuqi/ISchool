package com.ibm.ischool.presenter.impl;

import com.ibm.ischool.dao.LessonDao;
import com.ibm.ischool.presenter.IPersonalFragmentPresenter;
import com.ibm.ischool.util.ToastUtil;

import android.app.Activity;

public class PersonalFragmentPresenterImpl implements IPersonalFragmentPresenter {

	Activity mActivity;
	LessonDao mDao;
	
	public PersonalFragmentPresenterImpl(Activity activity) {
		this.mActivity = activity;
		this.mDao = new LessonDao(mActivity);
	}
	
	@Override
	public void clearData() {
		mDao.deleteLessonInfo();
		ToastUtil.showShortToast(mActivity, "成功清楚课程数据~");
	}
}
