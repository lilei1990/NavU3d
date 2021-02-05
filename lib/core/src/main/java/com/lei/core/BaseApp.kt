package com.lei.core

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.LogUtils.IFormatter
import com.blankj.utilcode.util.ProcessUtils
import com.lei.core.base.debug.DebugUtils
import com.lei.core.base.debug.debug.IDebug
import java.util.*

/**
 * 作者 : LiLei
 * 时间 : 2020/06/23.
 * 邮箱 :416587959@qq.com
 * 描述 :BaseApp
 */
open class BaseApp :Application() {
    private var isDebug: Boolean? = null
    private var isMainProcess: Boolean? = null
    /**
     * Android 5.0（API 级别 21）之前的平台版本使用 Dalvik ，Dalvik 限制每个 APK
     * 只能使用一个 classes.dex 字节码文件。当方法数超过64K限制时，
     * 编译就会报错，因此通过多 dex 文件支持库来解决这个问题，
     * 即MultiDex。而Android 5.0及更高版本使用名为 ART 的运行时，
     * 它本身支持从 APK 文件加载多个 DEX 文件。ART 在应用安装时执行预编译，
     * 扫描 classesN.dex 文件，并将它们编译成单个 .oat 文件，以供 Android 设备执行，
     * 因此MultiDex仅针对5.0以下的Android系统。
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        initLog()
        initCrash()
        initDebugMenu()
    }

    companion object{
        private lateinit var sInstance: BaseApp

        fun getContext(): Context {
            return sInstance
        }
    }

    // init it in ur application
    open fun initLog() {
        val config = LogUtils.getConfig()
            .setLogSwitch(isDebug()) // 设置 log 总开关，包括输出到控制台和文件，默认开
            .setConsoleSwitch(isDebug()) // 设置是否输出到控制台开关，默认开
            .setGlobalTag(null) // 设置 log 全局标签，默认为空
            // 当全局标签不为空时，我们输出的 log 全部为该 tag，
            // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
            .setLogHeadSwitch(true) // 设置 log 头信息开关，默认为开
            .setLog2FileSwitch(true) // 打印 log 时是否存到文件的开关，默认关
            .setDir("") // 当自定义路径为空时，写入应用的/cache/log/目录中
            .setFilePrefix("") // 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd$fileExtension"
            .setFileExtension(".log") // 设置日志文件后缀
            .setBorderSwitch(true) // 输出日志是否带边框开关，默认开
            .setSingleTagSwitch(true) // 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
            .setConsoleFilter(LogUtils.V) // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
            .setFileFilter(LogUtils.V) // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
            .setStackDeep(1) // log 栈深度，默认为 1
            .setStackOffset(0) // 设置栈偏移，比如二次封装的话就需要设置，默认为 0
            .setSaveDays(100) // 设置日志可保留天数，默认为 -1 表示无限时长
            // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
            .addFormatter(object : IFormatter<ArrayList<*>>() {
                override fun format(arrayList: ArrayList<*>): String {
                    return "LogUtils Formatter ArrayList { $arrayList }"
                }
            })
            .addFileExtraHead("ExtraKey", "ExtraValue")
        LogUtils.i(config.toString())
    }

    /**
     * 崩溃的时候回调方法
     */
    private  fun initCrash() {
        CrashUtils.init { crashInfo ->
            crashInfo.addExtraHead("extraKey", "extraValue")
            LogUtils.e(crashInfo.toString())
            AppUtils.relaunchApp()
        }
    }

    private  fun initDebugMenu() {
        DebugUtils.addDebugs(ArrayList<IDebug>())
    }

    private  fun isDebug(): Boolean {
        if (isDebug == null) isDebug = AppUtils.isAppDebug()
        return isDebug as Boolean
    }

     fun isMainProcess(): Boolean {
        if (isMainProcess == null) isMainProcess = ProcessUtils.isMainProcess()
        return isMainProcess as Boolean
    }
}