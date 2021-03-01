package com.huida.navu3d

import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.EncapPointData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.utils.GeoConvert
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import uk.me.jstott.jcoord.LatLng
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread

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
    var latitude = 39.904179
    var longitude = 116.407387

    @Test
    fun test() {
        val start = System.currentTimeMillis()
        for (i in (0 until 100000)) {
            val ll = LatLng(84.0, 23.0)
//            val convertUTMToLatLon = GeoConvert.INSTANCE.convertUTMToLatLon(84.0, 23.0)
            val utm = ll.toUTMRef()
//            val e = utm.toLatLng()
        }
        println("${System.currentTimeMillis() - start}")


    }

    fun runTest2() {
        thread {
            fixedRateTimer(period = 1000) {
                for (i in 1..10) {
                    mPointQueue.add(i)
                }
            }
        }.run()
        thread {
            fixedRateTimer(period = 1000) {
                while (true) {
                    val poll = mPointQueue.poll()
                    poll ?: break
                    println("$poll")
                }
            }
        }.run()

        sleep(100000)

    }

    private fun runTest() {
        val freq: Long = (1000 / NmeaProviderManager.mNmeaBuilder.nudHz).toLong()
        var startTime = System.currentTimeMillis()
        println("开始")
        var endTime = System.currentTimeMillis()
        for (i in 1..100) {

            println("-$i")
            var ab = PointData()
            ab.x = 100.0
            ab.y = 100.0
            sleep(100)
            endTime = System.currentTimeMillis()
        }
        println("用时:${endTime - startTime}")
    }

    private fun runTest1() {
        for (i in 1..100) {

            println("-$i")
            var ab = PointData()
            ab.x = 100.0
            ab.y = 100.0
            sleep(100)
        }
    }
}