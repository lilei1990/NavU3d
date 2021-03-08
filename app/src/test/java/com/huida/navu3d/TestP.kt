package com.huida.navu3d

import com.huida.navu3d.bean.PointData
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.utils.GeometryUtils
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 作者 : lei
 * 时间 : 2021/02/24.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class TestP {
    //点的队列
    val mPointQueue = ConcurrentLinkedQueue<Int>()

    //116.407387,39.904179
    var latitude1 = 39.9
    var latitude2 = 39.1f
    var longitude = 116.407387

    @Test
    fun test() {
        println(latitude1.toInt())
        println(latitude2.toInt())
    }



}