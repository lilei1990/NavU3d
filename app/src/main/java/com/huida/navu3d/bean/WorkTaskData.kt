package com.huida.navu3d.bean

import org.litepal.LitePal
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :作业任务数据模型
 */

class WorkTaskData : LitePalSupport() ,Serializable{

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

    //轨迹数据
    var trackLineData: ArrayList<TrackLineData>? =null

    //平行线数据,包含AB点
    var guideLineData: GuideLineData? =null

//    //A点
//    var pointA = ArrayList<PointDb>()
//
//    //B点
//    var pointB = ArrayList<PointDb>()

    fun getObjId(): Long {
        return baseObjId
    }

    /**
     * 查询轨迹线的数据
     */
    fun findTrackLines() {
        var arr =
            LitePal.where("worktaskdata_id=${getObjId()}")
                .find(TrackLineData::class.java, true)
        trackLineData?.clear()
        trackLineData?.addAll(arr)
    }
    /**
     * 查询导航线的数据
     */
    fun findGuideLines() {
         guideLineData =
            LitePal.where("worktaskdata_id=${getObjId()}")
                .findFirst(GuideLineData::class.java, true)
    }
}


