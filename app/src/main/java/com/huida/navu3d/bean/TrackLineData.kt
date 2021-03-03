package com.huida.navu3d.bean

import com.esri.core.geometry.Point
import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者 : lei
 * 时间 : 2021/02/01.
 * 邮箱 :416587959@qq.com
 * 描述 :轨迹线的数据
 */
class TrackLineData : LitePalSupport(), Serializable {
    //时间戳
    var time: Date = Date(System.currentTimeMillis())

    //当前线段的点
    @Column(ignore = true)
    lateinit var points: MutableList<PointData>

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
            LitePal.where("worktaskdata_id=${getId()}")
                .find(PointData::class.java)
    }
}