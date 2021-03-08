package com.huida.navu3d.bean

import android.util.Log
import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable
import java.lang.Exception
import java.util.*

/**
 * 作者 : lei
 * 时间 : 2021/02/01.
 * 邮箱 :416587959@qq.com
 * 描述 :轨迹线的数据
 */
class TrackLineData : LitePalSupport(), Serializable {
    //时间戳
    var time: Date = Date(System.currentTimeMillis())

    //关联workTask数据
    var workTaskId = -1L

    //当前线段的点
    @Column(ignore = true)
    var points = mutableListOf<PointData>()

    fun getId(): Long {
        return super.getBaseObjId()
    }

    /**
     * 查询线的数据
     */
    fun findPoint() {
//        var arr =
//            LitePal.where("worktaskdata_id=${getId()}")
//                .count(PointData::class.java)
//        //如果数据量太大,就分批加载
//        if (arr > 10000) {
//        }
        points =
            LitePal.where("tracklineid=${getId()}")
                .find(PointData::class.java)
    }

    /**
     * 查询线的数据
     */
    fun findFirst(): PointData? {

        return LitePal.where("tracklineid=${getId()}")
            .findFirst(PointData::class.java)
    }

    /**
     * 查询线的数据
     */
    fun findLast(): PointData? {

        return LitePal.where("tracklineid=${getId()}")
            .findLast(PointData::class.java)
    }
}