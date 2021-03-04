package com.huida.navu3d

import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorFactoryLocal
import com.esri.core.geometry.OperatorGeneralize
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.NmeaProviderManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.junit.Test
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
        GlobalScope.launch(Dispatchers.IO) {
            async {
            println("start1" + Thread.currentThread())

            }
            println("start2" + Thread.currentThread())
            flow<Int> {
                println("1" + Thread.currentThread())
                emit(1)
            }.flowOn(Dispatchers.IO).map {
                println("2" + Thread.currentThread())

            }.collect {
                println("3" + Thread.currentThread())
            }
        }
        sleep(2000)
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