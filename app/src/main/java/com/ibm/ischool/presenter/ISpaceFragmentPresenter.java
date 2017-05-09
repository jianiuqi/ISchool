package com.ibm.ischool.presenter;

import java.util.List;

import com.ibm.ischool.entity.WordEntity;

public interface ISpaceFragmentPresenter {

	void initView();
	
	void onActivityCallRefresh(List<WordEntity> entities);
}
