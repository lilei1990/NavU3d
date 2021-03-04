package com.huida.navu3d.ui.activity.unity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.huida.navu3d.ui.fragment.home.HomeFragmentBean
import com.huida.navu3d.ui.fragment.home.HomeVM
import com.huida.navu3d.ui.fragment.home.UnityVM
import com.lei.core.common.clickNoRepeat
import kotlin.math.roundToInt

/**
 * 作者 : lei
 * 时间 : 2021/03/02.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class UnityActivity : U3dExtActivity<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeVM by lazy { getActivityViewModel(HomeVM::class.java)!! }

    private val unityVM by lazy { getActivityViewModel(UnityVM::class.java)!! }
    lateinit var llRoot: LinearLayout
    var workTaskData: WorkTaskData? = null
    override fun init(savedInstanceState: Bundle?) {

        liveEvenBus(BusEnum.SELECT_WORK_TASK_DATA, WorkTaskData::class.java)
            .observe(this, Observer {
                workTaskData = it
            })
        binding.apply {
            initButton()
            initU3dLayout()
        }


    }

    /**
     * 场景初始化
    请求位置
     */
    fun onSceneLoad() {
        ToastUtils.showLong("onSceneLoad")
    }

    private fun initU3dLayout() {
        llRoot = binding.incUnity.llRoot
        binding.apply {
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
            unityVM.cartStance(it)
        }
        homeFragmentBean.speed.observe(this) {
            //速度
            binding.incTopBar.tvSpeed.text = "${(it * 100).roundToInt() / 100.00}Km/h"
        }
        //当前位置
        homeFragmentBean.currenLatLng.observe(this) {
            unityVM.moveCart(it, 2.0)
        }
        //是否录制
        homeFragmentBean.isRecord.observe(this) {
            unityVM.saveRecord(it)
        }
        //A
        homeFragmentBean.pointA.observe(this) {
            unityVM.setPointA()
            workTaskData?.guideLineData?.apply {
                setStart(it)
            }
        }
        //B
        homeFragmentBean.pointB.observe(this) {
            unityVM.setPointB()
            workTaskData?.guideLineData?.apply {
                setEnd(it)
            }
        }
        homeFragmentBean.status.observe(this) {
            when (it) {
                HomeFragmentBean.Status.START -> {

//                    binding.incMenu.bt5.itemRoot.visibility=View.INVISIBLE
//                    binding.incMenu.bt8.itemRoot.visibility=View.VISIBLE
                }
                HomeFragmentBean.Status.PAUSE -> {
//                    binding.incMenu.bt5.itemRoot.visibility=View.INVISIBLE
                }
                HomeFragmentBean.Status.STOP -> {
//                    binding.incMenu.bt5.itemRoot.visibility=View.VISIBLE
//                    binding.incMenu.bt8.itemRoot.visibility=View.INVISIBLE
                }
            }
        }
        homeFragmentBean.DataParallelLine.observe(this) {
            unityVM.addParallelLine(it)

//            workTaskData.lines.add(it)
        }
        homeFragmentBean.trackLine.observe(this) {
            //存储线
            liveEvenBus(BusEnum.DB_TRACK_LINE)
                .postAcrossProcess(it)
            workTaskData?.trackLineData?.add(it)
            //更新worktask数据
            liveEvenBus(BusEnum.DB_WORK_TASK_DATA)
                .postAcrossProcess(it)
        }


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


    override fun onDestroy() {
        homeVM.stop()
        llRoot.removeAllViews()
        NmeaProviderManager.clearAllRegist()
        super.onDestroy()
    }
}