package com.ibm.ischool.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import com.ibm.ischool.R;
import com.ibm.ischool.base.BaseActivity;

import android.content.Intent;
import android.os.Handler;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

	@AfterViews
	void initView() {
		new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(mContext, MainActivity_.class);
                startActivity(mainIntent);
                finish();
            }
        }, 1500);
	}

}
