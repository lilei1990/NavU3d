package com.huida.navu3d.ui.activity.main

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.huida.navu3d.ui.fragment.farmMachinery.FarmMachineryConfigFragment
import com.huida.navu3d.ui.fragment.farmTools.FarmToolsConfigFragment
import com.huida.navu3d.ui.fragment.home.HomeFragment
import com.huida.navu3d.ui.fragment.main.MainFragment
import com.huida.navu3d.ui.fragment.main.MainMenuFragment
import com.huida.navu3d.ui.fragment.main.SettingFragment
import com.huida.navu3d.ui.fragment.workTask.WorkTaskFragment

/**
 * 作者 : lei
 * 时间 : 2020/11/28.
 * 邮箱 :416587959@qq.com
 * 描述 :主界面page
 */
class ViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: SparseArray<Fragment> = SparseArray()

    init {
        fragments.put(PAGE_MAIN, MainFragment())
        fragments.put(PAGE_HOME, HomeFragment())
//        fragments.put(PAGE_FARM_MACHINERY, FarmMachineryConfigFragment())
//        fragments.put(PAGE_FARM_TOOLS, FarmToolsConfigFragment())
//        fragments.put(PAGE_WORK_TASK, WorkTaskFragment())
//        fragments.put(PAGE_SETTING, SettingFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size()
    }

    companion object {

        const val PAGE_MAIN = 0
        const val PAGE_HOME = 1
//        const val PAGE_FARM_MACHINERY = 2
//        const val PAGE_FARM_TOOLS = 3
//        const val PAGE_WORK_TASK = 4
//        const val PAGE_SETTING = 5

    }
}
