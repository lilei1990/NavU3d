package com.huida.navu3d.ui.fragment.workTask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huida.navu3d.bean.WorkTaskData

class TaskListViewModel : ViewModel() {
    private val liveData = MutableLiveData<ArrayList<WorkTaskData>>()
    private val arrayListOf = arrayListOf<WorkTaskData>()
    fun loadInitData(): MutableLiveData<ArrayList<WorkTaskData>> {
        //from remote
        //为了适配因配置变更而导致的页面重建, 重复利用之前的数据,加快  新页面渲染，不再请求接口
        if (liveData.value == null) {
            for (i in 0..10) {
                arrayListOf.add(
                    WorkTaskData(


                    )
                )
            }
        }
        liveData.postValue(arrayListOf)
        return liveData

    }

    init {

    }

}