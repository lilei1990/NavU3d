package com.huida.navu3d.bean

import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 : 轨迹点的数据模型
 */


open class PointData : LitePalSupport() {
    //0是定位点,1是A点,2是B点,3是线的点,4是引导线的点
    var type: Int = 0

    var lat = 0.0
    var lng = 0.0

    //Utm
    var X = 0.0
    var Y = 0.0


}