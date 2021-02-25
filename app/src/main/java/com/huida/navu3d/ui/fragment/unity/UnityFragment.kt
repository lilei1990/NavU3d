package com.huida.navu3d.ui.fragment.unity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.observe
import com.huida.navu3d.common.NameProviderManager
import com.huida.navu3d.databinding.FragmentUnityBinding
import com.huida.navu3d.ui.activity.unity.U3dViewModel
import com.huida.navu3d.ui.fragment.home.HomeViewModel
import com.huida.navu3d.utils.PointConvert
import com.lei.core.base.BaseVmFragment

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class UnityFragment : BaseVmFragment<FragmentUnityBinding>(FragmentUnityBinding::inflate) {
    private val u3dViewModel by lazy { getActivityViewModel(U3dViewModel::class.java) }
    private val homeViewModel by lazy { getActivityViewModel(HomeViewModel::class.java)!! }
    lateinit var llRoot: LinearLayout

    companion object {
        fun newInstance() = UnityFragment()
    }


    override fun init(savedInstanceState: Bundle?) {
        llRoot = binding.llRoot
        u3dViewModel.restartScene()
        binding.apply {
            binding.llRoot.addView(u3dViewModel.mUnityPlayer)
            u3dViewModel.mUnityPlayer!!.requestFocus()
            binding.scDayNight.clickNoRepeat {
                u3dViewModel.switchLight()
            }
        }
    }

    var aaa = 4000000.000
    override fun observe() {

        homeViewModel.DataPointA.observe(this) {
            u3dViewModel.addPoint("A")

        }
        homeViewModel.DataPointB.observe(this) {
            u3dViewModel.addPoint("B")
        }
        //打开小车轨迹
        u3dViewModel.isShowTrack(true, "FF00FF")
        NameProviderManager.registGGAListen("UnityFragment") {
            aaa+=0.01
            val position = it.position
            val latitude = position.latitude
            val longitude = position.longitude
            val pointXY = PointConvert.convertPoint(latitude, longitude)

            u3dViewModel.moveCart(pointXY, 1.0)
        }
        NameProviderManager.registVTGListen("UnityFragment") {

            u3dViewModel.cartStance(it.trueCourse)
        }
        homeViewModel.DataParallelLine.observe(this) {
            u3dViewModel.addParallelLine(it)

        }
    }

    override fun onDestroy() {
        llRoot.removeAllViews()
        super.onDestroy()
    }
}