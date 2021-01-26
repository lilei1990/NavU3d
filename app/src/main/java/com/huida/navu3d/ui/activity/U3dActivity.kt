package com.huida.navu3d.ui.activity

import android.content.ComponentCallbacks2
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.amap.api.maps.model.MyLocationStyle
import com.huida.navu3d.R
import com.huida.navu3d.databinding.ActivityU3dBinding
import com.lei.base_core.base.BaseVmActivity
import com.lei.base_core.common.clickNoRepeat
import com.unity3d.player.IUnityPlayerLifecycleEvents
import com.unity3d.player.UnityPlayer


class U3dActivity : BaseVmActivity<ActivityU3dBinding>(ActivityU3dBinding::inflate), IUnityPlayerLifecycleEvents {
    var mUnityPlayer // don't change the name of this variable; referenced from native code
            : UnityPlayer? = null


    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            initU3d()
            initButton()
            initMap(savedInstanceState)
        }
    }


    private fun initButton() {

        var applyConstraintSet = ConstraintSet()
        var applyConstraintReset = ConstraintSet()
        applyConstraintSet.clone(binding.clRoot);
        applyConstraintReset.clone(binding.clRoot);
        var bt1 =
            LayoutInflater.from(this)
                .inflate(R.layout.item_activity_main_button, binding.llMenu, false)
                .apply {

                }

        var bt2 =
            LayoutInflater.from(this)
                .inflate(R.layout.item_activity_main_button, binding.llMenu, false)
                .apply {

                }
        var bt3 =
            LayoutInflater.from(this)
                .inflate(R.layout.item_activity_main_button, binding.llMenu, false)
                .apply {

                }
        var bt4 =
            LayoutInflater.from(this)
                .inflate(R.layout.item_activity_main_button, binding.llMenu, false)
                .apply {
                    setOnClickListener {

                    }
                }
        var bt5 =
            LayoutInflater.from(this)
                .inflate(R.layout.item_activity_main_button, binding.llMenu, false)
                .apply {
                    setOnClickListener {

                    }
                }
        var bt6 =
            LayoutInflater.from(this)
                .inflate(R.layout.item_activity_main_button, binding.llMenu, false)
                .apply {
                    setOnClickListener {

                    }
                }
        binding.llMenu.addView(bt1)
        binding.llMenu.addView(bt2)
        binding.llMenu.addView(bt3)
        binding.llMenu.addView(bt4)
        binding.llMenu.addView(bt5)
        binding.llMenu.addView(bt6)
    }


    fun initMap(savedInstanceState: Bundle?) {
        val myLocationStyle: MyLocationStyle =
            MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        binding.map.map.uiSettings.isZoomControlsEnabled = false//不显示右下角放大缩小按钮
        binding.map.map.uiSettings.isZoomGesturesEnabled = false
        binding.map.map.myLocationStyle = myLocationStyle //设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        binding.map.map.isMyLocationEnabled =
            true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        binding.map.onCreate(savedInstanceState);

    }

    fun initU3d() {
        val cmdLine =
            updateUnityCommandLineArguments(intent.getStringExtra("unity"))

        intent.putExtra("unity", cmdLine)
        mUnityPlayer = UnityPlayer(this, this)
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT
        )
        mUnityPlayer!!.layoutParams = lp
        val bottomView = findViewById<LinearLayout>(R.id.llBottom)
        val topView = findViewById<LinearLayout>(R.id.llTop)
        bottomView.addView(mUnityPlayer)
        mUnityPlayer!!.requestFocus()


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
        binding.map.onSaveInstanceState(outState);
    }

    private fun updateUnityCommandLineArguments(cmdLine: String?): String? {
        return cmdLine
    }

    // When Unity player unloaded move task to background
    override fun onUnityPlayerUnloaded() {
        moveTaskToBack(true)
    }

    // Callback before Unity player process is killed
    override fun onUnityPlayerQuitted() {}

    /**
     * 用于更新意图
     */
    override fun onNewIntent(intent: Intent?) {
        setIntent(intent)
        mUnityPlayer!!.newIntent(intent)
    }

    // Quit Unity
    override fun onDestroy() {
        mUnityPlayer!!.destroy()
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.map.onDestroy();
    }

    // Pause Unity
    override fun onPause() {
        super.onPause()
        mUnityPlayer!!.pause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.map.onPause();
    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        mUnityPlayer!!.resume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.map.onResume();
    }

    // Low Memory Unity
    override fun onLowMemory() {
        super.onLowMemory()
        mUnityPlayer!!.lowMemory()
    }

    // Trim Memory Unity
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer!!.lowMemory()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mUnityPlayer!!.configurationChanged(newConfig)
    }

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        mUnityPlayer!!.windowFocusChanged(hasFocus)
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_MULTIPLE) mUnityPlayer!!.injectEvent(
            event
        ) else super.dispatchKeyEvent(event)
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    /*API12*/
    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }
}