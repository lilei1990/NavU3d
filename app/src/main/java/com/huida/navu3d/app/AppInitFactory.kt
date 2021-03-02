package com.huida.navu3d.app

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.huida.navu3d.bean.GuideLineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.TrackLineData
import com.huida.navu3d.bean.WorkTaskData
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal

/**
 * 作者 : lei
 * 时间 : 2021/03/02.
 * 邮箱 :416587959@qq.com
 * 描述 : 不通进程进行不通的初始化操作
 */
object AppInitFactory {
    fun getAppInitialization(processName: String): AppInitialization? {
        val appInitialization: AppInitialization?
        if (processName.endsWith(":main")) {
            appInitialization =
                MainAppInitialization()
        } else if (processName.endsWith(":unity")) {
            appInitialization =
                UnityAppInitialization()
        } else {
            appInitialization = null
        }
        return appInitialization
    }

    internal class MainAppInitialization :
        AppInitialization {
        override fun onAppCreate(application: Application?) {
            Log.d("TAG_lilei", "init: 初始化main进程")
            LitePal.initialize(application!!.applicationContext);
            //sql web调试工具
            Stetho.initializeWithDefaults(application);
//LitePal.deleteAll(WorkTaskData::class.java)
//LitePal.deleteAll(PointData::class.java)
//LitePal.deleteAll(GuideLineData::class.java)
//LitePal.deleteAll(TrackLineData::class.java)
            //初始化bugly
            CrashReport.initCrashReport(application!!.applicationContext, "9882dbea36", false);
//        SkinCompatManager.withoutActivity(this)
//            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤初始化
//            .addInflater(SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
//            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout 控件换肤初始化[可选]
//            .addInflater(SkinCardViewInflater()) // CardView v7 控件换肤初始化[可选]
////            .setSkinStatusBarColorEnable(false) // 关闭状态栏换肤，默认打开[可选]
//            .setSkinWindowBackgroundEnable(false) // 关闭windowBackground换肤，默认打开[可选]
//            .loadSkin()
        }

    }

    internal class UnityAppInitialization :
        AppInitialization {
        override fun onAppCreate(application: Application?) {
            Log.d("TAG_lilei", "init: 初始化unity进程")
            LitePal.initialize(application!!.applicationContext);
        }

    }
}