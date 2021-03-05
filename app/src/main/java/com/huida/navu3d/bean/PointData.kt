package com.huida.navu3d.bean

import org.litepal.crud.LitePalSupport
import uk.me.jstott.jcoord.LatLng
import uk.me.jstott.jcoord.UTMRef
import java.io.Serializable

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 : 轨迹点的数据模型
 */


open class PointData : LitePalSupport(), Serializable {

    //0是定位点,1是A点,2是B点,3是线的点,4是引导线的点
    var type: Int = 0

    var lat = 0.0
    var lng = 0.0

    //Utm,不存储到数据库
    var x = 0.0

    var y = 0.0

    //线的标识
    var trackLineId = -1L

    var lngZone: Int? = null
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
            pointData.x = utm.easting
            pointData.y = utm.northing
            pointData.lngZone = utm.lngZone
            pointData.latZone = utm.latZone
            return pointData
        }

        /**
         * 根据utm数据创建对象
         */
        fun build(lngZone: Int, latZone: Char, x: Double, y: Double): PointData {
            val pointData = PointData()
            var latLng = UTMRef(lngZone, latZone, x, y).toLatLng()
            pointData.lat = latLng.latitude
            pointData.lng = latLng.longitude
            pointData.x = x
            pointData.y = y
            pointData.lngZone = lngZone
            pointData.latZone = latZone
            return pointData
        }
    }

    fun getId(): Long {
        return baseObjId
    }

    /**
     * 复制一个对象
     */
    fun copy(): PointData {
        return build(lat, lng)
    }

    override fun toString(): String {
        return "PointData(x=$x, y=$y)"
    }

}