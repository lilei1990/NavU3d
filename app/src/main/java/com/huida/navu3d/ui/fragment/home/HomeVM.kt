package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorDensifyByLength
import com.esri.core.geometry.OperatorFactoryLocal
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.liveEvenBus
import com.lei.core.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence


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

    //补点操作
    var densifier = OperatorFactoryLocal
        .getInstance()
        .getOperator(Operator.Type.DensifyByLength) as OperatorDensifyByLength


    /**
     * 切换昼夜模式
     * “day”白天，“night”晚上
     */
    fun switchLight() {

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
//        val homeFragmentBean = HomeFragmentBean()
        homeRepo.workTaskData = workTask
        flow<WorkTaskData> {
            workTask?.findGuideLines()
            workTask?.findTrackLines()
            emit(workTask!!)
        }.flowOn(Dispatchers.IO).map {
            //导航线的数据
            val guideLineData = it?.guideLineData
            homeRepo.pointA.setValue(guideLineData?.getStart())
            homeRepo.pointB.setValue(guideLineData?.getEnd())
            homeRepo.creatGuideLine()
            //轨迹的数据
            val trackLineData = it?.trackLineData
            trackLineData?.apply {
                trackLineData.forEachIndexed { index, data ->
                    data.findPoint()
                }
                homeRepo.trackLineHistory.postValue(trackLineData)
            }
        }.flowOn(Dispatchers.Main).collect {
            ToastUtils.showLong("历史数据加载完成")
        }


    }


}