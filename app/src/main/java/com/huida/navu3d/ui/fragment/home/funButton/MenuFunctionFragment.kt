package com.huida.navu3d.ui.fragment.home.funButton

import android.os.Bundle
import com.huida.navu3d.databinding.FragmentHomeMenuFunctionBinding
import com.huida.navu3d.ui.fragment.home.HomeVM
import com.huida.navu3d.ui.fragment.workTask.WorkTaskVM
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class MenuFunctionFragment : BaseVmFragment<FragmentHomeMenuFunctionBinding>(FragmentHomeMenuFunctionBinding::inflate) {
    private val homeViewModel by lazy { getActivityViewModel(HomeVM::class.java)!! }
    private val workTaskViewModel by lazy { getActivityViewModel(WorkTaskVM::class.java) }
    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            initButton()
        }
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
//            homeViewModel!!.homeFragmentBean.DrawMapParallelLine(workTaskViewModel)
        }
        binding.bt4.tvText.text = "刷新线"
        binding.bt4.itemRoot.clickNoRepeat {
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


    }
}