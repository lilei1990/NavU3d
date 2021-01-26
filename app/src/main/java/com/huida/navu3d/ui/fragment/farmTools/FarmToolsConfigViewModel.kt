package com.huida.navu3d.ui.fragment.farmTools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huida.navu3d.bean.FarmToolsData
import com.huida.navu3d.db.AppDataBase

class FarmToolsConfigViewModel : ViewModel() {
    //存放所有数据
    private val liveData = MutableLiveData<FarmToolsData>()
    //存放某一块数据,页面数据
    var firstData = FarmToolsData()

    /**
     * 获取农具的数据
     */
    fun getFarmToolsData(): LiveData<FarmToolsData> {
        //数据库请求数据
        val farmToolsDatas = AppDataBase.getInstance()
            .collectFarmToolsDao().getAlls()
        //如果有数据就仅取第一个数据
        farmToolsDatas.apply {
            if (this!!.size > 0) firstData = this[0]
        }

        if (liveData.value == null) {
            liveData.postValue(firstData)
        }
        return liveData

    }

    /**
     * 保存农具的数据
     */
    fun setFarmToolsData() {
        //数据库保存
        AppDataBase.getInstance()
            .collectFarmToolsDao()
            .insert(firstData)


    }
}