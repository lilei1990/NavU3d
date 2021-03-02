package com.huida.navu3d

import com.esri.core.geometry.Operator
import com.esri.core.geometry.OperatorFactoryLocal
import com.esri.core.geometry.OperatorGeneralize
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.common.NmeaProviderManager
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
//        val start = System.currentTimeMillis()
//        for (i in (0 until 100000)) {
//            val ll = LatLng(84.0, 23.0)
////            val convertUTMToLatLon = GeoConvert.INSTANCE.convertUTMToLatLon(84.0, 23.0)
//            val utm = ll.toUTMRef()
////            val e = utm.toLatLng()
//        }
//        println("${System.currentTimeMillis() - start}")
        //将原始离散点转换成折线
        val outpm = Polyline()
        var a=0.01
        var b=0.02
        outpm.startPath(a, b)
        for (i in 1..1000) {
            a+=0.01
            outpm.lineTo(a,Math.cos((i*i- 1).toDouble()))
        }
        //抽稀操作
        val generalizer =
            OperatorFactoryLocal
                .getInstance()
                .getOperator(Operator.Type.Generalize) as OperatorGeneralize
        //1 传入点抽稀
        var outputGeom = generalizer.execute(
            outpm,
            0.03,
            true,
            null
        ) as Polyline
        val pathSize = outputGeom.getPathSize(0)
        for (i in (0 until pathSize)) {
            val point = outputGeom.getPoint(i)
            println("${point.x}--${point.y}")

        }

        val points: List<Point> =
            ArrayList()
        for (i in 1..1000) {
            a+=0.01
            outpm.lineTo(a,a)
        }
//        for (double x = 0; x < 4; x += 0.05) {
//    points.add(new MyPoint(x, Math.cos(x*x - 1)));
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