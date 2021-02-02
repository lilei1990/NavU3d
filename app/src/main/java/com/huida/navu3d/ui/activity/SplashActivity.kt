package com.huida.navu3d.ui.activity

import android.Manifest
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils.SimpleCallback
import com.huida.navu3d.R
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.databinding.ActivitySplashBinding

import com.lei.base_core.base.BaseVmActivity
import com.lei.base_core.helper.PermissionHelper
import com.lei.base_core.utils.PrefUtils
import com.lei.base_core.utils.StatusUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 开屏页
 * 权限：https://www.cnblogs.com/blosaa/p/9584348.html
 */
class SplashActivity : BaseVmActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val tips = "现在要向您申请存储权限，用于访问您的本地音乐，您也可以在设置中手动开启或者取消。"
    private val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        changeTheme()
        super.onCreate(savedInstanceState)


    }

    override fun init(savedInstanceState: Bundle?) {
        PermissionHelper.request(this, object : SimpleCallback {
            override fun onGranted() {
                startIntent()
            }

            override fun onDenied() {
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }


    /**
     * 开始倒计时跳转
     */
    private fun startIntent() {
        launch {
            delay(100)
            ActivityUtils.startActivity(MainActivity::class.java)
            finish()
        }
    }


    /**
     * 动态切换主题
     */
    private fun changeTheme() {
        val theme = PrefUtils.getBoolean(Constants.SP_THEME_KEY, false)
        if (theme) {
            setTheme(R.style.AppTheme_Night)
        } else {
            setTheme(R.style.AppTheme)
        }
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
