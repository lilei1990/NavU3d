package com.huida.navu3d.bean

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

    //平行线数据,包含AB点
    var guideLineData: GuideLineData? = null

    //轨迹数据
    var lines: ArrayList<TrackLineData>? = ArrayList()


    fun getObjId(): Long {
        return baseObjId
    }

    /**
     * 查询线的数据
     */
    fun findLines() {
        var arr =
            LitePal.where("worktaskdata_id=${getObjId()}")
                .find(TrackLineData::class.java, true)
        lines?.clear()
        lines?.addAll(arr)
    }
}


