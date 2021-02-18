package com.huida.navu3d.utils

import com.amap.api.maps.model.LatLng
import com.huida.navu3d.bean.PointXYData
import com.lei.core.BaseApp

/**
 * 作者 : lei
 * 时间 : 2021/02/18.
 * 邮箱 :416587959@qq.com
 * 描述 :转换point实例
 */
object PointConvert {

    fun convertPoint(latitude: Double, longitude: Double): PointXYData {
        val pointXY = PointXYData()
        val convertUTM = GeoConvert.INSTANCE.convertLatLonToUTM(latitude, longitude)

        pointXY.lat = latitude
        pointXY.lng = longitude

        pointXY.X = convertUTM[0]
        pointXY.Y = convertUTM[1]
        return pointXY
    }
}