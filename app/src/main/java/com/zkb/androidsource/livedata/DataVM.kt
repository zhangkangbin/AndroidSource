package com.zkb.androidsource.livedata

import android.util.Log
import androidx.lifecycle.*

class DataVM : ViewModel () {

  //  private lateinit var data: MutableLiveData<DataBean>

    private val data = MutableLiveData<DataBean>()
    fun getData(): MutableLiveData<DataBean> {
/*
        if (::data.isInitialized) {
            return data
        }
        data = MutableLiveData<DataBean>()*/
        return data
    }


/*    fun isAdult(): LiveData<String>  {

     val map=  Transformations.map(data){


        }

    }*/


    /**
     * 数据过滤
     */
    fun  map(): LiveData<DataBean> {
    return   data.map {

        if(it.age==11){
            it.name="11age"
        }

        it
        }
    }
    fun getGradeName(age:Int):LiveData<String>{
        val liveData = MutableLiveData<String>()
        liveData.value = "年级是$age"
        return liveData
    }

    /**
     * 数据切换
     */
    fun switchMapData(): LiveData<DataBean> {

      return data.switchMap {
          val liveData = MutableLiveData<DataBean>()
          liveData.value = it
          liveData

      }


    }
    fun switchMapDataObject(): LiveData<String> {

        return getData().switchMap {

            getGradeName(11)

        }
    }

    fun setData(age:Int) {
        val value = DataBean("XiaoBao_set1", age)
       //  getData().postValue(value)
        //main线程
          getData().value = value
    }

    fun postData() {

        Thread() {
            run {
                val value = DataBean("XiaoBao_post", 18)
                //子线程
                getData().postValue(value)

            }
        }.start();


    }

    override fun onCleared() {
        super.onCleared()
    }
}


