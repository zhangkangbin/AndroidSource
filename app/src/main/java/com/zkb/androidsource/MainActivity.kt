package com.zkb.androidsource

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.zkb.androidsource.layout.AsyncLayoutInflater
import com.zkb.androidsource.layout.AsyncLayoutInflaterActivity
import com.zkb.androidsource.livedata.LiveDataActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.test).setOnClickListener {
            startActivity(Intent(this, LiveDataActivity::class.java))

        }

    /*    var test=findViewById<ViewPager>(R.id.NO_DEBUG)
        var ViewPager2=findViewById<ViewPager2>(R.id.NO_DEBUG)

        AsyncLayoutInflater(this).inflate(R.layout.activity_main,null)
        { view: View, i: Int, viewGroup: ViewGroup? ->
            //异步加载布局
            setContentView(view)

        }*/
    }
}