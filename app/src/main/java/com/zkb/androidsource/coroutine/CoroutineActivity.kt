package com.zkb.androidsource.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import com.zkb.androidsource.R
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception

/**
  *
  * 协程的使用
  * @author:zhangkb
  * Date:2021/3/26 9:50
 */
class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        findViewById<View>(R.id.btnWriteFile).setOnClickListener {
           val isOk= test()

        }
        findViewById<View>(R.id.btnWriteFile).setOnClickListener {
            //val isOk= test()
            testUNDISPATCHED()
        }
        //主线程运行,调度器的目的就是切线程
        GlobalScope.launch(Dispatchers.Main) {
            try {
                //userNameView.text = getUserCoroutine().name
            }catch (e:Exception){
                //userNameView.text = getUserCoroutine().name
            }
        }
    }


    private fun test(){

        //启动一个子线程，没有start,确实这个更符合操作系统的想法。
        GlobalScope.launch {
            //do what you want
            writeFile("test.txt")
        }
        //线程启动模式
        startType()
    }


    private fun  startType(){
        /**
         * DEFAULT	立即执行协程体
        ATOMIC	立即执行协程体，但在开始运行之前无法取消
        UNDISPATCHED	立即在当前线程执行协程体，直到第一个 suspend 调用
        LAZY	只有在需要的情况下运行 ,调用 Job.start or job.join()，主动触发协程的调度执行
         */

        //启动一个子线程，没有start,确实这个更符合操作系统的想法。
        GlobalScope.launch(start = CoroutineStart.DEFAULT) {
            //do what you want
            writeFile("DEFAULT.txt")
            Log.d("kang","DEFAULT write ")
        }
        //ATOMIC 只有涉及 cancel 的时候才有意义
        GlobalScope.launch(start = CoroutineStart.ATOMIC) {
            //do what you want
            Log.d("kang","ATOMIC write ")
            writeFile("ATOMIC.txt")
        }

      val job=  GlobalScope.launch(start = CoroutineStart.LAZY) {
            //do what you want
          Log.d("kang","LAZY write ")
            writeFile("LAZY.txt")
        }
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {

            Log.d("kang","唤醒 LAZY write ")
            //LAZY 要等待 UNDISPATCHED的唤醒,如果用start一般是UNDISPATCHED执行完，才执行LAZY
            //如果用join，就是需要等待join的协程执行完毕
            job.start()
           // job.join()

            //do what you want
            Log.d("kang","ATOMIC write ")
            writeFile("ATOMIC.txt")



        }
    }

    private suspend  fun get(){

    }

    private  fun testUNDISPATCHED(){

        log(1)

        //UNDISPATCHED 模式 执行完毕才会执行下面的语句。这个有意思？啥原理呢？
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            log(2)

        }
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            //delay(100)
            log(3)
        }
        //
        GlobalScope.launch(start = CoroutineStart.DEFAULT) {
            //delay(100)
            log(4)
        }


       // job.join()
        log(5)

    }

    private fun  writeFile(fileName:String):Boolean{

        //sdcard 卡下
        val filePath=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file= File(filePath,fileName)

        if(!file.exists()){
            if (!file.createNewFile()){
                Log.d("kang","$fileName write failure")
                return false
            }
        }


        Log.d("kang","$fileName write success")

        return true
    }

    private fun log(msg:String){
        Log.d("kang",msg)
    }
    private fun log(msg:Int){
        Log.d("kang", msg.toString())
    }
}