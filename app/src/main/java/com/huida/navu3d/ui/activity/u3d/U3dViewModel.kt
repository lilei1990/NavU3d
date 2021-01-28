package com.huida.navu3d.ui.activity.u3d

import androidx.lifecycle.ViewModel
import com.esri.core.geometry.*
import java.util.*

class U3dViewModel : ViewModel() {
    /**
     * 设置A点
     */
    fun setPointA() {


    }
    /**
     * 设置B点
     */
    fun setPointB() {


    }

    /**
     * 开始
     */
    fun start() {


    }

    /**
     * 停止
     */
    fun stop() {


    }
    /**
     * 暂停
     */
    fun pause() {


    }
    /**
     * 录制
     */
    fun saveRecord() {

    }
    /**
     * 画平行线
     */
    fun DrawParallelLine() {

    }


    fun createPolyline1(): ArrayList<Polyline> {

        //偏移操作
        val offseter = OperatorFactoryLocal
            .getInstance()
            .getOperator(Operator.Type.Offset) as OperatorOffset
        val polylines =
            ArrayList<Polyline>()
        val line = Polyline()
        line.startPath(Math.random() * 1500, Math.random() * 900)
        line.lineTo(Math.random() * 1500, Math.random() * 900)
        for (i in 0..12) {
            val polyline = offseter.execute(
                line,
                null,
                100 * i.toDouble(),
                OperatorOffset.JoinType.Round,
                0.0,
                180.0,
                null
            ) as Polyline
            polylines.add(polyline)
        }
        return polylines
    }

    fun createMultipoint1(): MultiPoint? {
        val mPoint = MultiPoint()
        for (i in 0..49) {
            mPoint.add(Math.random() * 1500, Math.random() * 900)
        }
        return mPoint
    }

}