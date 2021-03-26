package com.zkb.androidsource.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

public class MyTextView2 extends androidx.appcompat.widget.AppCompatTextView {
    public MyTextView2(Context context) {
        super(context);
    }

    public MyTextView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("kang","MyTextView2 dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("kang","MyTextView2 onTouchEvent");
       // return super.onTouchEvent(event);
        return true;

    }
}
