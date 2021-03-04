package com.huida.navu3d.ui.activity.main

import android.os.Bundle
import androidx.lifecycle.Observer
import com.huida.navu3d.common.BusEnum
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.databinding.ActivityMainBinding
import com.huida.navu3d.ui.activity.unity.U3dExtActivity
import com.lei.core.utils.PrefUtils
import com.lei.core.utils.StatusUtils


/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 入口activity
 */
class MainActivity : U3dExtActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    var start = System.currentTimeMillis()
    override fun init(savedInstanceState: Bundle?) {
        binding.viewPager.adapter = ViewPageAdapter(this)
        binding.viewPager.offscreenPageLimit = 2
//        binding.viewPager.setCurrentItem()
        binding.viewPager.isUserInputEnabled=false
    }

    override fun observe() {
        //跳转到主界面
        liveEvenBus(BusEnum.TO_PAGE_MAIN)
            .observe(this, Observer {
                binding.viewPager.setCurrentItem(ViewPageAdapter.PAGE_MAIN, false)
            })
        //跳转到home界面
        liveEvenBus(BusEnum.TO_PAGE_HOME)
            .observe(this, Observer {
                binding.viewPager.setCurrentItem(ViewPageAdapter.PAGE_HOME, false)
            })
        //退出
        liveEvenBus(BusEnum.TO_EXIT)
            .observe(this, Observer {
                finish()
            })
    }

    /**
     * 沉浸式状态,随主题改变
     */
    override fun setSystemInvadeBlack() {
        val theme = PrefUtils.getBoolean(Constants.SP_THEME_KEY, false)
        if (theme) {
            StatusUtils.setSystemStatus(this, true, false)
        } else {
            StatusUtils.setSystemStatus(this, true, true)
        }
    }

}



