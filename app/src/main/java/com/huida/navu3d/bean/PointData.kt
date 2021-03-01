package com.huida.navu3d.bean

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import uk.me.jstott.jcoord.LatLng
import uk.me.jstott.jcoord.UTMRef

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

    //Utm,不存储到数据库
    @Column(ignore = true)
    var x = 0.0

    @Column(ignore = true)
    var y = 0.0

    @Column(ignore = true)
    var lngZone: Int? = null
    @Column(ignore = true)
    var latZone: Char? = null
    companion object {
        /**
         * 根据经纬度数据生成对象
         */
        fun build(latitude: Double, longitude: Double): PointData {
            val pointData = PointData()
            pointData.lat = latitude
            pointData.lng = longitude
            val utm = LatLng(latitude, longitude).toUTMRef()
            pointData.x=utm.easting
            pointData.y=utm.northing
            pointData.lngZone=utm.lngZone
            pointData.latZone=utm.latZone
            return pointData
        }

        /**
         * 根据utm数据创建对象
         */
        fun build(lngZone: Int, latZone: Char, x: Double, y: Double): PointData {
            val pointData = PointData()
            var latLng = UTMRef(lngZone, latZone, x, y).toLatLng()
            pointData.lat=latLng.latitude
            pointData.lng=latLng.longitude
            return pointData
        }
    }


}