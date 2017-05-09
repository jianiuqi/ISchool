package com.ibm.ischool.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.adapter.MyFragmentPagerAdapter;
import com.ibm.ischool.base.BaseActivity;
import com.ibm.ischool.ui.fragment.LevelFragment_;
import com.ibm.ischool.ui.fragment.PersonalFragment_;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@ViewById(R.id.tab_radio_group)
	RadioGroup mTabGroup;
	
	@ViewById(R.id.viewPager)
	ViewPager mViewPager;
	
	LevelFragment_ mLevelFragment;
	
	@AfterViews
	void initView() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        mLevelFragment = new LevelFragment_();
        fragments.add(mLevelFragment);
        fragments.add(new PersonalFragment_());

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(myFragmentPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 自由滑动结束的时候，会调用这个方法，即第三个方法的状态变成 1--> 2-->0(非首尾两页) 时 或 1--->0(第一页和最后一页)，会调用这个方法
            	if (position == 0) {
            		mLevelFragment.refresh();
				}
                RadioButton childAt = (RadioButton) mTabGroup.getChildAt(position);
                childAt.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.lesson_rb:
                        // 有一个方法，是可以指定ViewPager当前显示的Item的
                    	mViewPager.setCurrentItem(0);
                        break;
                    case R.id.personal_rb:
                        // 有一个方法，是可以指定ViewPager当前显示的Item的
                    	mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });
	}

}
