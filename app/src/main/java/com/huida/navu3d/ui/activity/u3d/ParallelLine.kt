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
     * 创建地图用的数据,平行线,
     *
     */
    fun budileMapLine(pointData: MutableList<PointXYData>): MutableList<Polyline> {

        val mParallelLine: MutableList<Polyline> = ArrayList()
        if (pointData.size < 1) return mParallelLine

        val line = Polyline()
        for ((index, e) in pointData.withIndex()) {
            if (index == 0) {
                line.startPath(pointData[index].latGC102, pointData[index].lngGC102)
            }
            line.lineTo(pointData[index].latGC102, pointData[index].lngGC102)
        }

        for (i in -5..5) {
            var polyline = offseter.execute(
                line,
                null,
                0.000500 * i.toDouble(),
                OperatorOffset.JoinType.Round,
                0.0,
                180.0,
                null
            ) as Polyline


            var polyline1 = offseter.execute(
                line,
                null,
                -0.000500 * i.toDouble(),
                OperatorOffset.JoinType.Round,
                0.0,
                180.0,
                null
            ) as Polyline

            mParallelLine.add(polyline)
            mParallelLine.add(polyline1)

        }
        return mParallelLine
    }

    /**
     * 创建Utm用的数据,平行线,
     * 必要用转换工具来回转换,数据精度失真
     */
    fun budileUtmLine(pointData: MutableList<PointXYData>): MutableList<Polyline> {

        val mParallelLine: MutableList<Polyline> = ArrayList()
        if (pointData.size < 1) return mParallelLine

        val line = Polyline()
        for ((index, e) in pointData.withIndex()) {
            if (index == 0) {
                line.startPath(pointData[index].X, pointData[index].Y)
            }
            line.lineTo(pointData[index].X, pointData[index].Y)
        }

        for (i in -5..5) {
            var polyline = offseter.execute(
                line,
                null,
                lineOffset * i.toDouble(),
                OperatorOffset.JoinType.Round,
                0.0,
                180.0,
                null
            ) as Polyline
//            for (i in 0 until polyline.pointCount) {
//                val point = polyline.getPoint(i)
//            }

            var polyline1 = offseter.execute(
                line,
                null,
                -lineOffset * i.toDouble(),
                OperatorOffset.JoinType.Round,
                0.0,
                180.0,
                null
            ) as Polyline

            mParallelLine.add(polyline)
            mParallelLine.add(polyline1)

        }
        return mParallelLine
    }

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
        val latlngGaode =
            convertGaoDe(B)

        B.latGC102 = latlngGaode.latitude
        B.lngGC102 = latlngGaode.longitude
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