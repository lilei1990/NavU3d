package com.huida.navu3d.bean

import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/02/01.
 * 邮箱 :416587959@qq.com
 * 描述 :轨迹线的数据
 */
class LineData : LitePalSupport() {
    var startX: Double = 0.0
    var startY: Double = 0.0
    var startLat: Double = 0.0
    var startLng: Double = 0.0
    var points = ArrayList<PointData>()
    override fun save(): Boolean {
        //把第一个点作为起始点,方便查询操作
        if (points.size > 0) {
            val point = points.get(0)
            startX = point.X
            startY = point.Y
            startLat = point.lat
            startLng = point.lng
        }
        return super.save()
    }
}