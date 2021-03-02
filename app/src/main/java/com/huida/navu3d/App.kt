package com.huida.navu3d

import com.facebook.stetho.Stetho
import com.lei.core.BaseApp
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal


/**
 * 作者 : lei
 * 时间 : 2021/01/07.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class App: BaseApp() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this);
        //sql web调试工具
        Stetho.initializeWithDefaults(this);
//LitePal.deleteAll(WorkTaskData::class.java)
//LitePal.deleteAll(PointData::class.java)
//LitePal.deleteAll(GuideLineData::class.java)
//LitePal.deleteAll(TrackLineData::class.java)
        //初始化bugly
        CrashReport.initCrashReport(applicationContext, "9882dbea36", false);
//        SkinCompatManager.withoutActivity(this)
//            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤初始化
//            .addInflater(SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
//            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout 控件换肤初始化[可选]
//            .addInflater(SkinCardViewInflater()) // CardView v7 控件换肤初始化[可选]
////            .setSkinStatusBarColorEnable(false) // 关闭状态栏换肤，默认打开[可选]
//            .setSkinWindowBackgroundEnable(false) // 关闭windowBackground换肤，默认打开[可选]
//            .loadSkin()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            println(t)
        }
    }
}