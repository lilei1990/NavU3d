package com.huida.navu3d.ui.fragment.farmMachinery

import android.os.Bundle
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.databinding.FragmentFarmMachineryConfigBinding
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 农机配置
 */
class FarmMachineryConfigFragment :
    BaseVmFragment<FragmentFarmMachineryConfigBinding>(FragmentFarmMachineryConfigBinding::inflate){
    private val viewModel by lazy { getActivityViewModel(FarmMachineryVM::class.java) }

    companion object {
        fun newInstance() =
            FarmMachineryConfigFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        //返回按钮
        binding.incTitleBar.clBack.clickNoRepeat {
            liveEvenBus(BusEnum.TO_PAGE_SETTING).post("")
        }
        //保存
        binding.incTitleBar.clConfirm.clickNoRepeat {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.firstData.wheelbase = binding.etWheelbase.text.toString().toDouble()
                viewModel.firstData.antennaToFront = binding.etAntennaToFront.text.toString().toDouble()
                viewModel.firstData.antennaHeight = binding.etAntennaHeight.text.toString().toDouble()

                viewModel.saveData()
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {

            viewModel.getData().asFlow().collect {
                binding.etWheelbase.setText("${it.wheelbase}")
                binding.etAntennaToFront.setText("${it.antennaToFront}")
                binding.etAntennaHeight.setText("${it.antennaHeight}")

            }

        }
    }


}