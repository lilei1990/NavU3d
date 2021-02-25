package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.huida.navu3d.common.NmeaProviderManager
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :数据实现层
 */
class NmeaRepo(
    viewModelScope: CoroutineScope,
    errorLiveData: MutableLiveData<ApiException>
) {
    val vtgData = MutableLiveData<VTGSentence>()
    val ggaData = MutableLiveData<GGASentence>()
    /**
     * 开始
     */
    fun start() {
        NmeaProviderManager.reset()
        NmeaProviderManager.start()
    }

    /**
     * 接收数据
     */
    fun receiveNmea(vtg: VTGSentence) {
        vtgData.postValue(vtg)
    }

    fun receiveNmea(gga: GGASentence) {
        ggaData.postValue(gga)
    }


}