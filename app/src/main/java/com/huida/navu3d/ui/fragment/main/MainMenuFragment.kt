package com.huida.navu3d.ui.fragment.main

import android.os.Bundle
import androidx.navigation.Navigation
import com.blankj.utilcode.util.AppUtils
import com.huida.navu3d.R
import com.huida.navu3d.databinding.FragmentMainMenuBinding
import com.lei.base_core.base.BaseVmFragment
import com.lei.base_core.common.clickNoRepeat
import com.lei.base_core.common.showBarB
import com.lilei.pwdpie.fragment.main.MainViewModel

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class MainMenuFragment : BaseVmFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun init(savedInstanceState: Bundle?) {
        binding.btStart.clickNoRepeat {
            activity?.showBarB("开始")
            Navigation.findNavController(it)
                .navigate(R.id.action_main_menu_fragment_to_task_list_fragment)
        }
        binding.btSetting.clickNoRepeat {
            activity?.showBarB("设置")
            Navigation.findNavController(it)
                .navigate(R.id.action_main_menu_fragment_to_setting_fragment)
        }
        binding.btExit.clickNoRepeat {
            activity?.showBarB("退出")
            AppUtils.exitApp()
        }
    }



}