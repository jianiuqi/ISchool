package com.ibm.ischool.base;

import java.io.File;
import java.util.ArrayList;

import org.androidannotations.annotations.EApplication;

import com.ibm.ischool.util.CrashHandler;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * 功能描述：用于存放全局变量和公用的资源等
 * @author jnq
 */
@EApplication
public class BaseApplication extends Application {

	public static final String TAG = BaseApplication.class.getSimpleName();
	
	/**
	 * imageLoader显示图片选项
	 */
	private static DisplayImageOptions mOptions;
	
    /**
     * Activity集合
     */
    private static ArrayList<BaseActivity> activitys = new ArrayList<BaseActivity>();

    private static BaseApplication mAppInstance;
    
    @Override
	public void onCreate() {
		super.onCreate();
		mAppInstance = this;
		initImageLoader(getApplicationContext());
		CrashHandler.getInstance().init(getApplicationContext());
	}

    public static synchronized BaseApplication getAppInstance() {
		return mAppInstance;
	}
    
    /**
     * 添加Activity到ArrayList<Activity>管理集合
     * @param activity
     */
    public void addActivity(BaseActivity activity) {
        String className = activity.getClass().getName();
        for (Activity at : activitys) {
            if (className.equals(at.getClass().getName())) {
                activitys.remove(at);
                break;
            }
        }
        activitys.add(activity);
    }

	/**
	 * 对外提供DisplayImageOptions
	 * @return
	 */
	public static DisplayImageOptions getDisplayImageOption() {
		return mOptions;
	}
	
	@SuppressWarnings("deprecation")
	private static void initImageLoader(Context context) {
		File cacheDir =StorageUtils.getOwnCacheDirectory(context, Constant.APP_FOLDER_NAME+"/imgCache"); 
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).threadPriority(3)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(209715200)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(configuration);
		mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(null)
				.showImageForEmptyUri(null)
				.showImageOnFail(null)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer()).build();
	}
	
}
