package com.zkb.androidsource.layout


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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