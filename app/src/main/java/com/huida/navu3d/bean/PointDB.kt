package com.huida.navu3d.bean

import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/03/05.
 * 邮箱 :416587959@qq.com
 * 描述 : 点的数据模型
 */
class PointDB : LitePalSupport() {
    var lat = 0.0
    var lng = 0.0
    //线的标识
    var trackLineId = -1L
}