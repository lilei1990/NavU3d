package com.huida.navu3d.utils

import com.esri.core.geometry.*
import uk.me.jstott.jcoord.UTMRef

/**
 * 作者 : lei
 * 时间 : 2021/02/18.
 * 邮箱 :416587959@qq.com
 * 描述 : 集合计算的工具类
 */
object GeometryUtils {
    val distanceOperator = OperatorFactoryLocal.getInstance()
        .getOperator(Operator.Type.Distance) as OperatorDistance

    /**
     * 获取点到曲线的距离
     *
     * @param coordinate
     * @param coordinates
     * @return
     */
    fun getPointToCurveDis(pp1: Point, polyline: Polyline): Double {

        return distanceOperator.execute(pp1, polyline, null)

    }

    /**
     * 延长线段
     * length:线延长多远
     * 直接更改B点的坐标
     */
    fun extLine(A: UTMRef, B: UTMRef, length: Int) {

        var distance = distanceOfTwoPoints(A, B)
        //算出变化后和之前比例
        var b = (length + distance) / distance
        //计算和延长的坐标Y
        B.easting = (B.easting - A.easting) * b + A.easting
        B.northing = (B.northing - A.northing) * b + A.northing


    }


    /**
     * 计算两点之间的距离
     *
     * @param pp1
     * @param pp2
     * @return
     */
    fun distanceOfTwoPoints(pp1: UTMRef, pp2: UTMRef): Double {

        return Math.sqrt(
            Math.pow(
                (pp2.easting - pp1.easting).toDouble(),
                2.0
            ) + Math.pow((pp2.northing - pp1.northing).toDouble(), 2.0)
        )
    }

}