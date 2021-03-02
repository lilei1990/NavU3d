package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.ui.fragment.home.HomeFragmentBean.Status.*
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
 * 描述 :具体实现类,主要数据的处理
 */
class HomeRepo(
    val viewModelScope: CoroutineScope,
    val errorLiveData: MutableLiveData<ApiException>,
    homeVM: HomeVM
) {
    val homeFragmentBean by lazy { homeVM.homeFragmentBean }
    val REGIST_FLAG = "HomeViewModel"

    val ggaListener: (GGASentence) -> Unit = {
        receive(it)
    }
    val vtgListener: (VTGSentence) -> Unit = {
        receive(it)
    }

    /**
     * 开始
     */
    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            homeFragmentBean.status.postValue(START)
            NmeaProviderManager.start()
            NmeaProviderManager.registGGAListen(REGIST_FLAG, ggaListener)
            NmeaProviderManager.registVTGListen(REGIST_FLAG, vtgListener)
        }
    }

    /**
     * 停止
     */
    fun stop() {
        homeFragmentBean.status.postValue(STOP)
        NmeaProviderManager.removeRegist(REGIST_FLAG)
        NmeaProviderManager.reset()
        NmeaProviderManager.stop()
    }

    /**
     * 暂停
     */
    fun pause() {
        homeFragmentBean.status.postValue(PAUSE)
        NmeaProviderManager.removeRegist(REGIST_FLAG)
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
     * 录制
     */
    fun saveRecord() {

        homeFragmentBean.isRecord(true)
    }

    /**
     * 设置历史数据
     */
    fun setWorkTaskData(workTaskData: WorkTaskData) {
        homeFragmentBean.setWorkTaskData(workTaskData)

    }

    /**
     * 画引导线
     */
    fun drawGuideLine() {
        homeFragmentBean.creatGuideLine()
    }

    /**
     * 接收GGA
     */
    fun receive(gga: GGASentence) {
        homeFragmentBean.putGGA(gga)
    }

    /**
     * 接收VTG
     */
    fun receive(vtg: VTGSentence) {
        homeFragmentBean.putVTG(vtg)
    }


}