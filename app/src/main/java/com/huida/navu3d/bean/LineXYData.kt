package com.huida.navu3d.bean

import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/02/01.
 * 邮箱 :416587959@qq.com
 * 描述 :轨迹线的数据
 */
class LineXYData: LitePalSupport() {
    var points = ArrayList<PointXYData>()
}