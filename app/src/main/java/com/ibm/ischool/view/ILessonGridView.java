package com.ibm.ischool.view;

import android.widget.BaseAdapter;

public interface ILessonGridView extends IBaseView {

	void setTitleText(String title);
	
	void setBackBtnVisiable(int visiablity);

	void setGridAdapter(BaseAdapter adapter);
}
