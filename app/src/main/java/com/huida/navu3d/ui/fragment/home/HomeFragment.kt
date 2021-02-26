package com.huida.navu3d.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.constants.BusConstants
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.ui.activity.unity.U3dVM
import com.huida.navu3d.utils.PointConvert
import com.kongqw.rockerlibrary.view.RockerView
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlin.math.roundToInt


class HomeFragment : BaseVmFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeVM by lazy { getFragmentViewModel(HomeVM::class.java) }
    private val u3dViewModel by lazy { getActivityViewModel(U3dVM::class.java) }
    lateinit var llRoot: LinearLayout
    override fun init(savedInstanceState: Bundle?) {
        liveEvenBus(BusConstants.SELECT_WORK_TASK_DATA.name, WorkTaskData::class.java)
            .observeSticky(this, Observer {
                homeVM.setWorkTaskData(it)
            })
        binding.apply {
            initButton()
            initU3dLayout()
            initRockView()
        }
    }


    private fun initU3dLayout() {
        llRoot =  binding.incUnity.llRoot
        binding.apply {
            binding.incUnity.llRoot.addView(u3dViewModel.mUnityPlayer)
            u3dViewModel.mUnityPlayer!!.requestFocus()
            binding.incUnity.scDayNight.clickNoRepeat {
                u3dViewModel.switchLight()
            }
        }
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
            homeVM.stance()
        }
        homeFragmentBean.speed.observe(this) {
            //速度
            binding.incTopBar.tvSpeed.text = "${(it * 100).roundToInt() / 100.00}Km/h"
        }

        homeFragmentBean.currenLatLng.observe(this) {
            homeVM.move()
        }
    }

    private fun initButton() {
        binding.incUnity.tvNight.text = "夜间模式"
        binding.incUnity.scDayNight.clickNoRepeat {
            homeVM.switchLight()
        }
        binding.incMenu.bt1.tvText.text = "设置A点"
        binding.incMenu.bt1.itemRoot.clickNoRepeat {
            homeVM.setPointA()
        }
        binding.incMenu.bt2.tvText.text = "设置B点"
        binding.incMenu.bt2.itemRoot.clickNoRepeat {
            homeVM.setPointB()
        }
        binding.incMenu.bt3.tvText.text = "生成导航线"
        binding.incMenu.bt3.itemRoot.clickNoRepeat {
            homeVM.drawGuideLine()
        }
        binding.incMenu.bt4.tvText.text = "刷新线"
        binding.incMenu.bt4.itemRoot.clickNoRepeat {
        }

        binding.incMenu.bt5.tvText.text = "开始"
        binding.incMenu.bt5.itemRoot.clickNoRepeat {
            homeVM.start()
        }
        binding.incMenu.bt6.tvText.text = "录制"
        binding.incMenu.bt6.itemRoot.clickNoRepeat {
            homeVM.saveRecord()
        }
        binding.incMenu.bt7.tvText.text = "暂停"
        binding.incMenu.bt7.itemRoot.clickNoRepeat {
            homeVM.pause()
        }
        binding.incMenu.bt8.tvText.text = "停止"
        binding.incMenu.bt8.itemRoot.clickNoRepeat {
            homeVM.stop()
        }

    }

    private fun initRockView() {
        binding.incDome.rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
        binding.incDome.rockerView.setOnShakeListener(
            RockerView.DirectionMode.DIRECTION_8,
            object : RockerView.OnShakeListener {
                override fun onStart() {

                }

                var offsetAngle = 0.01
                var offsetSpeedDistance = 0.005
                override fun direction(direction: RockerView.Direction) {
                    when (direction) {
                        RockerView.Direction.DIRECTION_LEFT -> {
                            NmeaProviderManager.setAngle(-offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_RIGHT -> {
                            NmeaProviderManager.setAngle(offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_UP -> {
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN -> {
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_LEFT -> {
                            NmeaProviderManager.setAngle(-offsetAngle)
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_RIGHT -> {
                            NmeaProviderManager.setAngle(offsetAngle)
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_LEFT -> {
                            NmeaProviderManager.setAngle(-offsetAngle)
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_RIGHT -> {
                            NmeaProviderManager.setAngle(offsetAngle)
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }

                    }
                }

                override fun onFinish() {

                }
            })
    }

    override fun onDestroy() {
        llRoot.removeAllViews()
        NmeaProviderManager.clearAllRegist()
        super.onDestroy()
    }
}