package com.zkb.androidsource.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.zkb.androidsource.R

class AsyncLayoutInflaterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //异步加载布局
        AsyncLayoutInflater(this).inflate(R.layout.activity_async_layout_inflater,null)
        { view: View, i: Int, viewGroup: ViewGroup? ->
             //异步加载布局
            setContentView(view)
        }
    }
}