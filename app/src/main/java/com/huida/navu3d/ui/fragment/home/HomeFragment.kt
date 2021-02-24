package com.huida.navu3d.ui.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import com.blankj.utilcode.util.LogUtils
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.common.NameProviderManager
import com.huida.navu3d.ui.fragment.main.MainMenuFragment
import com.huida.navu3d.ui.fragment.workTask.WorkTaskViewModel
import com.kongqw.rockerlibrary.view.RockerView
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlin.math.roundToInt


class HomeFragment : BaseVmFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeViewModel by lazy { getActivityViewModel(HomeViewModel::class.java)!! }
    private val workTaskViewModel by lazy { getActivityViewModel(WorkTaskViewModel::class.java) }

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        homeViewModel.taskWorkby = workTaskViewModel.selectWorkTaskData
        binding.apply {
            initU3dLayout()
            initButton()
            initRockView()
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

    private fun initButton() {
        binding.bt1.tvText.text = "设置A点"
        binding.bt1.itemRoot.clickNoRepeat {
            homeViewModel!!.setPointA()
        }
        binding.bt2.tvText.text = "设置B点"
        binding.bt2.itemRoot.clickNoRepeat {
            homeViewModel!!.setPointB()
        }
        binding.bt3.tvText.text = "生成导航线"
        binding.bt3.itemRoot.clickNoRepeat {
            homeViewModel!!.DrawMapParallelLine(workTaskViewModel)
        }
        binding.bt4.tvText.text = "刷新线"
        binding.bt4.itemRoot.clickNoRepeat {
            homeViewModel!!.DrawTrack()
        }

        binding.bt5.tvText.text = "开始"
        binding.bt5.itemRoot.clickNoRepeat {
            homeViewModel!!.start()
        }
        binding.bt6.tvText.text = "暂停"
        binding.bt6.itemRoot.clickNoRepeat {
            homeViewModel!!.stop()
        }
        binding.bt7.tvText.text = "停止"
        binding.bt7.itemRoot.clickNoRepeat {
            homeViewModel!!.stop()
        }
        binding.bt.clickNoRepeat {
//            mapViewModel.getMarker(this)
//            mapViewModel.getCarMarker(binding.gdMap.map)
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


    private fun initRockView() {
        binding.rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
        binding.rockerView.setOnShakeListener(
            RockerView.DirectionMode.DIRECTION_8,
            object : RockerView.OnShakeListener {
                override fun onStart() {

                }

                var offsetAngle = 0.01
                var offsetSpeedDistance = 0.005
                override fun direction(direction: RockerView.Direction) {
                    when (direction) {
                        RockerView.Direction.DIRECTION_LEFT -> {
                            NameProviderManager.setAngle(-offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_RIGHT -> {
                            NameProviderManager.setAngle(offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_UP -> {
                            NameProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN -> {
                            NameProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_LEFT -> {
                            NameProviderManager.setAngle(-offsetAngle)
                            NameProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_RIGHT -> {
                            NameProviderManager.setAngle(offsetAngle)
                            NameProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_LEFT -> {
                            NameProviderManager.setAngle(-offsetAngle)
                            NameProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_RIGHT -> {
                            NameProviderManager.setAngle(offsetAngle)
                            NameProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }

                    }
                }

                override fun onFinish() {

                }
            })
    }

    override fun onDestroy() {
        NameProviderManager.clearAllRegist()
        super.onDestroy()
    }
}