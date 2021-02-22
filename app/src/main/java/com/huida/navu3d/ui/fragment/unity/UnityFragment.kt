package com.huida.navu3d.ui.fragment.unity

import android.os.Bundle
import androidx.lifecycle.observe

import com.huida.navu3d.databinding.FragmentUnityBinding
import com.huida.navu3d.ui.activity.unity.U3dViewModel
import com.huida.navu3d.ui.fragment.home.HomeViewModel
import com.lei.core.base.BaseVmFragment
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.GlobalScope
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
                "\t\"x\": 449290.2745917518,\n" +
                "\t\"y\": 4416840.592564532,\n" +
                "\t\"yaw\": 39.0,\n" +
                "\t\"moveSpeed\": 2.0,\n" +
                "\t\"rotationSpeed\": 4.0\n" +
                "}"
        var linesss = "[\n" +
                "  {\"nav\":[\n" +
                "          {\"x\":449290.2745917518,\"y\":4416840.592564532},\n" +
                "          {\"x\":449297.6005963016,\"y\":4417740.847266125}\n" +
                "          ],\n" +
                "  \"name\":\"line1\"},\n" +
                "  {\"nav\":[\n" +
                "          {\"x\":449300.27426065726,\"y\":4416840.511190205},\n" +
                "          {\"x\":449307.60026520706,\"y\":4417740.765891798}\n" +
                "          ],\n" +
                "  \"name\":\"line2\"},\n" +
                "  {\"nav\":[\n" +
                "          {\"x\":449300.27426065726,\"y\":4416840.511190205},\n" +
                "          {\"x\":449307.60026520706,\"y\":4417740.765891798}\n" +
                "          ],\n" +
                "  \"name\":\"line3\"}\n" +
                "  \n" +
                "  \n" +
                "]"
        homeViewModel.DataPointA.observe(this) {
            u3dViewModel.addPoint("A")
//            moveCat()
        }
        homeViewModel.DataPointA.observe(this) {
            u3dViewModel.addPoint("B")
        }
        homeViewModel.DataCurrenLatLng.observe(this) {

            u3dViewModel.moveCart(it,homeViewModel.steerAngle,homeViewModel.speed)
        }

        homeViewModel.DataParallelLine.observe(this) {
            u3dViewModel.addParallelLine(it)
//            UnityPlayer.UnitySendMessage("Correspondent", "SendData", str)

        }



//        u3dViewModel.mUnityPlayer.UnitySendMessage("Manager", "Manager", str)
    }

    var a = 31.0
    fun moveCat() {
        GlobalScope.launch {
//            delay(1000)
            a += 2
            var str = "{\n" +
                    "\t\"x\": ${a},\n" +
                    "\t\"y\": 121.0,\n" +
                    "\t\"yaw\": 39.0,\n" +
                    "\t\"moveSpeed\": 2.0,\n" +
                    "\t\"rotationSpeed\": 0.0\n" +
                    "}"
            //生成导航线
            UnityPlayer.UnitySendMessage("Correspondent", "SendData", str)
        }
    }

}