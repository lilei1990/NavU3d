package com.huida.navu3d

import android.os.Build
import android.webkit.WebView
import com.facebook.stetho.Stetho
import com.lei.base_core.BaseApp
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater


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
        Stetho.initializeWithDefaults(this);

        //初始化bugly
        CrashReport.initCrashReport(applicationContext, "9882dbea36", false);
        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤初始化
            .addInflater(SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(SkinCardViewInflater()) // CardView v7 控件换肤初始化[可选]
//            .setSkinStatusBarColorEnable(false) // 关闭状态栏换肤，默认打开[可选]
            .setSkinWindowBackgroundEnable(false) // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin()
    }
}