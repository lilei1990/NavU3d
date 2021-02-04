package com.huida.navu3d.bean

import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.constants.PointType
import org.litepal.LitePal
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
        return pointAB!![0]
    }

    fun setPointA(pointXYData: PointXYData) {
        //如果数据为空就初始化两个初始值
        checkPointAB()
        pointAB!![0].type = PointType.A
        pointAB!![0].X = pointXYData.X
        pointAB!![0].Y = pointXYData.Y
        pointAB!![0].latGC102 = pointXYData.latGC102
        pointAB!![0].lngGC102 = pointXYData.lngGC102
        pointAB!![0].lng = pointXYData.lng
        pointAB!![0].lat = pointXYData.lat
        pointAB!![0].save()
        save()
    }

    /**
     * 获取B点数据
     */
    fun getPointB(): PointXYData? {

        return pointAB!![1]
    }

    fun setPointB(pointXYData: PointXYData) {
        //如果数据为空就初始化两个初始值
        checkPointAB()
        pointAB!![1].type = PointType.B
        pointAB!![1].X = pointXYData.X
        pointAB!![1].Y = pointXYData.Y
        pointAB!![1].latGC102 = pointXYData.latGC102
        pointAB!![1].lngGC102 = pointXYData.lngGC102
        pointAB!![1].lng = pointXYData.lng
        pointAB!![1].lat = pointXYData.lat
        pointAB!![1].save()
        save()
    }

    /**
     * 检查ab点的值 如果数据为空就初始化两个初始值
     */
    private fun checkPointAB() {
        pointAB?.apply {
            if (size == 0) {
                add(PointXYData())
                add(PointXYData())
            }
        }
    }


}


