package com.ibm.ischool.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.ibm.ischool.R;
import com.ibm.ischool.presenter.IPersonalFragmentPresenter;
import com.ibm.ischool.presenter.impl.PersonalFragmentPresenterImpl;
import com.ibm.ischool.util.ResourceUtils;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

@EFragment(R.layout.fragment_main_personal)
public class PersonalFragment extends Fragment {

	@ViewById(R.id.titlebar_text)
	TextView mTitleBar;
	
	IPersonalFragmentPresenter mPresenter;
	
	@AfterViews
	void initView(){
		mPresenter = new PersonalFragmentPresenterImpl(getActivity());
		mTitleBar.setText(ResourceUtils.getStringResource(this.getContext(), R.string.main_personal_titlebar_text));
	}
	
	@Click({R.id.clear_data_layout_rl})
	void onViewClick(View view){
		switch (view.getId()) {
		case R.id.clear_data_layout_rl:
			mPresenter.clearData();
			break;
		default:
			break;
		}
	}
}
