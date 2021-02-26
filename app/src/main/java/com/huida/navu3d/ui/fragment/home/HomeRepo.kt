package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.ui.activity.unity.U3dVM
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :具体实现类
 */
class HomeRepo(
   val viewModelScope: CoroutineScope,
   val errorLiveData: MutableLiveData<ApiException>
) {
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
    var isRecord=false
    fun saveRecord() {
//        homeFragmentBean.creatNavLine()
        isRecord=!isRecord
        //打开小车轨迹
//        u3dVM.isShowTrack(isRecord, "FF00FF")
        homeFragmentBean.isRecord(isRecord)
    }
    /**
     * 设置历史数据
     */
    fun  setWorkTaskData(workTaskData: WorkTaskData) {
        homeFragmentBean.setWorkTaskData(workTaskData)
    }

    /**
     * 画引导线
     */
    fun drawGuideLine() {
//        homeFragmentBean.creatGuideLine()
    }
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


}