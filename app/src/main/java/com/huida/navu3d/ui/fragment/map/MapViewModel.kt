package com.huida.navu3d.ui.fragment.map

import androidx.lifecycle.ViewModel
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.huida.navu3d.bean.PointXYData
import com.huida.navu3d.utils.GaoDeUtils


class MapViewModel : ViewModel() {


    //A点坐标
    var DataMarkerA: Marker? = null

    //B点坐标
    var DataMarkerB:Marker?  = null







     fun markPoint(point: PointXYData): MarkerOptions {
        val options = MarkerOptions()
        options.draggable(true)
                .snippet("DefaultMarker")
        options.position(GaoDeUtils.convertGPS(LatLng(point.lat, point.lng)))
        options.isFlat = true
        return options
    }




}