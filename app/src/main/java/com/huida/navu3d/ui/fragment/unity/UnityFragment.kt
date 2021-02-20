package com.huida.navu3d.ui.fragment.unity

import android.os.Bundle
import com.huida.navu3d.databinding.FragmentUnityBinding
import com.huida.navu3d.ui.activity.U3dViewModel
import com.lei.core.base.BaseVmFragment

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 主菜单界面
 */
class UnityFragment : BaseVmFragment<FragmentUnityBinding>(FragmentUnityBinding::inflate) {
    private val u3dViewModel by lazy { getActivityViewModel(U3dViewModel::class.java) }
    companion object {
        fun newInstance() = UnityFragment()
    }



    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            binding.llRoot.addView(u3dViewModel.mUnityPlayer)
            u3dViewModel.mUnityPlayer!!.requestFocus()
        }
    }

    override fun observe() {

    }


}