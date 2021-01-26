package com.huida.navu3d

import com.lei.base_core.BaseApp
import com.tencent.bugly.crashreport.CrashReport
import com.huida.navu3d.db.AppDataBase

/**
 * 作者 : lei
 * 时间 : 2021/01/07.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class App: BaseApp() {
    override fun onCreate() {
        super.onCreate()
        //初始化bugly
        CrashReport.initCrashReport(applicationContext, "9882dbea36", false);

    }
}