package com.huida.navu3d.ui.fragment.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.blankj.utilcode.util.LogUtils
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.ui.activity.NameProviderManager
import com.huida.navu3d.ui.activity.U3dViewModel
import com.huida.navu3d.ui.fragment.main.MainMenuFragment
import com.huida.navu3d.ui.fragment.workTask.WorkTaskViewModel
import com.kongqw.rockerlibrary.view.RockerView
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat


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
    }


    private fun initU3dLayout() {
        val u3dViewModel = activity?.let { ViewModelProvider(it).get(U3dViewModel::class.java) }!!
//        binding.llBottom.addView(u3dViewModel.mUnityPlayer)
//        mUnityPlayer!!.requestFocus()
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
            homeViewModel!!.setPointA(workTaskViewModel)
        }
        binding.bt2.tvText.text = "设置B点"
        binding.bt2.itemRoot.clickNoRepeat {
            homeViewModel!!.setPointB(workTaskViewModel)
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

        homeViewModel.DataOffsetLineDistance.observe(this) {
            binding.incTopBar.tvLength.text = "${it}"
        }
        homeViewModel.DataSatelliteCount.observe(this) {
            binding.incTopBar.tvStatellite.text = it
        }
        homeViewModel.DataSteerAngle.observe(this) {
            binding.incTopBar.tvRtk.text = it
        }
        homeViewModel.DataSteerAngle.observe(this) {
            binding.incTopBar.tvRtk.text = it
        }
        homeViewModel.DataSpeed.observe(this) {
            binding.incTopBar.tvSpeed.text = it
        }
    }



    private fun initRockView() {
        binding.rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
        binding.rockerView.setOnShakeListener(
                RockerView.DirectionMode.DIRECTION_8,
                object : RockerView.OnShakeListener {
                    override fun onStart() {

                    }

                    override fun direction(direction: RockerView.Direction) {
                        LogUtils.e("摇动方向 : $direction")
                        when (direction) {
                            RockerView.Direction.DIRECTION_LEFT -> {
                                NameProviderManager.setAngle(-5.0)
                            }
                            RockerView.Direction.DIRECTION_RIGHT -> {
                                NameProviderManager.setAngle(5.0)
                            }
                            RockerView.Direction.DIRECTION_UP -> {
                                NameProviderManager.setSpeedDistance(0.005)
                            }
                            RockerView.Direction.DIRECTION_DOWN -> {
                                NameProviderManager.setSpeedDistance(-0.005)
                            }
                            RockerView.Direction.DIRECTION_UP_LEFT -> {
                                NameProviderManager.setAngle(-5.0)
                                NameProviderManager.setSpeedDistance(0.005)
                            }
                            RockerView.Direction.DIRECTION_UP_RIGHT -> {
                                NameProviderManager.setAngle(5.0)
                                NameProviderManager.setSpeedDistance(0.005)
                            }
                            RockerView.Direction.DIRECTION_DOWN_LEFT -> {
                                NameProviderManager.setAngle(-5.0)
                                NameProviderManager.setSpeedDistance(-0.005)
                            }
                            RockerView.Direction.DIRECTION_DOWN_RIGHT -> {
                                NameProviderManager.setAngle(5.0)
                                NameProviderManager.setSpeedDistance(-0.005)
                            }

                        }
                    }

                    override fun onFinish() {

                    }
                })
    }

}