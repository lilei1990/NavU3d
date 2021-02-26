package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorDensifyByLength
import com.esri.core.geometry.OperatorFactoryLocal
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.WorkTaskData
import com.lei.core.base.BaseViewModel
import com.unity3d.player.UnityPlayer
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence


class HomeVM : BaseViewModel() {
    private val homeRepo by lazy { HomeRepo(viewModelScope, errorLiveData, this) }
    private val unityRepo by lazy { UnityRepo(viewModelScope, errorLiveData, this) }
    val vtgData = MutableLiveData<VTGSentence>()
    val ggaData = MutableLiveData<GGASentence>()
    val homeFragmentBean = HomeFragmentBean()

    /**
     * 开始
     */
    fun start() {
        unityRepo.start()
        homeRepo.start()
    }

    /**
     * 停止
     */
    fun stop() {
        homeRepo.stop()
        unityRepo.stop()
    }

    /**
     * 移动
     */
    fun move() {
        homeFragmentBean.currenLatLng.value?.apply {
            unityRepo.moveCart(this, 2.0)
        }
    }
    /**
     * 小车姿态
     */
    fun stance() {

        homeFragmentBean.steerAngle.value?.apply {
            unityRepo.cartStance(this)
        }
    }

    /**
     * 设置A点
     */
    fun setPointA() {
        homeRepo.setPointA()
        unityRepo.setPointA()
    }


    /**
     * 设置B点
     */

    fun setPointB() {
        homeRepo.setPointB()
        unityRepo.setPointB()
    }

    /**
     * 暂停
     */
    fun pause() {

        homeRepo.pause()
        unityRepo.pause()

    }

    /**
     * 录制
     */

    fun saveRecord() {
        homeRepo.saveRecord()
        unityRepo.saveRecord()
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
     * 切换昼夜模式
     * “day”白天，“night”晚上
     */
    fun switchLight() {
        unityRepo.switchLight()
    }

    /**
     * 当界面销毁的时候终止任务
     */
    override fun onCleared() {
        stop()
        super.onCleared()
    }


}