package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.*
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.utils.GeometryUtils
import com.lei.core.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.me.jstott.jcoord.LatLng
import uk.me.jstott.jcoord.UTMRef
import kotlin.math.roundToInt


class HomeVM : BaseViewModel() {
    val homeRepo by lazy { HomeRepo(viewModelScope, errorLiveData, this) }


    /**
     * 开始
     */
    fun start() {
        homeRepo.start()

    }

    /**
     * 停止
     */
    fun stop() {
        homeRepo.stop()

    }


    /**
     * 设置A点
     */
    fun setPointA() {
        homeRepo.setPointA()
    }


    /**
     * 设置B点
     */

    fun setPointB() {
        homeRepo.setPointB()
    }

    /**
     * 暂停
     */
    fun pause() {
        homeRepo.pause()
    }

    /**
     * 录制
     */

    fun saveRecord() {
        homeRepo.openRecord()
    }

    /**
     * 画引导线
     */
    fun drawGuideLine() {
        homeRepo.drawGuideLine()
    }



    /**
     * 当界面销毁的时候终止任务
     */
    override fun onCleared() {
        stop()
        super.onCleared()
    }

    /**
     * 添加平行线
     */
    fun addParallelLine() {

    }

    suspend fun setWorkTaskData(workTask: WorkTaskData?) {
        homeRepo.setWorkTaskData(workTask)
    }


}