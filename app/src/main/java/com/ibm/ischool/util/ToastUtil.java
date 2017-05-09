package com.ibm.ischool.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Toast工具类
 * @author jnq
 *
 */
public class ToastUtil {
	
	private static Toast toast = null;
	public static int LENGTH_LONG = Toast.LENGTH_LONG;
	private static int LENGTH_SHORT = Toast.LENGTH_SHORT;

	/**
	 * 普通文本短时间消息提示
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showShortToast(Context context, String text) {
		toast = Toast.makeText(context, text, LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0); // 设置Toast提示消息在屏幕上的位置
		toast.show();
	}

	/**
	 * 普通文本长时间消息提示
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showLongToast(Context context, String text) {
		toast = Toast.makeText(context, text, LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	 * 带图片消息提示
	 * 
	 * @param context
	 * @param ImageResourceId
	 * @param text
	 * @param duration
	 */
	public static void showImageToast(Context context, int ImageResourceId,
			String text, int duration) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		// 获取Toast提示消息里原有的View
		View toastView = toast.getView();
		// 创建一个ImageView
		ImageView img = new ImageView(context);
		img.setImageResource(ImageResourceId);
		// 创建一个LineLayout容器
		LinearLayout ll = new LinearLayout(context);
		// 向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img);
		ll.addView(toastView);
		// 将LineLayout容器设置为toast的View
		toast.setView(ll);
		// 显示消息
		toast.show();
	}
}
