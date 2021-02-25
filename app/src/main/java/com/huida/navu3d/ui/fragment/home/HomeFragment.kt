package com.huida.navu3d.ui.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.ui.fragment.workTask.WorkTaskVM
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlin.math.roundToInt


class HomeFragment : BaseVmFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeViewModel by lazy { getFragmentViewModel(HomeVM::class.java)!! }
    private val workTaskViewModel by lazy { getActivityViewModel(WorkTaskVM::class.java) }


    override fun init(savedInstanceState: Bundle?) {
        homeViewModel.taskWorkby = workTaskViewModel.selectWorkTaskData
        binding.apply {
            initU3dLayout()
        }
        workTaskViewModel.workTaskData
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
        //与最近导航线偏移距离
        homeViewModel.DataOffsetLineDistance.observe(this) {
            binding.incTopBar.tvLength.text = "${it}"
        }
        //卫星数
        homeViewModel.DataSatelliteCount.observe(this) {
            binding.incTopBar.tvStatellite.text = it
        }
        //角度
        homeViewModel.DataSteerAngle.observe(this) {
            binding.incTopBar.tvRtk.text = "${it}°"
        }
        //速度
        homeViewModel.DataSpeed.observe(this) {
            binding.incTopBar.tvSpeed.text = "${(it * 100).roundToInt() / 100.00}Km/h"
        }
        //A点
        homeViewModel.DataPointA.observe(this) {
            Log.d("TAG_lilei", "observeA: ")
        }
        //B点
        homeViewModel.DataPointB.observe(this) {
            Log.d("TAG_lilei", "observeB: ")
        }
    }



    override fun onDestroy() {
        NmeaProviderManager.clearAllRegist()
        super.onDestroy()
    }
}