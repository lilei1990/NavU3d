package com.huida.navu3d.ui.activity

import android.os.Bundle
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.databinding.ActivityMainBinding
import com.lei.core.base.BaseVmActivity
import com.lei.core.utils.PrefUtils
import com.lei.core.utils.StatusUtils


/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 入口activity
 */
class MainActivity : BaseVmActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun init(savedInstanceState: Bundle?) {
//        val db: SQLiteDatabase = Connector.getDatabase()
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



