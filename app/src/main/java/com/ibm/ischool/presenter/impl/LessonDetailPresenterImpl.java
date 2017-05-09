package com.ibm.ischool.presenter.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ibm.ischool.R;
import com.ibm.ischool.adapter.MyFragmentPagerAdapter;
import com.ibm.ischool.api.ILessonApi;
import com.ibm.ischool.api.IResponseApi;
import com.ibm.ischool.api.impl.LessonApiImpl;
import com.ibm.ischool.base.Constant;
import com.ibm.ischool.dao.LessonDao;
import com.ibm.ischool.entity.LessonEntity;
import com.ibm.ischool.entity.WordEntity;
import com.ibm.ischool.presenter.ILessonDetailPresenter;
import com.ibm.ischool.ui.fragment.SpaceFragment_;
import com.ibm.ischool.ui.fragment.WordFragment.WordFragmentCallback;
import com.ibm.ischool.ui.fragment.WordFragment_;
import com.ibm.ischool.util.AudioFileUtils;
import com.ibm.ischool.util.PlayerUtils;
import com.ibm.ischool.util.PlayerUtils.OnPlayListener;
import com.ibm.ischool.util.RecordUtil;
import com.ibm.ischool.util.RecordUtil.OnResultListener;
import com.ibm.ischool.util.SizeUtils;
import com.ibm.ischool.util.ToastUtil;
import com.ibm.ischool.view.ILessonDetailView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class LessonDetailPresenterImpl implements ILessonDetailPresenter {

	ILessonDetailView mView;
	FragmentActivity mActivity;
	ILessonApi mApi;
	/**
	 * 小圆点索引
	 */
	int mCurrentIndex;
	MyFragmentPagerAdapter myFragmentPagerAdapter;
	/**
	 * 存放Fragment
	 */
	List<Fragment> mFragments = new ArrayList<Fragment>();
	/**
	 * 标准音播放器
	 */
	PlayerUtils mPlayerUtils; 
	/**
	 * 录音播放器
	 */
	PlayerUtils mRecordPlayer;
	/**
	 * 标准录音是否正在播放
	 */
	boolean mVoicePlaying = false;
	/**
	 * 录音是否正在播放
	 */
	boolean mRecordPlaying = false;
	
	SpaceFragment_ mSpaceFragment;
	
	WordFragment_ mWordFragment;
	
	/**
	 * 获取的课程内容
	 */
	LessonEntity mEntity;
	
	LessonDao mDao;
	
	public LessonDetailPresenterImpl(ILessonDetailView view) {
		this.mView = view;
		this.mActivity = (FragmentActivity) view;
		this.mApi = new LessonApiImpl(mActivity);
		this.mDao = new LessonDao(mActivity);
	}
	
	@Override
	public void initView() {
		Intent intent = mActivity.getIntent();
		mEntity = intent.getParcelableExtra(LessonEntity.TAG);
		mView.setBackBtnVisiable(View.VISIBLE);
		mView.setTitleText("Lesson " + mEntity.getLessonNum());
		// 原始句子
		mWordFragment = new WordFragment_();
		Bundle bundle = new Bundle();
		bundle.putParcelable(LessonEntity.TAG, mEntity);
		mWordFragment.setArguments(bundle);
		mFragments.add(mWordFragment);
		// 分隔错误Fragment
		mSpaceFragment = new SpaceFragment_();
		mSpaceFragment.setArguments(bundle);
		// 设置Fragment数据源
		myFragmentPagerAdapter = new MyFragmentPagerAdapter(mActivity.getSupportFragmentManager(), mFragments);
        mView.setViewPagerAdapter(myFragmentPagerAdapter);
        // 初始化播放器
        mPlayerUtils = new PlayerUtils(mView.getVoiceProgressBar());
        mPlayerUtils.setUrlPrepare(Constant.BASE_SERVER_URL + "/" + mEntity.getUrl());
		mPlayerUtils.setOnPlayListener(new VoicePlayerListener());
		mRecordPlayer = new PlayerUtils(mView.getRecordProgressBar());
		mRecordPlayer.setOnPlayListener(new RecordPlayerListener());
	}
	
	@SuppressWarnings("unused")
	private void uploadRecordFile(){
		String filePath = AudioFileUtils.getWavFilePath();
		// 此处设置录音播放声音源
		mRecordPlayer.setUrlPrepare(filePath);
		mView.setDialogShow();
		mApi.uploadRecordFile(mEntity.getLessonId(), filePath, new IResponseApi<List<WordEntity>>() {
			
			@Override
			public void onSuccess(final List<WordEntity> entities) {
				mView.setDialogDismiss();
				// 显示播放本地录音布局
				mWordFragment.setFragmentCallback(new WordFragmentCallback() {
					
					@Override
					public List<WordEntity> setRefreshData() {
						return entities;
					}
				});
				/* 161122将发音和断句放到一个Fragment中
				if (mFragments.size() < 2 && entities.size() > 0) {
					mFragments.add(mSpaceFragment);
					myFragmentPagerAdapter.notifyDataSetChanged();
					addCardCircle();
				}
				mSpaceFragment.setFragmentCallback(new SpaceFragmentCallback() {
					
					@Override
					public List<WordEntity> setRefreshData() {
						return entities;
					}
				});*/
				mView.setRecordLayoutVisiable(View.VISIBLE);
				int[] score_star = WordEntity.getLessonScoreAndStar(entities);
				if (score_star[0] > mEntity.getLessonScore()) {
					ToastUtil.showShortToast(mActivity, "获得新纪录"+ score_star[0] + "分");
					mEntity.setLessonScore(score_star[0]);
					mEntity.setLessonStar(score_star[1]);
					mDao.addLesson(mEntity);
				}else {
					ToastUtil.showShortToast(mActivity, "没有最高纪录"+ mEntity.getLessonScore() + "分高哦~");
				}
			}
			
			@Override
			public void onFailure(String msg) {
				mView.setDialogDismiss();
				ToastUtil.showShortToast(mActivity, "重新读一遍吧~");
			}
		});
	}

	@Override
	public void startRecord() {
//		AudioRecordUtils utils = AudioRecordUtils.getInstance();
//		utils.startRecordAndFile();
		RecordUtil util = RecordUtil.getInstance();
		// 需先设置例句
		util.setExampleText(mEntity.getLessonContent().replace("'", "")
				.replace(",", "").replace(".", "").replace("?", "").replace(":", ""));
		util.startRecording();
		util.setListener(new OnResultListener() {
			
			@Override
			public void onSuccess(final String content) {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mView.setDialogDismiss();
						String filePath = AudioFileUtils.getWavFilePath();
						// 此处设置录音播放声音源
						mRecordPlayer.setUrlPrepare(filePath);
						final List<WordEntity> entities = WordEntity.getWordEntities(content);
						mWordFragment.setFragmentCallback(new WordFragmentCallback() {
							
							@Override
							public List<WordEntity> setRefreshData() {
								return entities;
							}
						});
						mView.setRecordLayoutVisiable(View.VISIBLE);
						int[] score_star = WordEntity.getLessonScoreAndStar(entities);
						if (score_star[0] > mEntity.getLessonScore()) {
							ToastUtil.showShortToast(mActivity, "获得新纪录"+ score_star[0] + "分");
							mEntity.setLessonScore(score_star[0]);
							mEntity.setLessonStar(score_star[1]);
							mDao.addLesson(mEntity);
						}else {
							ToastUtil.showShortToast(mActivity, "没有最高纪录"+ mEntity.getLessonScore() + "分高哦~");
						}
					}
				});
			}
			
			@Override
			public void onFailure() {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mView.setDialogDismiss();
						String filePath = AudioFileUtils.getWavFilePath();
						// 此处设置录音播放声音源
						mRecordPlayer.setUrlPrepare(filePath);
						final List<WordEntity> entities = new ArrayList<WordEntity>();
						String[] words = mEntity.getLessonContent().split(" ");
						for (int i = 0; i < words.length; i++) {
							entities.add(new WordEntity());
						}
						mWordFragment.setFragmentCallback(new WordFragmentCallback() {
							
							@Override
							public List<WordEntity> setRefreshData() {
								return entities;
							}
						});
						mView.setRecordLayoutVisiable(View.VISIBLE);
						int[] score_star = WordEntity.getLessonScoreAndStar(entities);
						if (score_star[0] > mEntity.getLessonScore()) {
							ToastUtil.showShortToast(mActivity, "获得新纪录"+ score_star[0] + "分");
							mEntity.setLessonScore(score_star[0]);
							mEntity.setLessonStar(score_star[1]);
							mDao.addLesson(mEntity);
						}else {
							ToastUtil.showShortToast(mActivity, "没有最高纪录"+ mEntity.getLessonScore() + "分高哦~");
						}
					}
				});
			}
		});
	}

	@Override
	public void stopRecord(boolean upload) {
//		AudioRecordUtils utils = AudioRecordUtils.getInstance();
//		utils.stopRecordAndFile();
//		if (upload) {
//			uploadRecordFile();
//		}
		RecordUtil util = RecordUtil.getInstance();
		util.stopRecording();
		mView.setDialogShow();
	}

	@Override
	public void removeRecordFile() {
		File file_wav = new File(AudioFileUtils.getWavFilePath());
		File file_raw = new File(AudioFileUtils.getRawFilePath());
		if (file_wav.exists()) {
			file_wav.delete();
		}
		if (file_raw.exists()) {
			file_raw.delete();
		}
	}

	/**
	 * 动态添加小圆点个数
	 */
	@SuppressWarnings("unused")
	private void addCardCircle(){
		for (int i = 0; i < mFragments.size(); i++) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setBackgroundResource(R.drawable.selector_lesson_detail_position_dot);
			LayoutParams layoutparams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutparams.rightMargin = SizeUtils.dpToPxInt(mActivity, 8);
			imageView.setLayoutParams(layoutparams);
			if(i == 0){
				imageView.setEnabled(true);
			}else{
				imageView.setEnabled(false);
			}
			mView.addCircleView(imageView);
		}
		mCurrentIndex = 0;
	}
	
	@Override
	public void setCurrentCardCirclePosition(int position) {
		if (position < 0 || position > mFragments.size() - 1
				|| mCurrentIndex == position) {
			return;
		}
		mView.getCircelChildView(position).setEnabled(true);
		mView.getCircelChildView(mCurrentIndex).setEnabled(false);
		mCurrentIndex = position;
	}
	
	@Override
	public void onActivityDestroy() {
		mPlayerUtils.stop();
		mRecordPlayer.stop();
		removeRecordFile();
	}

	@Override
	public void onVoiceImageClick() {
		if (mVoicePlaying) {
			mVoicePlaying = false;
			mPlayerUtils.pause();
			mView.setVoiceImageView(R.drawable.selector_voice_play);
		}else {
			mVoicePlaying = true;
			mPlayerUtils.play();
			mView.setVoiceImageView(R.drawable.selector_voice_pause);
		}
	}

	@Override
	public void onRecordImageClick() {
		if (mRecordPlaying) {
			mRecordPlaying = false;
			mRecordPlayer.pause();
			mView.setRecordImageView(R.drawable.selector_record_play);
		}else {
			mRecordPlaying = true;
			mRecordPlayer.play();
			mView.setRecordImageView(R.drawable.selector_record_pause);
		}
	}
	
	class VoicePlayerListener implements OnPlayListener{

		@Override
		public void onPlayReady() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPlayFinish() {
			if (mVoicePlaying) {
				mVoicePlaying = false;
				mView.setVoiceImageView(R.drawable.selector_voice_play);
				mView.getVoiceProgressBar().setProgress(0);
			}
		}
	}
	
	class RecordPlayerListener implements OnPlayListener{

		@Override
		public void onPlayReady() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPlayFinish() {
			if (mRecordPlaying) {
				mRecordPlaying = false;
				mView.setRecordImageView(R.drawable.selector_record_play);
				mView.getRecordProgressBar().setProgress(0);
			}
		}
	}
	
}
