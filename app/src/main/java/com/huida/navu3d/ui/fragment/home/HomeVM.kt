package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.*
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.NmeaProviderManager
import com.lei.core.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence


class HomeVM : BaseViewModel()  {

    val vtgData = MutableLiveData<VTGSentence>()
    val ggaData = MutableLiveData<GGASentence>()
    val homeFragmentBean = HomeFragmentBean()


    /**
     * 开始
     */
    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            NmeaProviderManager.reset()
            NmeaProviderManager.start()
            NmeaProviderManager.registGGAListen("HomeViewModel") {
                receive(it)
            }
            NmeaProviderManager.registVTGListen("HomeViewModel") {
                receive(it)
            }
        }
    }

    /**
     * 停止
     */
    fun stop() {
        NmeaProviderManager.stop()
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
//        homeFragmentBean.creatNavLine()

        homeFragmentBean.isRecord(true)
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
     fun receive(gga: GGASentence) {
        ggaData.postValue(gga)
        homeFragmentBean.putGGA(gga)
    }
    /**
     * 接收VTG
     */
     fun receive(vtg: VTGSentence) {
        vtgData.postValue(vtg)
        homeFragmentBean.putVTG(vtg)
    }

    /**
     * 设置历史数据
     */
    fun  setWorkTaskData(workTaskData: WorkTaskData) {
        homeFragmentBean.setWorkTaskData(workTaskData)
    }


}