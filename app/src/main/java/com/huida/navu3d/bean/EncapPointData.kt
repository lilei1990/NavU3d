package com.huida.navu3d.bean

import com.esri.core.geometry.Point

/**
 * 作者 : lei
 * 时间 : 2021/03/01.
 * 邮箱 :416587959@qq.com
 * 描述 : 封装扩展geometry
 */
class EncapPointData : Point() {
    var lat = 0.0
    var lng = 0.0


    fun toPointData(): PointData {
        val pointData = PointData()
        pointData.lat=lat
        pointData.lng=lng
        pointData.x=x
        pointData.y=y
        return pointData
    }

}