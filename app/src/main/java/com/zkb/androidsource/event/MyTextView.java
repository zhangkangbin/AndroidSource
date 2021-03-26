package com.zkb.androidsource.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {


    private List<View> child=new ArrayList<View>();
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void addTouch(View view){

        child.add(view);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("kang","MyTextView dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("kang","MyTextView onTouchEvent");
       // return super.onTouchEvent(event);
        return true;

    }
}
