package com.huida.navu3d.bean

import com.blankj.utilcode.util.ToastUtils
import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :作业任务数据模型
 */

class WorkTaskData : LitePalSupport() {

    var sortId: Int = 0

    //创建时间
    var date: Long = System.currentTimeMillis()

    //地块名称
    var name: String = ""

    //创建者
    var creator: String = ""

    //作业类型
    var area: Int = 0

    //农具
    var farmTools: Int = 0

    //ab点
    var pointAB: ArrayList<PointXYData>? = ArrayList()


    //参考线
    //历史轨迹
    //标记,引导标记
    /**
     * 获取A点数据
     */
    fun getPointA(): PointXYData? {
        if (pointAB?.size == 0) {
            return null
        }
        return pointAB!![0]
    }

    fun setPointA(pointXYData: PointXYData) {
        if (pointAB?.size == 0) {
            pointAB!!.add(pointXYData)
        } else {
            pointAB!![0] = pointXYData
        }
    }

    /**
     * 获取B点数据
     */
    fun getPointB(): PointXYData? {
        if (pointAB?.size != 2) {
            return null
        }
        return pointAB!![1]
    }

    fun setPointB(pointXYData: PointXYData) {
        if (pointAB?.size == 0) {
            ToastUtils.showLong("请先添加A点")
            return
        }

        if (pointAB?.size == 2) {
            pointAB!![1] = pointXYData
        } else if (pointAB?.size == 1) {
            pointAB!!.add(pointXYData)
        }
    }
}
