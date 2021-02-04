package com.huida.navu3d.constants

/**
 * 作者 : lei
 * 时间 : 2021/02/04.
 * 邮箱 :416587959@qq.com
 * 描述 :0是定位点,1是A点,2是B点,3是线的点,4是引导线的点
 */
object PointType {
    //0是定位点,1是A点,2是B点,3是线的点,4是引导线的点
    val LOCATION = 0x00000000
    val A = 0x00000001
    val B = 0x00000002
    val LINE = 0x00000003
    val GUIDE_LINE = 0x00000004
}