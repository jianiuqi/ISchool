package com.ibm.ischool.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.base.BaseActivity;
import com.ibm.ischool.presenter.ILessonGridPresenter;
import com.ibm.ischool.presenter.impl.LessonGridPresenterImpl;
import com.ibm.ischool.view.ILessonGridView;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

@EActivity(R.layout.activity_lesson_grid)
public class LessonGridActivity extends BaseActivity implements ILessonGridView{

	@ViewById(R.id.back_btn)
	ImageButton mBackBtn;
	
	@ViewById(R.id.titlebar_text)
	TextView mTitleBar;
	
	@ViewById(R.id.gridview)
	GridView mGridView;
	
	ILessonGridPresenter mPresenter;
	
	@AfterViews
	void initView(){
		mPresenter = new LessonGridPresenterImpl(this);
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
	public void setTitleText(String title) {
		mTitleBar.setText(title);
	}

	@Override
	public void setBackBtnVisiable(int visiablity) {
		mBackBtn.setVisibility(visiablity);
	}

	@Override
	public void setGridAdapter(BaseAdapter adapter) {
		mGridView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.initView();
	}
}
