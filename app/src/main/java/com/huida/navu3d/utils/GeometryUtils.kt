package com.huida.navu3d.utils

import com.esri.core.geometry.*
import com.huida.navu3d.bean.PointXYData

/**
 * 作者 : lei
 * 时间 : 2021/02/18.
 * 邮箱 :416587959@qq.com
 * 描述 : 集合计算的工具类
 */
object GeometryUtils {
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

    /**
     * 延长线段
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

}