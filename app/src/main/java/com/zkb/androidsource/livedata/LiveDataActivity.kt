package com.zkb.androidsource.livedata

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.zkb.androidsource.R

class LiveDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        init()

    }
    private fun init(){

        val text=findViewById<TextView>(R.id.text)

        val dataVm= ViewModelProvider(this).get(DataVM::class.java)
       dataVm.getData().observe(this, Observer {
           Log.d("kang","first:"+it.name)
           Log.d("kang","first:"+it.age)
        })

        findViewById<View>(R.id.change).setOnClickListener {
            dataVm.setData(11)
        }
        findViewById<View>(R.id.adult).setOnClickListener {
            dataVm.setData(23)
        }

   /*    dataVm.map().observe(this, Observer {
            Log.d("kang","map:"+it.age)
        })

*/


        dataVm.switchMapData().observe(this, Observer {
            Log.d("kang","switchMapData:"+it)

            text.text=it.name;
        })


        dataVm.switchMapDataObject().observe(this, Observer {
            Log.d("kang", "switchMapDataObject:$it")

            text.text=it
        })


    }
}