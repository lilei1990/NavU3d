package com.huida.navu3d.ui.fragment.main

import android.os.Bundle
import com.huida.navu3d.databinding.FragmentHomeBinding
import com.lei.core.base.BaseVmFragment
import com.lilei.pwdpie.fragment.main.MainViewModel


class HomeFragment : BaseVmFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun init(savedInstanceState: Bundle?) {

    }



}