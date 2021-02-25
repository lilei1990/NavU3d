package com.huida.navu3d.ui.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.ui.activity.unity.U3dVM
import com.huida.navu3d.ui.fragment.workTask.WorkTaskVM
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlin.math.roundToInt


class HomeFragment : BaseVmFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeVM by lazy { getActivityViewModel(HomeVM::class.java) }

    private val workTaskViewModel by lazy { getActivityViewModel(WorkTaskVM::class.java) }


    override fun init(savedInstanceState: Bundle?) {
        workTaskViewModel.selectWorkTaskData?.apply {
            homeVM.setWorkTaskData(this)
        }
        binding.apply {

            initU3dLayout()
        }
    }


    private fun initU3dLayout() {
        //切换地图和u3d场景
        binding.btSwitch.clickNoRepeat {
            val progress = binding.mlRoot.progress
            if (progress == 0.0f) {
                binding.mlRoot.transitionToEnd()
                binding.mlRoot.bringChildToFront(binding.llBottom)
            }
            if (progress == 1.0f) {
                binding.mlRoot.transitionToStart()
                binding.mlRoot.bringChildToFront(binding.llTop)
            }
        }
        binding.btSwitch.performClick()
    }


    override fun observe() {
        val homeFragmentBean = homeVM.homeFragmentBean
        homeFragmentBean.offsetLineDistance.observe(this) {
            //与最近导航线偏移距离
            binding.incTopBar.tvLength.text = "${it}"
        }
        homeFragmentBean.satelliteCount.observe(this) {
            //卫星数
            binding.incTopBar.tvStatellite.text = "${it}"
        }
        homeFragmentBean.steerAngle.observe(this) {
            //角度
            binding.incTopBar.tvRtk.text = "${it}"
        }
        homeFragmentBean.speed.observe(this) {
            //速度
            binding.incTopBar.tvSpeed.text = "${(it * 100).roundToInt() / 100.00}Km/h"
//            binding.incTopBar.tvSpeed.text = "${it}Km/h"
        }
        homeVM.vtgData.observe(this) {
            Log.d("TAG", "observe: lil0ei")
        }
        homeVM.ggaData.observe(this) {
            Log.d("TAG", "observe: lil1ei")
        }
    }


    override fun onDestroy() {
        NmeaProviderManager.clearAllRegist()
        super.onDestroy()
    }
}