package com.huida.navu3d.ui.activity.unity

import android.util.Log
import androidx.lifecycle.ViewModel
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.unity3d.player.UnityPlayer
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONObject


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
    fun addParallelLine(it: MutableMap<Int, Polyline>) {
        val lines = JSONArray()
        for ((key,value) in it) {
            val line = JSONObject()
            val startPoint = JSONObject()
            val endPoint = JSONObject()
            startPoint.put("x",value.getPoint(0).x)
            startPoint.put("y",value.getPoint(0).y)
            endPoint.put("x",value.getPoint(1).x)
            endPoint.put("y",value.getPoint(1).y)
            line.put("nav", JSONArray().put(startPoint).put(endPoint))
            line.put("name","line1")
            lines.put(line)
        }
        UnityPlayer.UnitySendMessage("Correspondent", "BuildNavLine", lines.toString())

    }

    /**
     * 小车移动
     */
    fun moveCart(
        it: PointData,
        steerAngle: Double,
        speed: Double
    ) {
//        Log.d("TAG_lilei", "moveCart: ${it.X}--${it.Y}")
        val json = JSONObject()
        json.put("x",it.X)
        json.put("y",it.Y)
        json.put("yaw",steerAngle)
        json.put("moveSpeed",speed)
        json.put("rotationSpeed",4.0)
        UnityPlayer.UnitySendMessage("Correspondent", "SendData", json.toString())
    }

    fun isShowTrack(isShow:Boolean,@Nullable color:String) {
        val json = JSONObject()
        json.put("isShowTrack",true)
        json.put("colorStr",color)
        UnityPlayer.UnitySendMessage("Correspondent", "IsShowTrack", json.toString())
    }
}