package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.*
import com.lei.core.base.BaseViewModel
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence


class HomeVM : BaseViewModel() ,NemaImp {
    private val repo by lazy { NmeaRepo(viewModelScope, errorLiveData) }



    val vtgData = MutableLiveData<VTGSentence>()
    val ggaData = MutableLiveData<GGASentence>()
    val homeFragmentBean = HomeFragmentBean()


    /**
     * 开始
     */
    fun start() {
        repo.start(this)
    }

    /**
     * 停止
     */
    fun stop() {
        repo.stop()
    }
    /**
     * 设置A点
     */
    fun setPointA() {
        homeFragmentBean.creatPointA()
    }


    /**
     * 设置B点
     */

    fun setPointB() {
        homeFragmentBean.creatPointB()
    }

    /**
     * 暂停
     */
    fun pause() {


    }

    /**
     * 录制
     */
    fun saveRecord() {

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
     * 接收GGA
     */
    override fun receive(gga: GGASentence) {
        ggaData.postValue(gga)
        homeFragmentBean.putGGA(gga)
    }
    /**
     * 接收VTG
     */
    override fun receive(vtg: VTGSentence) {
        vtgData.postValue(vtg)
        homeFragmentBean.putVTG(vtg)
    }


}