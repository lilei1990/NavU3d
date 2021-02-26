package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.unity3d.player.UnityPlayer
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONObject

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :Unity fragment具体实现类
 */
class UnityRepo(
    val viewModelScope: CoroutineScope,
    val errorLiveData: MutableLiveData<ApiException>,
    homeVM: HomeVM
) {

    /**
     * 设置标记点
     */
    fun addPoint(s: String) {
        UnityPlayer.UnitySendMessage("Correspondent", "SetSignBrand", s)
    }

    /**
     * 添加导航线
     */
//    val scale = 100.0
//    val scaleY = 10000000
    fun addParallelLine(it: MutableMap<Int, Polyline>) {
        val lines = JSONArray()
        for ((key, value) in it) {
            val line = JSONObject()
            val startPoint = JSONObject()
            val endPoint = JSONObject()
            var x0: Double = (value.getPoint(0).x)
            var y0: Double = (value.getPoint(0).y)
            var x1: Double = (value.getPoint(1).x)
            var y1: Double = (value.getPoint(1).y)
            startPoint.put("x", x0)
            startPoint.put("y", y0)
            endPoint.put("x", x1)
            endPoint.put("y", y1)
            line.put("nav", JSONArray().put(startPoint).put(endPoint))
            line.put("name", "line1")
            lines.put(line)
        }
        UnityPlayer.UnitySendMessage("Correspondent", "BuildNavLine", lines.toString())

    }

    /**
     * 小车移动
     */
    val scaleX=100000
    val scaleY=1000000
    fun moveCart(
        it: PointData,
        speed: Double
    ) {
        var x = (it.X).toFloat()%scaleX
        var y = (it.Y).toFloat()%scaleY
        Log.d("TAG_lilei", "moveCart: ${x}--${y}")
        val json = JSONObject()
        json.put("x", x)
        json.put("y", y)
        //这个速度知识插值器,当数据刷新频率达到一定程度就影响不到实际效果
        json.put("moveSpeed", speed)
        UnityPlayer.UnitySendMessage("Correspondent", "SendVData", json.toString())
    }

    /**
     * 小车姿态
     */
    fun cartStance(
        steerAngle: Double
    ) {
        Log.d("TAG_lilei", "cartStance: ${steerAngle}")
        val json = JSONObject()
        json.put("yaw", steerAngle)
        json.put("rotationSpeed", 4.0)
        UnityPlayer.UnitySendMessage("Correspondent", "SendRData", json.toString())
    }

    /**
     * 是否显示轨迹
     */
    fun isShowTrack(isShow: Boolean, @Nullable color: String) {
        val json = JSONObject()
        json.put("isShowTrack", isShow)
        json.put("colorStr", color)
        UnityPlayer.UnitySendMessage("Correspondent", "IsShowTrack", json.toString())
    }

    /**
     * 切换昼夜模式
     * “day”白天，“night”晚上
     */
    var isSwLight = false
    fun switchLight() {
        isSwLight = !isSwLight
        if (isSwLight) {
            UnityPlayer.UnitySendMessage("Correspondent", "SwitchLight", "night")
        } else {
            UnityPlayer.UnitySendMessage("Correspondent", "SwitchLight", "day")
        }
    }

    /**
     * 场景初始化
     */
    fun restartScene() {
        UnityPlayer.UnitySendMessage("Correspondent", "RestartScene", "")
    }

    fun start() {
        //开始之前初始化unity
        restartScene()
    }

    /**
     * 设置A点
     */
    fun setPointA() {
        addPoint("A")
    }

    /**
     * 设置B点
     */
    fun setPointB() {
        addPoint("B")
    }
    /**
     * 暂停
     */
    fun pause() {
        //关闭轨迹
        isShowTrack(false, "FF00FF")
    }
    /**
     * 录制
     */
    fun saveRecord() {
        //打开小车轨迹
        isShowTrack(true, "FF00FF")
    }

    /**
     * 停止
     */
    fun stop() {
        //初始化unity
        restartScene()
    }
}