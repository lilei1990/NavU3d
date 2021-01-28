package com.huida.navu3d.ui.fragment.main

import android.os.Bundle
import androidx.navigation.Navigation
import com.blankj.utilcode.util.SPUtils
import com.huida.navu3d.R
import com.huida.navu3d.common.Constants
import com.huida.navu3d.common.initBackClick
import com.huida.navu3d.databinding.FragmentSettingBinding
import com.lei.base_core.base.BaseVmFragment
import com.lei.base_core.common.clickNoRepeat
import com.lilei.pwdpie.fragment.main.MainViewModel

/**
 * 设置界面
 */
class SettingFragment : BaseVmFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun init(savedInstanceState: Bundle?) {
        binding.incTitleBar.initBackClick()
        binding.btFarmToolsConfig.clickNoRepeat {
            Navigation.findNavController(it)
                .navigate(R.id.action_setting_fragment_to_farm_Tools_fragment)
        }
        binding.btFarmMachineryConfig.clickNoRepeat {
            Navigation.findNavController(it)
                .navigate(R.id.action_setting_fragment_to_fram_machinery_fragment)
        }

        binding.scDayNight.clickNoRepeat {
            val theme = SPUtils.getInstance().getBoolean(Constants.SP_THEME_KEY,false)
            it.isSelected = !theme
            SPUtils.getInstance().put(Constants.SP_THEME_KEY, it.isSelected)
            mActivity.recreate()
        }
    }

}