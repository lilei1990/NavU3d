package com.huida.navu3d.ui.fragment.farmTools

import android.os.Bundle
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.huida.navu3d.common.initBackClick
import com.huida.navu3d.common.initConfirmClick
import com.huida.navu3d.databinding.FragmentFarmToolsConfigBinding
import com.lei.core.base.BaseVmFragment
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
    private val viewModel by lazy { getFragmentViewModel(FarmToolsConfigVM::class.java) }

    companion object {
        fun newInstance() =
            FarmToolsConfigFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.incTitleBar.initBackClick()
        binding.incTitleBar.initConfirmClick {
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
