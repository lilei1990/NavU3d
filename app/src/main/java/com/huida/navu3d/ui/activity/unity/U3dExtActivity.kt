package com.huida.navu3d.ui.activity.unity

import android.content.ComponentCallbacks2
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lei.core.base.BaseVmActivity
import com.unity3d.player.IUnityPlayerLifecycleEvents
import com.unity3d.player.UnityPlayer

/**
 * 作者 : lei
 * 时间 : 2021/01/27.
 * 邮箱 :416587959@qq.com
 * 描述 :这一层只处理u3d的逻辑 抽取,
 */

abstract class U3dExtActivity<VB : ViewBinding>(inflate: (LayoutInflater) -> VB) : BaseVmActivity<VB>(inflate),
    IUnityPlayerLifecycleEvents {
    val u3dViewModel by lazy { getActivityViewModel(U3dViewModel::class.java)!! }
    // don't change the name of this variable; referenced from native code

    override fun onCreate(savedInstanceState: Bundle?) {
        initUnityPlayer()
        super.onCreate(savedInstanceState)
    }

    private fun initUnityPlayer() {
        val cmdLine =
            updateUnityCommandLineArguments(intent.getStringExtra("unity"))
        intent.putExtra("unity", cmdLine)
        // don't change the name of this variable; referenced from native code

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT
        )

        u3dViewModel.mUnityPlayer= UnityPlayer(this, this)
        u3dViewModel.mUnityPlayer!!.layoutParams = lp
        u3dViewModel.mUnityPlayer!!.requestFocus()

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
        u3dViewModel.mUnityPlayer!!.newIntent(intent)
    }

    // Quit Unity
    override fun onDestroy() {
        u3dViewModel.mUnityPlayer!!.destroy()
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        binding.map.onDestroy();
    }

    // Pause Unity
    override fun onPause() {
        super.onPause()
        u3dViewModel.mUnityPlayer!!.pause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        binding.map.onPause();
    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        u3dViewModel.mUnityPlayer!!.resume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
//        binding.map.onResume();
    }

    // Low Memory Unity
    override fun onLowMemory() {
        super.onLowMemory()
        u3dViewModel.mUnityPlayer!!.lowMemory()
    }

    // Trim Memory Unity
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            u3dViewModel.mUnityPlayer!!.lowMemory()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        u3dViewModel.mUnityPlayer!!.configurationChanged(newConfig)
    }

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        u3dViewModel.mUnityPlayer!!.windowFocusChanged(hasFocus)
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_MULTIPLE)  u3dViewModel.mUnityPlayer!!.injectEvent(
            event
        ) else super.dispatchKeyEvent(event)
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return  u3dViewModel.mUnityPlayer!!.injectEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return  u3dViewModel.mUnityPlayer!!.injectEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return  u3dViewModel.mUnityPlayer!!.injectEvent(event)
    }

    /*API12*/
    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return  u3dViewModel.mUnityPlayer!!.injectEvent(event)
    }

}