package com.huida.navu3d.ui.fragment.home

import com.huida.navu3d.bean.LineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.WorkTaskData

/**
 * 作者 : lei
 * 时间 : 2021/02/22.
 * 邮箱 :416587959@qq.com
 * 描述 : 线段数据管理处理
 */

class LineDbManage {
    //每次点start的时候就代表一个新的线段
    val lineXYData = LineData()
    /**
     * 数据库存储
     */
    fun saveDb() {
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
        taskWorkby?.LineXYDatas?.add(lineXYData)
        lineXYData.save()
        taskWorkby?.save()
        return this
    }

    /**
     * 存储数据
     */
    fun savePoint(pointXY: PointData) {
        lineXYData.points.add(pointXY)
        saveDb()
    }
}