package com.huida.navu3d.ui.fragment.home

import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorFactoryLocal
import com.esri.core.geometry.OperatorGeneralize
import com.huida.navu3d.bean.TrackLineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.WorkTaskData
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread

/**
 * 作者 : lei
 * 时间 : 2021/02/22.
 * 邮箱 :416587959@qq.com
 * 描述 : 线段数据管理处理
 */

class LineDbManage {
    //每次点start的时候就代表一个新的线段
    val lineXYData = TrackLineData()
    val mLineXYData = TrackLineData()
    //点的队列
    val mPointQueue=ConcurrentLinkedDeque<PointData>()
    //抽稀操作
    val generalizer =
        OperatorFactoryLocal
            .getInstance()
            .getOperator(Operator.Type.Generalize) as OperatorGeneralize
    /**
     * 数据库存储
     */
    fun saveDb() {

//        //1 传入点抽稀
//        outputGeom = generalizer.execute(
//            outputGeom,
//            0.03 * scale,
//            true,
//            null
//        ) as Polyline

        //数据每一百个点存储一次,并且抽稀操作
        if (lineXYData.points.size % 10 == 0) {
            for (point in lineXYData.points) {
                point.save()
            }
            lineXYData.save()
        }
    }

    /**
     * 关联数据,并存储
     */
    fun build(taskWorkby: WorkTaskData?): LineDbManage {
        taskWorkby?.trackLineData?.add(lineXYData)
        lineXYData.save()
        taskWorkby?.save()
        return this
    }

    /**
     * 存储数据
     */
    fun savePoint(pointXY: PointData) {
        lineXYData.points.add(pointXY)
        pointXY.save()
        lineXYData.save()
    }
    fun sendPoint(pointXY: PointData) {
        mPointQueue.add(pointXY)
    }
    fun saveP() {
        thread {
            fixedRateTimer(period = 1000){
                while (true) {
                    val point = mPointQueue.poll()
                    point?: break
                    mLineXYData.points.add(point)
                    point.save()
                }
            }
        }.run()
    }
}