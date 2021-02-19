package com.huida.navu3d.ui.fragment.workTask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huida.navu3d.bean.FarmToolsData
import com.huida.navu3d.bean.WorkTaskData
import org.litepal.LitePal

class WorkTaskViewModel : ViewModel() {


    val workTaskDatas = MutableLiveData<ArrayList<WorkTaskData>>()

    //存放某一块数据,页面数据
    val workTaskData = WorkTaskData()

    //当前选择的数据,用于共享给homefragment
    var selectWorkTaskData: WorkTaskData? = null
    fun loadInitData(): MutableLiveData<ArrayList<WorkTaskData>> {
        val arrayListOf = arrayListOf<WorkTaskData>()
        val alls =
                LitePal.findAll(WorkTaskData::class.java, true)
        alls?.apply {
            this.forEach {
                arrayListOf.add(it)
            }
        }

        workTaskDatas.postValue(arrayListOf)
        return workTaskDatas

    }

    /**
     * 获取农具数据
     */
    fun getFarmToolsData() {
        //数据库请求数据
        val farmToolsDatas =
                LitePal.findAll(FarmToolsData::class.java)
        //如果有数据就仅取第一个数据
        farmToolsDatas.apply {
//            if (this!!.size > 0) firstData = this[0]
        }
    }

    /**
     * 获取农具数据
     */
    fun addFarmToolsData() {
        workTaskData.save()
    }
}