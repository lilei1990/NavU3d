package com.huida.navu3d.ui.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.AppUtils
import com.huida.navu3d.R
import com.huida.navu3d.databinding.FragmentMainBinding
import com.huida.navu3d.databinding.FragmentMainMenuBinding
import com.lei.core.base.BaseVmFragment
import com.lei.core.common.clickNoRepeat
import com.lei.core.common.showBarB

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class MainFragment : BaseVmFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainMenuVM

    override fun init(savedInstanceState: Bundle?) {

    }


    override fun onResume() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
        super.onResume()
    }
}