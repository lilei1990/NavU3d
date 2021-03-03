package com.huida.navu3d.ui.activity.unity

import android.util.Log
import androidx.lifecycle.ViewModel
import com.huida.navu3d.bean.PointData
import com.unity3d.player.UnityPlayer
import org.jetbrains.annotations.Nullable
import org.json.JSONObject


class U3dVM : ViewModel() {




    /**
     * 小车移动
     */
    fun moveCart(
        it: PointData,
        speed: Double
    ) {
        var x = (it.x ).toFloat()
        var y = (it.y ).toFloat()
//        var x = (it.X * 1000).toInt()*1f/1000%scaleX
//        var y = (it.Y * 1000).toInt()*1f/1000%scaleY
//        Log.d("TAG_lilei", "moveCart: ${x}--${y}")
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

}