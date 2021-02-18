package com.huida.navu3d.utils

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.CoordinateConverter
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.esri.core.geometry.Point
import com.lei.core.BaseApp

/**
 * 作者 : lei
 * 时间 : 2021/02/18.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object GaoDeUtils {
    /**
     * 移动视角到缩放大小
     */
    fun moveCameraZoomTo(map: AMap, var0: Float) {
        map.moveCamera(CameraUpdateFactory.zoomTo(var0))
    }

    /**
     * 移动视角到经纬度
     * target - 目标位置的屏幕中心点经纬度坐标。
     * zoom - 目标可视区域的缩放级别。 20f最大
     * tilt - 目标可视区域的倾斜度，以角度为单位。
     * bearing - 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度。
     */
    fun moveCameraLatLng(map: AMap, target: LatLng, zoom: Float, tilt: Float, bearing: Float) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(target, zoom, tilt, bearing)))
    }

    /**
     * GPS经纬度转换高德
     */
    fun convertGPS(sourceLatLng: LatLng): LatLng {
        val converter = CoordinateConverter(BaseApp.getContext())
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS)
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(sourceLatLng)
        // 执行转换操作
        return converter.convert()
    }

    /**
     * utm坐标转高德.不要用,失真严重bug
     */
    fun utmToGaoDe(B: Point): LatLng {
        val utmLatlng = GeoConvert.INSTANCE.convertUTMToLatLon(B.x, B.y)

        val latlngGaode =GaoDeUtils.convertGPS( LatLng(utmLatlng[0], utmLatlng[1]))

        return latlngGaode
    }

}