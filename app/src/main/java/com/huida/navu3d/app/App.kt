package com.huida.navu3d.app

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import com.huida.navu3d.app.AppInitFactory
import com.lei.core.BaseApp


/**
 * 作者 : lei
 * 时间 : 2021/01/07.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class  App: BaseApp() {
    override fun onCreate() {
        super.onCreate()
        val appInitialization = AppInitFactory.getAppInitialization(getProcessName())
        appInitialization?.onAppCreate(this)


    }
    private fun getCurrentProcessName(): String? {
        var currentProcName = ""
        val pid = Process.myPid()
        val manager: ActivityManager =
            this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.getRunningAppProcesses()) {
            if (processInfo.pid === pid) {
                currentProcName = processInfo.processName
                break
            }
        }
        return currentProcName
    }
}