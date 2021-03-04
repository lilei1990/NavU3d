package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorDensifyByLength
import com.esri.core.geometry.OperatorFactoryLocal
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.liveEvenBus
import com.lei.core.base.BaseViewModel
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence


class HomeVM : BaseViewModel() {
    private val homeRepo by lazy { HomeRepo(viewModelScope, errorLiveData, this) }
    val homeFragmentBean = HomeFragmentBean()
    val ggaBus = liveEvenBus(BusEnum.NMEA_GGA,GGASentence::class.java)
    val ggaObserve : Observer<GGASentence> = Observer {
        Log.d("TAGlilei", "${it.toString()}")
    }

    val vtgBus = liveEvenBus(BusEnum.NMEA_VTG,VTGSentence::class.java)
    val vtgObserve: Observer<VTGSentence> = Observer {
        Log.d("TAGlilei", "${it.toString()}")
    }

    /**
     * 开始
     */
    fun start() {
        homeRepo.start()
//        ggaBus.observeForever(ggaObserve)
//        vtgBus.observeForever(vtgObserve)
    }

    /**
     * 停止
     */
    fun stop() {
        homeRepo.stop()
//        ggaBus.removeObserver(ggaObserve)
//        vtgBus.removeObserver(vtgObserve)
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


}