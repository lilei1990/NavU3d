package com.huida.navu3d.ui.fragment.workTask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huida.navu3d.bean.FarmToolsData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.db.AppDataBase

class WorkTaskViewModel : ViewModel() {
    private val farmMachineryDao by lazy {
        AppDataBase.getInstance()
            .collectFarmMachineryDao()
    }
    private val workTaskDao by lazy {
        AppDataBase.getInstance()
            .collectWorkTaskDao()
    }

     val workTaskDatas = MutableLiveData<ArrayList<WorkTaskData>>()
    //存放某一块数据,页面数据
    val workTaskData = WorkTaskData()
    fun loadInitData(): MutableLiveData<ArrayList<WorkTaskData>> {
         val arrayListOf = arrayListOf<WorkTaskData>()
        val alls = workTaskDao.getAlls()
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
        val farmToolsDatas = farmMachineryDao.getAlls()
        //如果有数据就仅取第一个数据
        farmToolsDatas.apply {
//            if (this!!.size > 0) firstData = this[0]
        }
    }
    /**
     * 获取农具数据
     */
    fun addFarmToolsData() {
        workTaskDao.insert(workTaskData)
    }
}