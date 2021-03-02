package com.huida.navu3d.app

import android.app.Application
import androidx.annotation.CallSuper




/**
 * 作者 : lei
 * 时间 : 2021/03/02.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
 interface AppInitialization {
    fun onAppCreate(application: Application?)
}