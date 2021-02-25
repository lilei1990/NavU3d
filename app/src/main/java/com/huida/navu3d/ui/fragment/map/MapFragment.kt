package com.huida.navu3d.ui.fragment.map

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.observe
import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.ScaleAnimation
import com.huida.navu3d.databinding.FragmentMapBinding
import com.huida.navu3d.ui.fragment.home.HomeVM
import com.huida.navu3d.utils.GaoDeUtils
import com.lei.core.base.BaseVmFragment

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class MapFragment : BaseVmFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {
    private val mapViewModel by lazy { getFragmentViewModel(MapVM::class.java)!! }
    private val homeViewModel by lazy { getFragmentViewModel(HomeVM::class.java)!! }
    val gdMap by lazy { binding.gdMap}
    companion object {
        fun newInstance() = MapFragment()
    }



    override fun init(savedInstanceState: Bundle?) {

        binding.apply {
            initMap(savedInstanceState)
        }
    }

    override fun observe() {
        //A点
        homeViewModel.DataPointA.observe(this) {
            mapViewModel.DataMarkerA?:mapViewModel.DataMarkerA?.remove()
            val markPoint = mapViewModel.markPoint(it)
            GaoDeUtils.moveCameraLatLng(binding.gdMap.map, LatLng(markPoint.position.latitude, markPoint.position.longitude), 20f, 0f, 30f)
            val markerAnimation: Animation = ScaleAnimation(0f, 1f, 0f, 1f) //初始化生长效果动画
            markerAnimation.setDuration(1000) //设置动画时间 单位毫秒
            mapViewModel.DataMarkerA = binding.gdMap.map.addMarker(markPoint)
            mapViewModel.DataMarkerA!!.setAnimation(markerAnimation)
            mapViewModel.DataMarkerA!!.startAnimation();
        }
        //B点
        homeViewModel.DataPointB.observe(this) {
            mapViewModel.DataMarkerB?:mapViewModel.DataMarkerB?.remove()
            val markPoint = mapViewModel.markPoint(it)
            GaoDeUtils.moveCameraLatLng(binding.gdMap.map, LatLng(markPoint.position.latitude, markPoint.position.longitude), 20f, 0f, 30f)
            val markerAnimation: Animation = ScaleAnimation(0f, 1f, 0f, 1f) //初始化生长效果动画
            markerAnimation.setDuration(1000) //设置动画时间 单位毫秒
            mapViewModel.DataMarkerB = binding.gdMap.map.addMarker(markPoint)
            mapViewModel.DataMarkerB!!.setAnimation(markerAnimation)
            mapViewModel.DataMarkerB!!.startAnimation();
        }
        var addPolyline: Polyline? = null
        //轨迹线
        homeViewModel.DataPointXY.observe(this) {
//            addPolyline?.apply {
//                addPolyline?.remove()
//            }
//            val latLngs = ArrayList<LatLng>()
//            for (pointXY in it) {
//                latLngs.add(GaoDeUtils.convertGPS(LatLng(pointXY.lat, pointXY.lng)))
//            }
//            PolylineOptions().add()
//            addPolyline = binding.gdMap.map.addPolyline(
//                    PolylineOptions().addAll(latLngs).width(5f).color(Color.argb(255, 1, 255, 1))
//            )


        }
        homeViewModel.DataParallelLine.observe(this) {
            for ((key, polyline) in it) {
                val latLngs: MutableList<LatLng> = ArrayList()

                for (i in 0 until polyline.pointCount) {
                    val point = polyline.getPoint(i)
                    latLngs.add(GaoDeUtils.utmToGaoDe(point))
                }

                binding.gdMap.map.addPolyline(
                        PolylineOptions().addAll(latLngs).width(5f).color(Color.argb(255, 1, 255, 1))
                )
                val textOptions = TextOptions()
                textOptions.position(latLngs[0])
                textOptions.text("编号:${key}")
                binding.gdMap.map.addText(textOptions)
            }
        }
    }
    fun initMap(savedInstanceState: Bundle?) {
        binding.gdMap.map.myLocationStyle = initLocationStyle() //设置定位蓝点的Style
        //设置卫星地图
        binding.gdMap.map.mapType = AMap.MAP_TYPE_SATELLITE
        binding.gdMap.onCreate(savedInstanceState);
//开启定位
//        mapViewModel.startLoaction(this)

    }
    /**
     * 设置定位蓝点的Style
     */
    fun initLocationStyle(): MyLocationStyle {
        val myLocationStyle: MyLocationStyle =
                MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//            myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(false)
//        binding.map.map.uiSettings.isZoomControlsEnabled = false//禁用放大缩小
//        binding.map.map.uiSettings.isZoomGesturesEnabled = false
        return myLocationStyle
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.gdMap.onSaveInstanceState(outState);
    }

    // Quit Unity
    override fun onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        gdMap.onDestroy();
        super.onDestroy()
    }

    // Pause Unity
    override fun onPause() {
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        gdMap.onPause();
        super.onPause()
    }

    // Resume Unity
    override fun onResume() {
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        gdMap.onResume();
        super.onResume()
    }

}