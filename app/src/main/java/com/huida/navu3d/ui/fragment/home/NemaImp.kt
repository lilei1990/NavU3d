package com.huida.navu3d.ui.fragment.home

import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
interface NemaImp {
    fun receive(gga: GGASentence)
    fun receive(vtg: VTGSentence)
}