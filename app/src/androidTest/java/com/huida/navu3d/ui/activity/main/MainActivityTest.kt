package com.huida.navu3d.ui.activity.main

import androidx.test.filters.SmallTest
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.common.NmeaProviderManager
import junit.framework.TestCase

/**
 * 作者 : lei
 * 时间 : 2021/02/24.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class MainActivityTest : TestCase(){
    @SmallTest
    fun test() {

        println("PointData().X")
        println(PointData().x)
        val freq: Long = (1000 / NmeaProviderManager.mNmeaBuilder.nudHz).toLong()
        var startTime=System.currentTimeMillis()
        PointData().save()

        println("end")
    }
}