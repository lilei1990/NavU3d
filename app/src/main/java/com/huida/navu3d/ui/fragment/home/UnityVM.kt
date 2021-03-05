package com.huida.navu3d.ui.fragment.home

import android.util.Log
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.TrackLineData
import com.lei.core.base.BaseViewModel
import com.unity3d.player.UnityPlayer
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONObject

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :Unity fragment具体实现类
 */
class UnityVM : BaseViewModel() {
    //缩小的比例系数
    val scaleX = 100000
    val scaleY = 1000000

    /**
     * 设置标记点
     */
    fun addPoint(s: String) {
        UnityPlayer.UnitySendMessage("Correspondent", "SetSignBrand", s)
    }

    /**
     * 添加导航线
     */
    //防止线的编号为负数,unity不能接收负号
    val flag = 100000
    var paralleLine: MutableMap<Int, Polyline>? = null
    fun addParallelLine(it: MutableMap<Int, Polyline>) {
        removeParallelLine()
        paralleLine = it
        val lines = JSONArray()
        for ((key, value) in it) {
            val line = JSONObject()
            val startPoint = JSONObject()
            val endPoint = JSONObject()
            val x0: Float = scaleX(value.getPoint(0).x)
            val y0: Float = scaleY(value.getPoint(0).y)
            val x1: Float = scaleX(value.getPoint(1).x)
            val y1: Float = scaleY(value.getPoint(1).y)
            startPoint.put("x", x0)
            startPoint.put("y", y0)
            endPoint.put("x", x1)
            endPoint.put("y", y1)
            line.put("nav", JSONArray().put(startPoint).put(endPoint))
            line.put("name", "line${flag - key}")
            lines.put(line)
            Log.d("TAGlilei", "addParallelLine: ${flag - key}")
        }
        UnityPlayer.UnitySendMessage("Correspondent", "BuildNavLine", lines.toString())

    }

    fun removeParallelLine() {
        paralleLine?.forEach { t, u ->
            Log.d("TAGlilei", "addParallelLine: ${t}")
            UnityPlayer.UnitySendMessage("Correspondent", "RemoveNavLine", "line${flag - t}")
        }
    }

    /**
     * 小车移动
     */

    fun moveCart(
        it: PointData,
        speed: Double
    ) {
        val point=it.toUtm()
        var x = scaleX(point.easting)
        var y = scaleY(point.northing)
//        Log.d("TAG_lilei", "moveCart: ${x}--${y}")
        val json = JSONObject()
        json.put("x", x)
        json.put("y", y)
        //这个速度知识插值器,当数据刷新频率达到一定程度就影响不到实际效果
        json.put("moveSpeed", speed)
        UnityPlayer.UnitySendMessage("Correspondent", "SendVData", json.toString())
    }

    /**
     * 生成历史农机轨迹
     */

    fun showHistoryTrack(
        it: TrackLineData

    ) {

        val id = it.getId()
        val points = it.points
        val json = JSONObject()
        val jsonArray = JSONArray()
        for (point in points) {
            val p=point.toUtm()
            var x = scaleX(p.easting)
            var y = scaleY(p.northing)
            val startPoint = JSONObject()
            startPoint.put("x", x)
            startPoint.put("y", y)
            jsonArray.put(startPoint)
        }
        json.put("nav", jsonArray)
        json.put("name", "line${id}")
        UnityPlayer.UnitySendMessage("Correspondent", "ShowHistoryTrack", json.toString())
        Log.d("TAGlilei", "cartStance: ${json.toString()}")
    }

    /**
     * 小车姿态
     */
    fun cartStance(
        steerAngle: Double
    ) {
//        Log.d("TAG_lilei", "cartStance: ${steerAngle}")
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
        UnityPlayer.UnitySendMessage("SceneController", "RestartScene", "")
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
     * 录制
     */
    fun saveRecord(isRecord: Boolean) {
        //打开小车轨迹
        isShowTrack(isRecord, "FF00FF")
    }

    /**
     * 停止
     */
    fun stop() {
        //初始化unity
        restartScene()
    }


    fun scaleX(a: Double): Float {
        return (a).toFloat() % scaleX
    }

    fun scaleY(a: Double): Float {
        return (a).toFloat() % scaleY
    }



}