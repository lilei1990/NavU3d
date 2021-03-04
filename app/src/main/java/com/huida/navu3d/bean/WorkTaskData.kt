package com.huida.navu3d.bean

import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :作业任务数据模型
 */

class WorkTaskData : LitePalSupport(), Serializable {
    //时间戳
    var time: Date = Date(System.currentTimeMillis())

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
    @Column(ignore = true)
    var trackLineData: ArrayList<TrackLineData>? = ArrayList()

    //平行线数据,包含AB点
    var guideLineData: GuideLineData? = GuideLineData()


    fun getId(): Long {
        return baseObjId
    }

    /**
     * 查询轨迹线的数据
     */
    fun findTrackLines() {
        var arr =
            LitePal.where("worktaskId=${getId()}")
                .find(TrackLineData::class.java, true)
        arr?.apply {
            trackLineData?.clear()
            trackLineData?.addAll(arr)
        }
    }

    /**
     * 查询导航线的数据
     */
    fun findGuideLines() {
        val aaa =
            LitePal.where("worktaskdata_id=${getId()}")
                .findFirst(GuideLineData::class.java, true)
        aaa?.apply {
            guideLineData = aaa
        }
    }

    override fun toString(): String {
        return "WorkTaskData(sortId=$sortId, date=$date, name='$name', creator='$creator', area=$area, farmTools=$farmTools, trackLineData=$trackLineData, guideLineData=$guideLineData)"
    }

}


