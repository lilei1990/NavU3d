package com.huida.navu3d.ui.activity.u3d

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.observe
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.ScaleAnimation
import com.blankj.utilcode.util.LogUtils
import com.huida.navu3d.R
import com.huida.navu3d.bean.CurrentWorkTask
import com.huida.navu3d.databinding.ActivityU3dBinding
import com.huida.navu3d.ui.activity.DomeManager
import com.kongqw.rockerlibrary.view.RockerView
import com.kongqw.rockerlibrary.view.RockerView.OnShakeListener
import com.lei.base_core.common.clickNoRepeat
import com.unity3d.player.IUnityPlayerLifecycleEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class U3dActivity : U3d<ActivityU3dBinding>(ActivityU3dBinding::inflate),
    IUnityPlayerLifecycleEvents {
    val viewModel by lazy { getActivityViewModel(U3dViewModel::class.java)!! }
    val mapViewModel by lazy { getActivityViewModel(MapViewModel::class.java)!! }

    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            initU3dLayout()
            initButton()
            initMap(savedInstanceState)
            initRockView()
        }

    }

    override fun observe() {

        viewModel.DataOffsetLineDistance.observe(this) {
            binding.incTopBar.tvLength.text = "${it}"
        }
        viewModel.DataSatelliteCount.observe(this) {
            binding.incTopBar.tvStatellite.text = it
        }
        viewModel.DataSteerAngle.observe(this) {
            binding.incTopBar.tvRtk.text = it
        }
        viewModel.DataSteerAngle.observe(this) {
            binding.incTopBar.tvRtk.text = it
        }
        viewModel.DataSpeed.observe(this) {
            binding.incTopBar.tvSpeed.text = it
        }
        mapViewModel.mLocationStyle.observe(this) {
            binding.gdMap.map.myLocationStyle = it //设置定位蓝点的Style
        }
        viewModel.DataMarkerA.observe(this) {
            val cameraPosition =
                CameraPosition(LatLng(it.position.latitude, it.position.longitude), 15f, 0f, 30f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            binding.gdMap.map.moveCamera(cameraUpdate)
            val markerAnimation: Animation = ScaleAnimation(0f, 1f, 0f, 1f) //初始化生长效果动画
            markerAnimation.setDuration(1000) //设置动画时间 单位毫秒
            val marker = binding.gdMap.map.addMarker(it)
            marker.setAnimation(markerAnimation)
            marker.startAnimation();

        }
        viewModel.DataMarkerB.observe(this) {

            val markerAnimation: Animation = ScaleAnimation(0f, 1f, 0f, 1f) //初始化生长效果动画
            markerAnimation.setDuration(1000) //设置动画时间 单位毫秒
            val marker = binding.gdMap.map.addMarker(it)
            marker.setAnimation(markerAnimation)
            marker.startAnimation();

        }
        var addPolyline: Polyline? = null
        viewModel.DataPointXY.observe(this) {
            addPolyline?.apply {
                addPolyline?.remove()
            }
            val latLngs = ArrayList<LatLng>()
            for (pointXY in it) {
                latLngs.add(LatLng(pointXY.latGC102, pointXY.lngGC102))
            }
            addPolyline = binding.gdMap.map.addPolyline(
                PolylineOptions().addAll(latLngs).width(5f).color(Color.argb(255, 1, 255, 1))
            )


        }

        viewModel.DataParallelLine.observe(this) {
            for (polyline in it) {
                val latLngs: MutableList<LatLng> = ArrayList()
                for (i in 0 until polyline.pointCount) {
                    val point = polyline.getPoint(i)
                    latLngs.add(ParallelLine.utmToGaoDe(point))
                }

                binding.gdMap.map.addPolyline(
                    PolylineOptions().addAll(latLngs).width(5f).color(Color.argb(255, 1, 255, 1))
                )
            }
        }

    }

    private fun initRockView() {
        binding.rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
        binding.rockerView.setOnShakeListener(
            RockerView.DirectionMode.DIRECTION_8,
            object : OnShakeListener {
                override fun onStart() {

                }

                override fun direction(direction: RockerView.Direction) {
                    LogUtils.e("摇动方向 : $direction")
                    when (direction) {
                        RockerView.Direction.DIRECTION_LEFT -> {
                            DomeManager.setAngle(-5.0)
                        }
                        RockerView.Direction.DIRECTION_RIGHT -> {
                            DomeManager.setAngle(5.0)
                        }
                        RockerView.Direction.DIRECTION_UP -> {
                            DomeManager.setSpeedDistance(0.005)
                        }
                        RockerView.Direction.DIRECTION_DOWN -> {
                            DomeManager.setSpeedDistance(-0.005)
                        }
                        RockerView.Direction.DIRECTION_UP_LEFT -> {
                            DomeManager.setAngle(-5.0)
                            DomeManager.setSpeedDistance(0.005)
                        }
                        RockerView.Direction.DIRECTION_UP_RIGHT -> {
                            DomeManager.setAngle(5.0)
                            DomeManager.setSpeedDistance(0.005)
                        }
                        RockerView.Direction.DIRECTION_DOWN_LEFT -> {
                            DomeManager.setAngle(-5.0)
                            DomeManager.setSpeedDistance(-0.005)
                        }
                        RockerView.Direction.DIRECTION_DOWN_RIGHT -> {
                            DomeManager.setAngle(5.0)
                            DomeManager.setSpeedDistance(-0.005)
                        }

                    }
                }

                override fun onFinish() {

                }
            })
    }

    private fun initButton() {
        binding.bt1.tvText.text = "设置A点"
        binding.bt1.itemRoot.clickNoRepeat {
            viewModel!!.setPointA()
        }
        binding.bt2.tvText.text = "设置B点"
        binding.bt2.itemRoot.clickNoRepeat {
            viewModel!!.setPointB()
        }
        binding.bt3.tvText.text = "生成导航线"
        binding.bt3.itemRoot.clickNoRepeat {
            viewModel!!.DrawMapParallelLine()
        }
        binding.bt4.tvText.text = "刷新线"
        binding.bt4.itemRoot.clickNoRepeat {
            viewModel!!.DrawTrack(binding.gdMap.map)
        }

        binding.bt5.tvText.text = "开始"
        binding.bt5.itemRoot.clickNoRepeat {
            viewModel!!.start(this)
        }
        binding.bt6.tvText.text = "暂停"
        binding.bt6.itemRoot.clickNoRepeat {
            viewModel!!.stop()
        }
        binding.bt7.tvText.text = "停止"
        binding.bt7.itemRoot.clickNoRepeat {
            viewModel!!.stop()
        }
        binding.bt.clickNoRepeat {
            mapViewModel.getMarker(this)
            mapViewModel.getCarMarker(binding.gdMap.map)
        }

    }


    fun initMap(savedInstanceState: Bundle?) {
        mapViewModel.initLocationStyle()



        binding.gdMap.map.uiSettings.isMyLocationButtonEnabled = true;//设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        binding.gdMap.map.isMyLocationEnabled = true
        binding.gdMap.map.mapType = AMap.MAP_TYPE_SATELLITE
        binding.gdMap.onCreate(savedInstanceState);
        //开启定位
        mapViewModel.startLoaction(this)

    }

    private fun initU3dLayout() {
        val bottomView = findViewById<LinearLayout>(R.id.llBottom)
        val topView = findViewById<LinearLayout>(R.id.llTop)
        bottomView.addView(mUnityPlayer)
//        mUnityPlayer!!.requestFocus()


        //切换地图和u3d场景
        binding.btSwitch.clickNoRepeat {
            val progress = binding.mlRoot.progress
            if (progress == 0.0f) {
                binding.mlRoot.transitionToEnd()
                binding.mlRoot.bringChildToFront(bottomView)
            }
            if (progress == 1.0f) {
                binding.mlRoot.transitionToStart()
                binding.mlRoot.bringChildToFront(topView)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.gdMap.onSaveInstanceState(outState);
    }

    // Quit Unity
    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.gdMap.onDestroy();
    }

    // Pause Unity
    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.gdMap.onPause();
    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.gdMap.onResume();
    }


}