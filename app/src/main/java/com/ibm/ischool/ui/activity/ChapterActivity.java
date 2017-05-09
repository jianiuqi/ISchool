package com.ibm.ischool.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.base.BaseActivity;
import com.ibm.ischool.presenter.IChapterPresenter;
import com.ibm.ischool.presenter.impl.ChapterPresenterImpl;
import com.ibm.ischool.ui.view.ClipViewPager;
import com.ibm.ischool.ui.view.ScalePageTransformer;
import com.ibm.ischool.view.IChapterView;

import android.annotation.SuppressLint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

@EActivity(R.layout.activity_chapter)
public class ChapterActivity extends BaseActivity implements IChapterView, OnPageChangeListener{

	@ViewById(R.id.back_btn)
	ImageButton mBackBtn;
	
	@ViewById(R.id.titlebar_text)
	TextView mTitleBar;
	
	@ViewById(R.id.viewpager)
	ClipViewPager mViewPager;
	
	@ViewById(R.id.card_position_dots_container)
	LinearLayout mCardDotsContainer;
	
	IChapterPresenter mPresenter;
	
	@AfterViews
	void initView(){
		mViewPager.setPageTransformer(true, new ScalePageTransformer());
    	findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility") 
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
    	mViewPager.addOnPageChangeListener(this);
		mPresenter = new ChapterPresenterImpl(this);
		mPresenter.initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.refresh();
	}
	
	@Click({R.id.back_btn})
	void onViewClick(View view){
		switch (view.getId()) {
		case R.id.back_btn:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void setBackBtnVisiable(int visiablity) {
		mBackBtn.setVisibility(visiablity);
	}
	
	@Override
	public void setTitleText(String title) {
		mTitleBar.setText(title);
	}

	@Override
	public void setPagerAdapter(PagerAdapter adapter) {
		mViewPager.setAdapter(adapter);
	}

	@Override
	public void setOffscreenPageLimit(int size) {
		mViewPager.setOffscreenPageLimit(size);
	}

	@Override
	public void addCardCircleView(View childView) {
		mCardDotsContainer.addView(childView);
	}

	@Override
	public View getCardCircelChildView(int index) {
		return mCardDotsContainer.getChildAt(index);
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
	public void removeCircleViews() {
		mCardDotsContainer.removeAllViews();
	}

}
