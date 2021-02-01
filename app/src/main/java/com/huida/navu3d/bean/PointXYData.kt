package com.huida.navu3d.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.esri.core.geometry.Polyline

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 : 轨迹点的数据模型
 */
@Entity(tableName = "point_xy")
class PointXYData : Cloneable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Int = 0
    var workTaskId: Int = 0

    //0是定位点,1是A点,2是B点
    var type: Int = 0
    var lat = 0.0
    var lng = 0.0

    //高德用的
    var latGC102 = 0.0
    var lngGC102 = 0.0

    //Utm
    var X = 0.0
    var Y = 0.0

    override fun toString(): String {
        return "PointXY(lat=$lat, lng=$lng, latGC102=$latGC102, lngGC102=$lngGC102, X=$X, Y=$Y)"
    }

//    fun convertPolyline(): Polyline {
//        val polyline = Polyline()
//        polyline.setPoint()
//        return polyline
//    }
}