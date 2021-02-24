package com.huida.navu3d

import com.huida.navu3d.bean.PointData
import com.huida.navu3d.common.NameProviderManager
import com.huida.navu3d.utils.GeoConvert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.thread
import kotlin.coroutines.EmptyCoroutineContext

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
//    latitude = 39.904179
//    longitude = 116.407387
    @Test
    fun test() {
        for (i in 0..31) {
            val convertLatLonToUTM = GeoConvert.INSTANCE.convertLatLonToUTM(i*1.0, 116.407387)
            println(convertLatLonToUTM[0])
        }
        val gaussToBLToGauss = GeoCvUt.GaussToBLToGauss(116.404269, 39.982638)
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
        val freq: Long = (1000 / NameProviderManager.mNmeaBuilder.nudHz).toLong()
        var startTime = System.currentTimeMillis()
        println("开始")
        var endTime = System.currentTimeMillis()
        for (i in 1..100) {

            println("-$i")
            var ab = PointData()
            ab.X = 100.0
            ab.Y = 100.0
            sleep(100)
            endTime = System.currentTimeMillis()
        }
        println("用时:${endTime - startTime}")
    }

    private fun runTest1() {
        for (i in 1..100) {

            println("-$i")
            var ab = PointData()
            ab.X = 100.0
            ab.Y = 100.0
            sleep(100)
        }
    }
}