package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorDensifyByLength
import com.esri.core.geometry.OperatorFactoryLocal
import com.huida.navu3d.bean.WorkTaskData
import com.lei.core.base.BaseViewModel


class HomeVM : BaseViewModel() {
    private val homeRepo by lazy { HomeRepo(viewModelScope, errorLiveData) }
    private val unityRepo by lazy { UnityRepo(viewModelScope, errorLiveData) }
    val vtgData by lazy { homeRepo.vtgData }
    val ggaData by lazy { homeRepo.ggaData }
    val homeFragmentBean by lazy { homeRepo.homeFragmentBean }


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
        homeRepo.saveRecord()
    }

    /**
     * 画引导线
     */
    fun drawGuideLine() {
//        homeFragmentBean.creatGuideLine()
    }

    //补点操作
    var densifier = OperatorFactoryLocal
        .getInstance()
        .getOperator(Operator.Type.DensifyByLength) as OperatorDensifyByLength


    /**
     * 设置历史数据
     */
    fun setWorkTaskData(workTaskData: WorkTaskData) {
        homeRepo.setWorkTaskData(workTaskData)
    }

    /**
     * 当界面销毁的时候终止任务
     */
    override fun onCleared() {
        stop()
        super.onCleared()
    }
}