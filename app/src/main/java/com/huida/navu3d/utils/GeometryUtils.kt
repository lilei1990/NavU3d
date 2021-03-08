package com.huida.navu3d.utils

import com.esri.core.geometry.*
import com.huida.navu3d.constants.Constants.interval
import com.huida.navu3d.constants.Constants.overlapskip
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
    val operatorTouches = OperatorFactoryLocal.getInstance()
        .getOperator(Operator.Type.Touches) as OperatorTouches

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

    /**
     * 计算两个集合是否相交
     */
    fun geometryTouches(
        geometryA: Geometry?,
        geometryB: Geometry?
    ): Boolean {

        return OperatorTouches.local().execute(geometryA, geometryB, null, null)
    }

    /**
     * 返回作业面积（单位：亩）
     *
     * @param workLength 作业里程
     * @return
     */
    fun calculationWorkArea(workLength: Double): Double {

        return getNavInterval() * workLength * 0.0015
    }

    /**
     * 导航线的垄宽，真实垄宽+重叠/遗漏
     *
     * @return 如果重叠过大，则导航线垄宽即为真实垄宽，忽略重叠设置
     */
    fun getNavInterval(): Double {
        var pinterval: Double = interval + overlapskip.toDouble()
        if (pinterval < 0) {
            pinterval = interval.toDouble()
        }
        return pinterval
    }

    /**
     * 判断点在直线的左边还是右边
     * 以及两点p1(x1,y1),p2(x2,y2),判断点p(x,y)在线的左边还是右边。
     */
    fun LeftOfLine(p: Point, p1: Point, p2: Point): Boolean {
        val tmpx = (p1.x - p2.x) / (p1.y - p2.y) * (p.y - p2.y) + p2.x;
        //当tmpx>p.x的时候，说明点在线的左边，小于在右边，等于则在线上。
        if (tmpx > p.x)
            return true;
        return false;
    }
    fun LeftOfLine(p: UTMRef, p1: UTMRef, p2: UTMRef): Boolean {
        val tmpx = (p1.easting - p2.easting) / (p1.northing - p2.northing) * (p.northing - p2.northing) + p2.easting;
        //当tmpx>p.easting的时候，说明点在线的左边，小于在右边，等于则在线上。
        if (tmpx > p.easting)
            return true;
        return false;
    }
}