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
    var latitude = 39.904179
    var longitude = 116.407387

    @Test
    fun test() {
        val A = PointData()
        A.x = 0.0
        A.y = 0.0
        val B = PointData()
        B.x = -1.0
        B.y = 1.0
        GeometryUtils.extLine(A, B, 1)
        GeometryUtils.extLine(B, A, 1)
        println(A.toString())
        println(B.toString())
//        //补点操作
//        val polyline = Polyline()
//        polyline.startPath(0.0, 0.0)
//        polyline.lineTo(2.0, 0.0)
//        //补点操作
//        val densifier =
//            OperatorFactoryLocal
//                .getInstance()
//                .getOperator(Operator.Type.DensifyByLength) as OperatorDensifyByLength
//        //3 20厘米等间距补点
//
//        //3 20厘米等间距补点
//        val outputGeom =
//            densifier.execute(polyline, 0.1, null) as Polyline
//        val jsonString: String = OperatorExportToJson.local().execute(null, outputGeom)
//        println(jsonString)
//        val polyline = Polyline()
//        polyline.startPath(0.0, 0.0)
//        polyline.lineTo(2.0, 0.0)
//        var outputGeom = OperatorBuffer.local().execute(polyline, null, 2.0, null);
//
//        val jsonString: String = OperatorExportToJson.local().execute(null, outputGeom)
//        println("Buffer distance = " + java.lang.Double.toString(1.0) + ":")
//        println("面积:" + ":" + outputGeom.calculateArea2D())
//        println(jsonString)
//        println()
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