package com.huida.navu3d.ui.fragment.home

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.ui.activity.main.MainActivity
import com.huida.navu3d.utils.GeometryUtils
import com.kongqw.rockerlibrary.view.RockerView
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class HomeFragment : BaseVmFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeVM by lazy { getActivityViewModel(HomeVM::class.java)!! }

    private val unityVM by lazy { getActivityViewModel(UnityVM::class.java)!! }
    val mUnityPlayer by lazy { (activity as MainActivity).mUnityPlayer }

    //    var workTaskData: WorkTaskData? = null
    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            initButton()
            initU3dLayout()
            initRockView()
        }


    }


    override fun onResume() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            AlertDialog.Builder(requireContext())
                .setTitle("提示")
                .setMessage("确认退出吗?")
                .setNegativeButton(
                    "取消"
                ) { dialog, which ->

                }
                .setPositiveButton(
                    "确定"
                ) { dialog, which -> //跳转到main page
                    liveEvenBus(BusEnum.TO_PAGE_MAIN).post(1)
                    unityVM.restartScene()
                    homeVM.stop()
                    dialog?.dismiss()
                }
                .show()

        }
        super.onResume()
    }


    /**
     * 初始化u3d的布局
     */
    private fun initU3dLayout() {

        binding.apply {
            if (mUnityPlayer?.getParent() != null) {
                val view = mUnityPlayer?.getParent() as ViewGroup
                view.removeAllViews()
            }
            binding.incUnity.llRoot.addView(mUnityPlayer)

            mUnityPlayer!!.requestFocus()
            binding.incUnity.scDayNight.clickNoRepeat {
                unityVM.switchLight()
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
//        binding.btSwitch.performClick()
    }

    /**
     * 订阅消息
     */
    override fun observe() {
        val homeRepo = homeVM.homeRepo
        homeRepo.offsetLineDistance.observe(this) {
            //与最近导航线偏移距离
            binding.incTopBar.tvLength.text = "${(it * 10000).roundToInt() / 100.00}"
        }
        homeRepo.satelliteCount.observe(this) {
            //卫星数
            binding.incTopBar.tvStatellite.text = "${it}"
        }
        homeRepo.steerAngle.observe(this) {
            //角度
            binding.incTopBar.tvRtk.text = "${it}"
            unityVM.cartStance(it)
        }
        homeRepo.speed.observe(this) {
            //速度
            binding.incTopBar.tvSpeed.text = "${(it * 100).roundToInt() / 100.00}Km/h"
        }
        //当前位置
        homeRepo.currenLatLng.observe(this) {
            unityVM.moveCart(it, 2.0)
        }
        //是否录制
        homeRepo.isRecord.observe(this) {
            unityVM.saveRecord(it)
        }
        //A
        homeRepo.pointA.observe(this) {
            unityVM.setPointA()

        }
        //B
        homeRepo.pointB.observe(this) {
            unityVM.setPointB()

        }
        homeRepo.status.observe(this) {
            when (it) {
                HomeRepo.Status.START -> {

//                    binding.incMenu.bt5.itemRoot.visibility=View.INVISIBLE
//                    binding.incMenu.bt8.itemRoot.visibility=View.VISIBLE
                }
                HomeRepo.Status.PAUSE -> {
//                    binding.incMenu.bt5.itemRoot.visibility=View.INVISIBLE
                }
                HomeRepo.Status.STOP -> {
//                    binding.incMenu.bt5.itemRoot.visibility=View.VISIBLE
//                    binding.incMenu.bt8.itemRoot.visibility=View.INVISIBLE
                }
            }
        }
        homeRepo.DataParallelLine.observe(this) {
            unityVM.addParallelLine(it)

        }
        homeRepo.workLength.observe(this) {
            binding.incTopBar.tvMileage.text="${(it * 100).roundToInt() / 100.00}米"
            binding.incTopBar.tvArea.text=
                "${(GeometryUtils.calculationWorkArea(it) * 100).roundToInt() / 100.00}亩"

        }
        //轨迹的历史数据
        homeRepo.trackLineHistory.observe(this) {
            it.forEachIndexed { index, data ->
                unityVM.showHistoryTrack(data)
            }
        }
        //订阅 数据
        liveEvenBus(BusEnum.SELECT_WORK_TASK_DATA, WorkTaskData::class.java)
            .observe(this, Observer {
                lifecycleScope.launch {
                    homeVM.setWorkTaskData(it)
                }
            })
    }

    private fun initButton() {
        binding.incUnity.tvNight.text = "夜间模式"
        binding.incUnity.scDayNight.clickNoRepeat {
            unityVM.switchLight()
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
            unityVM.restartScene()
        }

    }

    private fun initRockView() {
        binding.incDome.rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
        binding.incDome.rockerView.setOnShakeListener(
            RockerView.DirectionMode.DIRECTION_4_ROTATE_45,
            object : RockerView.OnShakeListener {
                override fun onStart() {
                }

                var offsetSpeedDistance = 0.01
                override fun direction(direction: RockerView.Direction) {
                    when (direction) {
                        RockerView.Direction.DIRECTION_LEFT -> {
                            NmeaProviderManager.left()
                        }
                        RockerView.Direction.DIRECTION_RIGHT -> {
                            NmeaProviderManager.right()
                        }
                        RockerView.Direction.DIRECTION_UP -> {
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN -> {
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }


                    }
                }

                override fun onFinish() {
                    NmeaProviderManager.center()
                }
            })
    }


    override fun onDestroy() {

        homeVM.stop()
        val parent = mUnityPlayer?.parent as ViewGroup
        parent.removeAllViews()
        NmeaProviderManager.clearAllRegist()
        super.onDestroy()
    }
}