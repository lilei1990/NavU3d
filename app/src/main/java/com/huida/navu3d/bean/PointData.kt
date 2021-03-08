package com.huida.navu3d.bean

import com.esri.core.geometry.Point
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


data class PointData(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    //线的标识
    var trackLineId: Long = -1L
) : LitePalSupport(), Serializable {


    fun getId(): Long {
        return baseObjId
    }

    /**
     * 复制一个对象
     */
    fun copy(): PointData {
        return PointData(lat, lng)
    }

    fun toUtm(): UTMRef {
        return LatLng(lat, lng).toUTMRef()
    }

    fun toPoint(): Point {
        val utm = toUtm()
        return Point(utm.easting, utm.northing)
    }
}