package com.huida.navu3d.ui.fragment.unity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.observe
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.databinding.FragmentUnityBinding
import com.huida.navu3d.ui.activity.unity.U3dVM
import com.huida.navu3d.ui.fragment.home.HomeVM
import com.huida.navu3d.utils.PointConvert
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class UnityFragment : BaseVmFragment<FragmentUnityBinding>(FragmentUnityBinding::inflate) {
    private val u3dViewModel by lazy { getActivityViewModel(U3dVM::class.java) }
    private val homeFragmentBean by lazy { getActivityViewModel(HomeVM::class.java)!!.homeFragmentBean }
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

    override fun observe() {
        homeFragmentBean.pointA.observe(this) {
            u3dViewModel.addPoint("A")

        }
        homeFragmentBean.pointB.observe(this) {
            u3dViewModel.addPoint("B")
        }

        NmeaProviderManager.registGGAListen("UnityFragment") {
            val position = it.position
            val latitude = position.latitude
            val longitude = position.longitude
            val pointXY = PointConvert.convertPoint(latitude, longitude)
            u3dViewModel.moveCart(pointXY, 1.0)
        }
        NmeaProviderManager.registVTGListen("UnityFragment") {

            u3dViewModel.cartStance(it.trueCourse)
        }
        homeFragmentBean.DataParallelLine.observe(this) {
            u3dViewModel.addParallelLine(it)

        }
    }

    override fun onDestroy() {
        llRoot.removeAllViews()
        super.onDestroy()
    }
}