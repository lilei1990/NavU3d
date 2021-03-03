package com.huida.navu3d.bean

import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorFactoryLocal
import com.esri.core.geometry.OperatorOffset
import com.esri.core.geometry.Polyline
import com.huida.navu3d.constants.Constants.lineOffset
import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/02/17.
 * 邮箱 :416587959@qq.com
 * 描述 : 导航线
 */
class GuideLineData : LitePalSupport() {
    //索引,所在的位置
    var index: Int = 0
    var startX: Double = 0.0
    var startY: Double = 0.0
    var startLat = 0.0
    var startLng = 0.0
    var endX: Double = 0.0
    var endY: Double = 0.0
    var endLat = 0.0
    var endLng = 0.0



    /**
     * 创建Utm用的数据,平行线,
     */
    fun budileUtmLine(): MutableMap<Int, Polyline> {
        //偏移操作
        val offseter = OperatorFactoryLocal
                .getInstance()
                .getOperator(Operator.Type.Offset) as OperatorOffset
        val line = Polyline()
        line.startPath(startX, startY)
        line.lineTo(endX, endY)
        val mapOf = mutableMapOf<Int, Polyline>()
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
            //编号和线的坐标
            mapOf.put(i + index, polyline)
        }


        return mapOf
    }

    /**
     * 设置开始点
     */
    fun setStart(mA: PointData) {
        startX = mA.x
        startY = mA.y
        startLat = mA.lat
        startLng = mA.lng

    }

    fun getStart(): PointData {
        return  PointData.build(startLat,startLng)
    }

    /**
     * 设置结束点
     */
    fun setEnd(mB: PointData) {
        endX = mB.x
        endY = mB.y
        endLat = mB.lat
        endLng = mB.lng
    }

    fun getEnd(): PointData {

        return  PointData.build(endLat,endLng)
    }
}