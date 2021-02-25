package com.huida.navu3d.ui.activity.unity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt


class U3dViewModel : ViewModel() {
    var mUnityPlayer: UnityPlayer? = null

    /**
     * 设置标记点
     */
    fun addPoint(s: String) {

        UnityPlayer.UnitySendMessage("Correspondent", "SetSignBrand", s)
    }

    /**
     * 添加导航线
     */
    val scale = 100.0
    val scaleY = 10000000
    fun addParallelLine(it: MutableMap<Int, Polyline>) {
        val lines = JSONArray()
        for ((key, value) in it) {
            val line = JSONObject()
            val startPoint = JSONObject()
            val endPoint = JSONObject()
            var x0: Double = (value.getPoint(0).x *100).toInt()/scale
            var y0: Double = (value.getPoint(0).y *100).toInt()/scale
            var x1: Double = (value.getPoint(1).x *100).toInt()/scale
            var y1: Double = (value.getPoint(1).y *100).toInt()/scale
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
    fun moveCart(
        it: PointData,
        speed: Double
    ) {
        var x = (it.X ).toFloat()
        var y = (it.Y ).toFloat()
//        var x = (it.X * 1000).toInt()*1f/1000%scaleX
//        var y = (it.Y * 1000).toInt()*1f/1000%scaleY
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
        json.put("isShowTrack", true)
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
}