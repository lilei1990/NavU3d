package com.huida.navu3d.ui.activity.u3d

import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.observe
import com.amap.api.maps.AMap
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.ScaleAnimation
import com.amap.api.maps.utils.overlay.SmoothMoveMarker
import com.huida.navu3d.R
import com.huida.navu3d.databinding.ActivityU3dBinding
import com.lei.base_core.common.clickNoRepeat
import com.unity3d.player.IUnityPlayerLifecycleEvents


class U3dActivity : U3d<ActivityU3dBinding>(ActivityU3dBinding::inflate),
    IUnityPlayerLifecycleEvents {

    val viewModel by lazy { getActivityViewModel(U3dViewModel::class.java) }
    val mapViewModel by lazy { getActivityViewModel(MapViewModel::class.java)!! }

    override fun init(savedInstanceState: Bundle?) {



        binding.apply {
            initU3dLayout()
            initButton()
            initMap(savedInstanceState)

        }

    }


    private fun initButton() {
        binding.bt1.tvText.text = "设置A点"
        binding.bt1.itemRoot.clickNoRepeat {
            mapViewModel!!.setA()
        }
        binding.bt2.tvText.text = "设置B点"
        binding.bt2.itemRoot.clickNoRepeat {
            mapViewModel!!.setB()
            mapViewModel.setLine(binding.gdMap.map)
        }
        binding.bt3.tvText.text = "开始"
        binding.bt3.itemRoot.clickNoRepeat {
            viewModel!!.start()
        }
        binding.bt4.tvText.text = "暂停"
        binding.bt4.itemRoot.clickNoRepeat {
            viewModel!!.pause()
        }
        binding.bt4.tvText.text = "停止"
        binding.bt4.itemRoot.clickNoRepeat {
            viewModel!!.stop()
        }
        binding.bt.clickNoRepeat {
            mapViewModel.getMarker(this)
            mapViewModel.getLocationStyle()
            mapViewModel.getCarMarker(binding.gdMap.map)
        }

    }


    fun initMap(savedInstanceState: Bundle?) {
        mapViewModel.mLocationStyle.observe(this){
            binding.gdMap.map.myLocationStyle = it //设置定位蓝点的Style
        }
        mapViewModel.mMarker.observe(this){
            val markerAnimation: Animation = ScaleAnimation(0f, 1f, 0f, 1f) //初始化生长效果动画
            markerAnimation.setDuration(1000) //设置动画时间 单位毫秒
            val marker = binding.gdMap.map.addMarker(it)
            marker.setAnimation(markerAnimation)
            marker.startAnimation();

        }

        binding.gdMap.map.uiSettings.isMyLocationButtonEnabled = true;//设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        binding.gdMap.map.isMyLocationEnabled = true
        binding.gdMap.map.mapType = AMap.MAP_TYPE_SATELLITE
        binding.gdMap.onCreate(savedInstanceState);
//        mapViewModel.startLoaction(this)

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