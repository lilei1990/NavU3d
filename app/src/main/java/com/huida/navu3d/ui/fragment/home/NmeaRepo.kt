package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.huida.navu3d.common.NmeaProviderManager
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.sf.marineapi.nmea.sentence.GGASentence

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :数据实现层
 */
class NmeaRepo(
    private val coroutineScope: CoroutineScope,
    errorLiveData: MutableLiveData<ApiException>
) {

    /**
     * 开始
     */
    fun start(
        nemaImp: NemaImp
    ) {
        coroutineScope.launch {
            NmeaProviderManager.reset()
            NmeaProviderManager.start()
            NmeaProviderManager.registGGAListen("HomeViewModel") {
                nemaImp.receive(it)
            }
            NmeaProviderManager.registVTGListen("HomeViewModel") {
                nemaImp.receive(it)
            }
        }

    }

    /**
     * 停止
     */
    fun stop() {
        NmeaProviderManager.stop()
    }


}