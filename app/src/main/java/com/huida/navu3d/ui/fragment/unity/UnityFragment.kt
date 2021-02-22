package com.huida.navu3d.ui.fragment.unity

import android.os.Bundle
import androidx.lifecycle.observe
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.ScaleAnimation
import com.blankj.utilcode.util.ActivityUtils
import com.huida.navu3d.databinding.FragmentUnityBinding
import com.huida.navu3d.ui.activity.main.MainActivity
import com.huida.navu3d.ui.activity.unity.U3dViewModel
import com.huida.navu3d.ui.fragment.home.HomeViewModel
import com.huida.navu3d.utils.GaoDeUtils
import com.lei.core.base.BaseVmFragment
import com.unity3d.player.UnityPlayer
import com.unity3d.splash.services.core.api.Intent.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class UnityFragment : BaseVmFragment<FragmentUnityBinding>(FragmentUnityBinding::inflate) {
    private val u3dViewModel by lazy { getActivityViewModel(U3dViewModel::class.java) }
    private val homeViewModel by lazy { getActivityViewModel(HomeViewModel::class.java)!! }

    companion object {
        fun newInstance() = UnityFragment()
    }


    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            binding.llRoot.addView(u3dViewModel.mUnityPlayer)
            u3dViewModel.mUnityPlayer!!.requestFocus()
        }
    }

    override fun observe() {
        var str = "{\n" +
                "\t\"x\": 31.0,\n" +
                "\t\"y\": 121.0,\n" +
                "\t\"yaw\": 39.0,\n" +
                "\t\"moveSpeed\": 2.0,\n" +
                "\t\"rotationSpeed\": 4.0\n" +
                "}"
        var lines = "[\n" +
                "  {\"nav\":[\n" +
                "          {\"x\":1.0,\"y\":10.0},\n" +
                "          {\"x\":1.0,\"y\":20.0}\n" +
                "          ],\n" +
                "  \"name\":\"line1\"},\n" +
                "  {\"nav\":[\n" +
                "          {\"x\":1.0,\"y\":30.0},\n" +
                "          {\"x\":1.0,\"y\":40.0}\n" +
                "          ],\n" +
                "  \"name\":\"line2\"},\n" +
                "  {\"nav\":[\n" +
                "          {\"x\":1.0,\"y\":50.0},\n" +
                "          {\"x\":1.0,\"y\":60.0}\n" +
                "          ],\n" +
                "  \"name\":\"line3\"}\n" +
                "  \n" +
                "  \n" +
                "]"
        homeViewModel.DataPointA.observe(this) {
            moveCat()
            //生成导航线
            UnityPlayer.UnitySendMessage("Correspondent", "SendData", str)
        }
        UnityPlayer.UnitySendMessage("Correspondent", "BuildNavLine", lines)


//        u3dViewModel.mUnityPlayer.UnitySendMessage("Manager", "Manager", str)
    }

    var a = 31.0
    fun moveCat() {
        GlobalScope.launch {
            delay(1000)
            var str = "{\n" +
                    "\t\"x\": ${a++},\n" +
                    "\t\"y\": 121.0,\n" +
                    "\t\"yaw\": 39.0,\n" +
                    "\t\"moveSpeed\": 2.0,\n" +
                    "\t\"rotationSpeed\": 4.0\n" +
                    "}"
            //生成导航线
            UnityPlayer.UnitySendMessage("Correspondent", "SendData", str)
        }
    }

}