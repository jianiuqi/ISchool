package com.ibm.ischool.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by jnq on 2017/5/11.
 * 显示光标且点击后无反应
 */

@SuppressLint("AppCompatCustomView")
public class CursorTextView extends EditText {


    public CursorTextView(Context context) {
        super(context);
    }

    public CursorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

}
