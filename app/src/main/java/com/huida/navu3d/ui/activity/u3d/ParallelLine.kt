package com.huida.navu3d.ui.activity.u3d

import com.amap.api.maps.model.LatLng
import com.esri.core.geometry.*
import com.huida.navu3d.bean.PointXYData
import com.huida.navu3d.utils.GeoConvert
import com.lei.core.BaseApp
import java.util.*

/**
 * 作者 : lei
 * 时间 : 2021/01/29.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object ParallelLine {
    //偏移操作
    val offseter = OperatorFactoryLocal
        .getInstance()
        .getOperator(Operator.Type.Offset) as OperatorOffset

    //平行线的间隔
    val lineOffset = 10



    /**
     * 延长线
     * length:线延长多远
     * 直接更改B点的坐标
     */
    fun extLine(A: PointXYData, B: PointXYData, length: Int) {
        var distance = distanceOfTwoPoints(A, B)

        //算出变化后和之前比例
        var b = (length + distance) / distance
        //计算和延长的坐标Y
        B.Y = (B.Y - A.Y) * b + A.Y
        B.X = (B.X - A.X) * b + A.X
        val latlng = GeoConvert.INSTANCE.convertUTMToLatLon(B.X, B.Y)
        B.lat = latlng[0]
        B.lng = latlng[1]

    }

    fun convertGaoDe(B: PointXYData): LatLng {
        val latlngGaode =
            GeoConvert.INSTANCE.gaoDeConvert(LatLng(B.lat, B.lng), BaseApp.getContext())
        return latlngGaode
    }

    /**
     * utm坐标转高德.不要用,失真严重bug
     */
    fun utmToGaoDe(B: Point): LatLng {
        val utmLatlng = GeoConvert.INSTANCE.convertUTMToLatLon(B.x, B.y)

        val latlngGaode =
            GeoConvert.INSTANCE.gaoDeConvert(
                LatLng(utmLatlng[0], utmLatlng[1]),
                BaseApp.getContext()
            )

        return latlngGaode
    }

    /**
     * 计算两点之间的距离
     *
     * @param pp1
     * @param pp2
     * @return
     */
    fun distanceOfTwoPoints(pp1: PointXYData, pp2: PointXYData): Double {
        return Math.sqrt(
            Math.pow((pp2.X - pp1.X).toDouble(), 2.0) + Math.pow((pp2.Y - pp1.Y).toDouble(), 2.0)
        )
    }

    /**
     * 获取点到曲线的距离
     *
     * @param coordinate
     * @param coordinates
     * @return
     */
    fun getPointToCurveDis(pp1: Point, polyline: Polyline): Double {
        val distanceOperator = OperatorFactoryLocal.getInstance()
            .getOperator(Operator.Type.Distance) as OperatorDistance
        return distanceOperator.execute(pp1, polyline, null)

    }
}