package com.ibm.ischool.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ibm.ischool.view.IBaseView;
import com.kaopiz.kprogresshud.KProgressHUD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity extends  FragmentActivity implements IBaseView {
	
	/** 
     * 记录处于前台的Activity 
     */  
    private static BaseActivity mForegroundActivity = null;  
    /** 
     * 记录所有活动的Activity 
     */  
    private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();  
  
    protected Context mContext;
    
    KProgressHUD mDialog;
    
    @SuppressLint("InlinedApi")
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        mContext = this;
        mDialog = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        		.setCancellable(true).setLabel("Please wait").setDimAmount(0.5f);
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏    
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏  
//      android:fitsSystemWindows="true"  android:clipToPadding="true"
    }
  
    @Override  
    protected void onResume() {  
        mForegroundActivity = this;  
        super.onResume();  
    }  
  
    @Override  
    protected void onPause() {  
        mForegroundActivity = null;  
        super.onPause();  
    }  
  
    /** 
     * 关闭所有Activity 
     */  
    public static void finishAll() {  
        List<BaseActivity> copy;  
        synchronized (mActivities) {  
            copy = new ArrayList<BaseActivity>(mActivities);  
        }  
        for (BaseActivity activity : copy) {  
            activity.finish();  
        }  
    }  
  
    /** 
     * 关闭所有Activity，除了参数传递的Activity 
     */  
    public static void finishAll(BaseActivity except) {  
        List<BaseActivity> copy;  
        synchronized (mActivities) {  
            copy = new ArrayList<BaseActivity>(mActivities);  
        }  
        for (BaseActivity activity : copy) {  
            if (activity != except)  
                activity.finish();  
        }  
    }  
  
    /** 
     * 是否有启动的Activity 
     */  
    public static boolean hasActivity() {  
        return mActivities.size() > 0;  
    }  
  
    /** 
     * 获取当前处于前台的activity 
     */  
    public static BaseActivity getForegroundActivity() {  
        return mForegroundActivity;  
    }  
  
    /** 
     * 获取当前处于栈顶的activity，无论其是否处于前台 
     */  
    public static BaseActivity getCurrentActivity() {  
        List<BaseActivity> copy;  
        synchronized (mActivities) {  
            copy = new ArrayList<BaseActivity>(mActivities);  
        }  
        if (copy.size() > 0) {  
            return copy.get(copy.size() - 1);  
        }  
        return null;  
    }  
  
    /** 
     * 退出应用 
     */  
    public void exitApp() {  
        finishAll();  
        android.os.Process.killProcess(android.os.Process.myPid());  
    }
    
    @Override
    public void setDialogShow() {
    	mDialog.show();
    }
    
    @Override
    public void setDialogDismiss() {
    	mDialog.dismiss();
    }
}
