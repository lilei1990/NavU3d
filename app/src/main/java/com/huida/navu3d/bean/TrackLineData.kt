package com.huida.navu3d.bean

import org.litepal.crud.LitePalSupport
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者 : lei
 * 时间 : 2021/02/01.
 * 邮箱 :416587959@qq.com
 * 描述 :轨迹线的数据
 */
class TrackLineData : LitePalSupport() {
    //时间戳
    var time: Date= Date(System.currentTimeMillis())

    //当前线段的点
    var points = ArrayList<PointData>()

     fun getId(): Long {
        return super.getBaseObjId()
    }
}