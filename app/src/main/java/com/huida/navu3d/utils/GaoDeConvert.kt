package com.huida.navu3d.utils

import android.content.Context
import com.amap.api.maps.CoordinateConverter
import com.amap.api.maps.model.LatLng
import com.lei.core.BaseApp

/**
 * 作者 : lei
 * 时间 : 2021/02/18.
 * 邮箱 :416587959@qq.com
 * 描述 :转换高德经纬度
 */
object GaoDeConvert {
    fun convert(sourceLatLng: LatLng): LatLng {
        val converter = CoordinateConverter(BaseApp.getContext())
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS)
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(sourceLatLng)
        // 执行转换操作
        return converter.convert()
    }
}