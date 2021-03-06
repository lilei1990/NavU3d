package com.huida.navu3d.ui.fragment.farmTools

import android.os.Bundle
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.databinding.FragmentFarmToolsConfigBinding
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 农具配置
 */
class FarmToolsConfigFragment :
    BaseVmFragment<FragmentFarmToolsConfigBinding>(FragmentFarmToolsConfigBinding::inflate) {
    private val viewModel by lazy { getActivityViewModel(FarmToolsConfigVM::class.java) }

    companion object {
        fun newInstance() =
            FarmToolsConfigFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        //返回按钮
        binding.incTitleBar.clBack.clickNoRepeat {
            liveEvenBus(BusEnum.TO_PAGE_SETTING).post("")
        }
        //保存
        binding.incTitleBar.clConfirm.clickNoRepeat {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.firstData.width = binding.etWide.text.toString().toDouble()
                viewModel.firstData.offset = binding.etOffset.text.toString().toDouble()
                viewModel.firstData.centerToSuspension = binding.etBackCenter.text.toString().toDouble()
                viewModel.firstData.overlapping = binding.etOverlapping.text.toString().toDouble()
                viewModel.setFarmToolsData()
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {

            viewModel.getFarmToolsData().asFlow().collect {
                binding.etWide.setText("${it.width}")
                binding.etOffset.setText("${it.offset}")
                binding.etBackCenter.setText("${it.centerToSuspension}")
                binding.etOverlapping.setText("${it.overlapping}")
            }

        }


    }


}
