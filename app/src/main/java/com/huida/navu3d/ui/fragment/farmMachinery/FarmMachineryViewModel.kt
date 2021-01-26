package com.huida.navu3d.ui.fragment.farmMachinery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huida.navu3d.bean.FarmMachineryData
import com.huida.navu3d.db.AppDataBase

class FarmMachineryViewModel : ViewModel() {
    //存放所有数据
    private val liveData = MutableLiveData<FarmMachineryData>()

    //存放某一块数据,页面数据
    var firstData = FarmMachineryData()

    private val farmMachineryDao by lazy {
        AppDataBase.getInstance()
            .collectFarmMachineryDao()
    }

    /**
     * 获取农具的数据
     */
    fun getData(): LiveData<FarmMachineryData> {
        //数据库请求数据
        val farmToolsDatas = farmMachineryDao.getAlls()
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
    fun saveData() {
        //数据库保存
        farmMachineryDao
            .insert(firstData)


    }
}