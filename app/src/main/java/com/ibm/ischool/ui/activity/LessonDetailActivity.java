package com.ibm.ischool.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.base.BaseActivity;
import com.ibm.ischool.presenter.ILessonDetailPresenter;
import com.ibm.ischool.presenter.impl.LessonDetailPresenterImpl;
import com.ibm.ischool.ui.view.HoloCircularProgressBar;
import com.ibm.ischool.ui.view.RippleLayout;
import com.ibm.ischool.view.ILessonDetailView;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EActivity(R.layout.activity_lesson_detail)
public class LessonDetailActivity extends BaseActivity implements ILessonDetailView, OnPageChangeListener {

	@ViewById(R.id.back_btn)
	ImageButton mBackBtn;
	
	@ViewById(R.id.titlebar_text)
	TextView mTitleBar;
	
	@ViewById(R.id.mic_imgview)
	ImageView mMicImageview;
	
	@ViewById(R.id.ripple_layout)
    RippleLayout mRippleLayout;
	
	ILessonDetailPresenter mPresenter;
	
	@ViewById(R.id.viewPager)
	ViewPager mViewPager;
	
	@ViewById(R.id.dots_container)
	LinearLayout mDotsContainer;
	
	@ViewById(R.id.server_voice_progress)
	HoloCircularProgressBar mServerPlayBar;
	
	@ViewById(R.id.record_play_progress)
	HoloCircularProgressBar mRecordPlayBar;
	
	@ViewById(R.id.server_voice_play_iv)
	ImageView mServerPlayIV;
	
	@ViewById(R.id.local_record_play_iv)
	ImageView mRecordPlayIV;
	
	@ViewById(R.id.record_play_layout)
	RelativeLayout mRecordPlayLayout;
	
	@AfterViews
	void initView(){
		mPresenter = new LessonDetailPresenterImpl(this);
		mPresenter.initView();
		mViewPager.addOnPageChangeListener(this);
	}
	
	@Click({R.id.back_btn, R.id.server_voice_play_iv, R.id.local_record_play_iv})
	void onViewClick(View view) {
		switch (view.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.server_voice_play_iv:
			mPresenter.onVoiceImageClick();
			break;
		case R.id.local_record_play_iv:
			mPresenter.onRecordImageClick();
			break;
		default:
			break;
		}
	}
	
	long downTime = 0;
	long upTime = 0;
	
	@Touch({R.id.mic_imgview})
	void onViewTouch(View view, MotionEvent event){
		if (view.getId() == R.id.mic_imgview) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downTime = System.currentTimeMillis();
					mRippleLayout.startRippleAnimation();
					mPresenter.startRecord();
					break;
	            case MotionEvent.ACTION_UP:
	            	upTime = System.currentTimeMillis();
	            	mRippleLayout.stopRippleAnimation();
	            	if (upTime - downTime > 3000) { //时间大于3s再上传
	            		mPresenter.stopRecord(true);
	        		}else {
	        			mPresenter.stopRecord(false);
					}
	            	break;
			}
		}
	}
	
	@Override
	public void setTitleText(String title) {
		mTitleBar.setText(title);
	}

	@Override
	public void setBackBtnVisiable(int visiablity) {
		mBackBtn.setVisibility(visiablity);
	}

	@Override
	public void setViewPagerAdapter(PagerAdapter adapter) {
		mViewPager.setAdapter(adapter);
	}

	@Override
	public void addCircleView(View childView) {
		mDotsContainer.addView(childView);
	}

	@Override
	public View getCircelChildView(int index) {
		return mDotsContainer.getChildAt(index);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		mPresenter.setCurrentCardCirclePosition(arg0);
	}

	@Override
	public HoloCircularProgressBar getVoiceProgressBar() {
		return mServerPlayBar;
	}

	@Override
	public HoloCircularProgressBar getRecordProgressBar() {
		return mRecordPlayBar;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPresenter.onActivityDestroy();
	}

	@Override
	public void setVoiceImageView(int resourceId) {
		mServerPlayIV.setImageResource(resourceId);
	}

	@Override
	public void setRecordImageView(int resourceId) {
		mRecordPlayIV.setImageResource(resourceId);
	}

	@Override
	public void setRecordLayoutVisible(int visibility) {
		mRecordPlayLayout.setVisibility(visibility);
	}
	
}
