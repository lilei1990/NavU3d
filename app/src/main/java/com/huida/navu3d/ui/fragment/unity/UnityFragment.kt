package com.huida.navu3d.ui.fragment.unity

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.observe
import com.blankj.utilcode.util.SPUtils
import com.huida.navu3d.common.NameProviderManager
import com.huida.navu3d.constants.Constants

import com.huida.navu3d.databinding.FragmentUnityBinding
import com.huida.navu3d.ui.activity.unity.U3dViewModel
import com.huida.navu3d.ui.fragment.home.HomeViewModel
import com.huida.navu3d.utils.PointConvert
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import kotlin.concurrent.fixedRateTimer

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class UnityFragment : BaseVmFragment<FragmentUnityBinding>(FragmentUnityBinding::inflate) {
    private val u3dViewModel by lazy { getActivityViewModel(U3dViewModel::class.java) }
    private val homeViewModel by lazy { getActivityViewModel(HomeViewModel::class.java)!! }
  lateinit  var llRoot:LinearLayout
    companion object {
        fun newInstance() = UnityFragment()
    }


    override fun init(savedInstanceState: Bundle?) {
        llRoot=binding.llRoot
        binding.apply {
            binding.llRoot.addView(u3dViewModel.mUnityPlayer)
            u3dViewModel.mUnityPlayer!!.requestFocus()
            binding.scDayNight.clickNoRepeat {
                u3dViewModel.switchLight()
            }
        }
    }

    override fun observe() {

        homeViewModel.DataPointA.observe(this) {
            u3dViewModel.addPoint("A")

        }
        homeViewModel.DataPointB.observe(this) {
            u3dViewModel.addPoint("B")
        }
//        homeViewModel.DataCurrenLatLng.observe(this) {
//            u3dViewModel.moveCart(it,homeViewModel.steerAngle,homeViewModel.speed)
//        }
        //打开小车轨迹
        u3dViewModel.isShowTrack(true,"FF00FF")
        NameProviderManager.registGGAListen("UnityFragment") {
            val position = it.position
            val latitude = position.latitude
            val longitude = position.longitude
            val pointXY = PointConvert.convertPoint(latitude, longitude)
            u3dViewModel.moveCart(pointXY,0.0)
        }
        NameProviderManager.registVTGListen("UnityFragment") {

            u3dViewModel.cartStance(it.trueCourse)
        }
        homeViewModel.DataParallelLine.observe(this) {
//            u3dViewModel.addParallelLine(it)
//            UnityPlayer.UnitySendMessage("Correspondent", "SendData", str)

        }



//        u3dViewModel.mUnityPlayer.UnitySendMessage("Manager", "Manager", str)
    }

    var a =1.0
    fun moveCat() {
        GlobalScope.launch {
//            delay(1000)
            a += 1
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

    override fun onDestroy() {
        llRoot.removeAllViews()
        super.onDestroy()
    }
}