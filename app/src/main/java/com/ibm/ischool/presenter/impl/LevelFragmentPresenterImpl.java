package com.ibm.ischool.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import com.ibm.ischool.R;
import com.ibm.ischool.base.Constant;
import com.ibm.ischool.dao.LessonDao;
import com.ibm.ischool.entity.LevelEntity;
import com.ibm.ischool.presenter.ILevelFragmentPresenter;
import com.ibm.ischool.ui.activity.ChapterActivity_;
import com.ibm.ischool.util.ResourceUtils;
import com.ibm.ischool.view.ILevelFragmentView;

import android.app.Activity;
import android.content.Intent;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LevelFragmentPresenterImpl implements ILevelFragmentPresenter{

	ILevelFragmentView mView;
	Activity mActivity;
	List<LevelEntity> mEntities;
	LessonDao mDao;
	
	public LevelFragmentPresenterImpl(ILevelFragmentView view, Activity activity) {
		this.mView = view;
		this.mActivity = activity;
		this.mDao = new LessonDao(mActivity);
	}
	
	@Override
	public void initView() {
		mView.setTitleText(ResourceUtils.getStringResource(mActivity, R.string.main_lesson_titlebar_text));
	}

	@Override
	public void startChapter(final int level) {
		final Intent intent = new Intent(mActivity, ChapterActivity_.class);
		final SweetAlertDialog dialog = new SweetAlertDialog(mActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
		dialog.setTitleText("提示")
				.setCustomImage(R.drawable.icon_level_1)
                .setContentText("请选择游戏模式")
				.setConfirmText("闯关")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						switch (level) {
							case 1:
								intent.putExtra(LevelEntity.TAG, mEntities.get(0));
								break;
							case 2:
								intent.putExtra(LevelEntity.TAG, mEntities.get(1));
								break;
							case 3:
								intent.putExtra(LevelEntity.TAG, mEntities.get(2));
								break;
						}
						// 闯关
						intent.putExtra(Constant.LEARNING_MODE, Constant.PASS_MODE);
						mActivity.startActivity(intent);
						dialog.dismiss();
					}
				})
				.setCancelText("跟读")
				.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						switch (level) {
							case 1:
								intent.putExtra(LevelEntity.TAG, mEntities.get(0));
								break;
							case 2:
								intent.putExtra(LevelEntity.TAG, mEntities.get(1));
								break;
							case 3:
								intent.putExtra(LevelEntity.TAG, mEntities.get(2));
								break;
						}
						// 跟读
						intent.putExtra(Constant.LEARNING_MODE, Constant.TRACKING_MODE);
						mActivity.startActivity(intent);
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	@Override
	public void refreshStar() {
		mEntities = new ArrayList<LevelEntity>();
		int totalStar = Constant.TOTAL_CHAPTER * Constant.TOTAL_LESSON * 3;
		for (int i = 1; i < 4; i++) {
			LevelEntity entity = new LevelEntity();
			entity.setLevelId("level" + i);
			mEntities.add(entity);
			mDao.getLevelScore(entity);
			mView.setLevelProgressAndStar(entity.getLevelStar(), totalStar, entity.getLevelStar() + "/" + totalStar, i);
		}
	}

}
