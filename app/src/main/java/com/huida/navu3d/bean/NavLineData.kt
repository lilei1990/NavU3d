package com.huida.navu3d.bean

import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorFactoryLocal
import com.esri.core.geometry.OperatorOffset
import com.esri.core.geometry.Polyline
import com.huida.navu3d.ui.activity.u3d.ParallelLine
import org.litepal.crud.LitePalSupport
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者 : lei
 * 时间 : 2021/02/17.
 * 邮箱 :416587959@qq.com
 * 描述 : 导航线
 */
class NavLineData : LitePalSupport() {
    //索引,所在的位置
    var index: Int = 3
    var startX: Double = 0.0
    var startY: Double = 0.0
    var endX: Double = 0.0
    var endY: Double = 0.0

    //平行线的间隔
    val lineOffset = 10

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
            mapOf.put(i+index , polyline)
        }
        return mapOf
    }


}